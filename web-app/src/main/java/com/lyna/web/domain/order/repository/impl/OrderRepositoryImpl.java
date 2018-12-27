package com.lyna.web.domain.order.repository.impl;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.utils.DateTimeUtils;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderView;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.storagefile.exeption.StorageException;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.view.CsvOrder;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends BaseRepository<Order, String> implements OrderRepository {

    private final Logger log = LoggerFactory.getLogger(OrderRepositoryImpl.class);

    public OrderRepositoryImpl(EntityManager em) {
        super(Store.class, em);
    }

    @Override
    public Iterator<CsvOrder> getMapOrder(Reader targetReader) {
        CsvToBean<CsvOrder> csvToBean = new CsvToBeanBuilder(targetReader)
                .withType(CsvOrder.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<CsvOrder> csvUserIterator = csvToBean.iterator();
        return csvUserIterator;
    }

    @Override
    public Order save(Order order) {
        try {
            if (order.getOrderId() == null) {
                entityManager.persist(order);
                return order;
            } else {
                entityManager.merge(order);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean checkExists(String postCourseId, String productId, String quantity) throws StorageException {
        try {
            BigDecimal amount = new BigDecimal(quantity);
            String query = "SELECT a FROM Order a inner join OrderDetail b on a.orderId = b.orderId WHERE a.postCourseId = :postCourseId AND b.productId = :productId And b.amount = :amount";
            List list = entityManager.createQuery(query)
                    .setParameter("postCourseId", postCourseId)
                    .setParameter("productId", productId)
                    .setParameter("amount", amount)
                    .getResultList();
            if (list != null && list.size() > 0)
                return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new StorageException("CSVのデータが不正。");
        }
        return false;
    }

    @Override
    public List<Order> findByTenantId(int tenantId) {
        return entityManager.createQuery("SELECT o FROM Order o WHERE o.tenantId = :tenantId")
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    public List<Order> findByTenantIdAndPostCourseId(int tenantId, String postCourseId) {
        return entityManager.createQuery("SELECT o FROM Order o WHERE o.tenantId = :tenantId AND o.postCourseId = :postCourseId")
                .setParameter("tenantId", tenantId)
                .setParameter("postCourseId", postCourseId)
                .getResultList();
    }

    @Override
    public List<OrderView> findOverViews(int tenantId, RequestPage orderRequestPage) {
        TypedQuery<OrderView> tQuery = entityManager.createQuery(
                orderRequestPage.getSelect()
                        .append(orderRequestPage.getFrom())
                        .append(orderRequestPage.getWhere())
                        .toString(), OrderView.class);
        fillParams(tQuery, orderRequestPage.getParams());
        return tQuery.getResultList();
    }

    @Override
    public String checkExists(String postCourseId, String orderDate, int tenantId) throws StorageException {
        try {
            Date date = DateTimeUtils.converStringToDate(orderDate);
            if (date != null) {
                String query = "SELECT a.orderId FROM Order a " +
                        "WHERE a.postCourseId = :postCourseId and a.orderDate = :orderDate and a.tenantId = :tenantId";
                List list = entityManager.createQuery(query)
                        .setParameter("postCourseId", postCourseId)
                        .setParameter("orderDate", date)
                        .setParameter("tenantId", tenantId)
                        .getResultList();
                if (list != null && list.size() > 0)
                    return (String) list.get(0);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new StorageException("CSVのデータが不正。");
        }
        return null;
    }

}
