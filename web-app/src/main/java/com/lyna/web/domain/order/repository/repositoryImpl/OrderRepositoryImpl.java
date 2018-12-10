package com.lyna.web.domain.order.repository.repositoryImpl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.exception.StorageException;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.view.CsvDelivery;
import com.lyna.web.domain.view.CsvOrder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends BaseRepository<Order, String> implements OrderRepository {

    private final Logger log = LoggerFactory.getLogger(OrderRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public OrderRepositoryImpl(EntityManager em) {
        super(Store.class, em);
    }

    @Override
    public Iterator<CsvOrder> getMapOrder(Reader targetReader) {
        CsvToBean<CsvOrder> csvToBean = new CsvToBeanBuilder(targetReader)
                .withType(CsvOrder.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<CsvOrder> csvOrderIterator = csvToBean.iterator();
        return csvOrderIterator;
    }

    @Override
    public Iterator<CsvDelivery> getMapDelivery(Reader targetReader) {
        CsvToBean<CsvDelivery> csvToBean = new CsvToBeanBuilder(targetReader)
                .withType(CsvOrder.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<CsvDelivery> csvDeliveryIterator = csvToBean.iterator();
        return csvDeliveryIterator;
    }

    @Override
    public Order save(Order order) {
        try {
            if (order.getOrderId() == null) {
                em.persist(order);
                return order;
            } else {
                em.merge(order);
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
}
