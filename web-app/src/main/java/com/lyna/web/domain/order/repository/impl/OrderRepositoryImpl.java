package com.lyna.web.domain.order.repository.impl;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DateTimeUtils;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.OrderView;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.storagefile.exeption.StorageException;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.view.CsvOrder;
import com.lyna.web.infrastructure.repository.BaseRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl extends BaseRepository<Order, String> implements OrderRepository {

    private final Logger log = LoggerFactory.getLogger(OrderRepositoryImpl.class);

    public OrderRepositoryImpl(EntityManager em) {
        super(Store.class, em);
    }

    @Override
    public Iterator<CsvOrder> getMapOrder(Reader targetReader, Map<Integer, String> mapHeader) {

        List<CSVRecord> dataOrder = getDataOrder(targetReader);
        List<CsvOrder> csvOrders = new ArrayList<>();

        dataOrder.forEach(csvRecord -> {
            CsvOrder csvOrder = new CsvOrder();

            mapHeader.forEach((index, key) -> {
                if ("日付".equals(key)) {
                    csvOrder.setOrderDate(csvRecord.get(index));
                } else if ("店舗".equals(key)) {
                    csvOrder.setStoreName(csvRecord.get(index));
                } else if ("店舗コード".equals(key)) {
                    csvOrder.setStoreCode(csvRecord.get(index));
                } else if ("便".equals(key)) {
                    csvOrder.setPost(csvRecord.get(index));
                } else if ("商品コード".equals(key)) {
                    csvOrder.setProductCode(csvRecord.get(index));
                } else if ("商品".equals(key)) {
                    csvOrder.setProductName(csvRecord.get(index));
                } else if ("個数".equals(key)) {
                    csvOrder.setQuantity(csvRecord.get(index));
                } else if ("大分類".equals(key)) {
                    csvOrder.setCategory1(csvRecord.get(index));
                } else if ("中分類".equals(key)) {
                    csvOrder.setCategory2(csvRecord.get(index));
                } else if ("小分類".equals(key)) {
                    csvOrder.setCategory3(csvRecord.get(index));
                }
            });

            csvOrders.add(csvOrder);
        });

        return csvOrders.iterator();
    }

    @Override
    public Map<String, Integer> getHeaderOrder(Reader reader) {
        CSVParser csvParser;
        try {
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().withTrim());
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new StorageException(Constants.PARSE_CSV_FAILED);

        }
        Map<String, Integer> hashMap = csvParser
                .getHeaderMap()
                .entrySet()
                .stream().parallel().
                        sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        return hashMap;
    }

    @Override
    public List<CSVRecord> getDataOrder(Reader reader) {
        CSVParser csvParser;
        try {
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            return csvParser.getRecords();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new StorageException(Constants.PARSE_CSV_FAILED);
        }
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
        return order;
    }

    @Override
    public boolean checkExists(String postCourseId, String productId, String quantity) throws StorageException {
        try {
            BigDecimal amount = new BigDecimal(quantity);
            String query = "SELECT a FROM Order a inner join OrderDetail b on a.orderId = b.orderId WHERE a.postCourseId = :postCourseId AND b.productId = :productId And b.amount = :amount";
            List list = entityManager.createQuery(query)
                    .setParameter(Constants.POST_COURSE_ID, postCourseId)
                    .setParameter("productId", productId)
                    .setParameter("amount", amount)
                    .getResultList();
            if (list != null && list.size() > 0)
                return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new StorageException(Constants.PARSE_CSV_FAILED);
        }
        return false;
    }

    @Override
    public List<Order> findByTenantId(int tenantId) {
        return entityManager.createQuery("SELECT o FROM Order o WHERE o.tenantId = :tenantId")
                .setParameter(Constants.TENANT_ID, tenantId)
                .getResultList();
    }

    @Override
    public List<Order> findByTenantIdAndPostCourseId(int tenantId, String postCourseId) {
        return entityManager.createQuery("SELECT o FROM Order o WHERE o.tenantId = :tenantId AND o.postCourseId = :postCourseId")
                .setParameter(Constants.TENANT_ID, tenantId)
                .setParameter(Constants.POST_COURSE_ID, postCourseId)
                .getResultList();
    }

    @Override
    public List<String> findByTenantIdAndPostCourseId(int tenantId, List<String> postCourseIds) {
        return entityManager.createQuery("SELECT o.orderId FROM Order o WHERE o.tenantId = :tenantId AND o.postCourseId in (:postCourseIds)")
                .setParameter(Constants.TENANT_ID, tenantId)
                .setParameter("postCourseIds", postCourseIds)
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
    public String getByPostCourseIdOrderDateTenantId(String postCourseId, String orderDate, int tenantId) throws StorageException {
        try {
            Date date = DateTimeUtils.converStringToDate(orderDate);
            if (date != null) {
                String query = "SELECT a.orderId FROM Order a " +
                        "WHERE a.postCourseId = :postCourseId and a.orderDate = :orderDate and a.tenantId = :tenantId";
                List list = entityManager.createQuery(query)
                        .setParameter(Constants.POST_COURSE_ID, postCourseId)
                        .setParameter("orderDate", date)
                        .setParameter("tenantId", tenantId)
                        .getResultList();
                if (list != null && list.size() > 0)
                    return (String) list.get(0);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new StorageException(Constants.PARSE_CSV_FAILED);
        }
        return null;
    }

    @Override
    public List<OrderDetail> getMapByPostCourseIdOrderDateTenantId(String postCourseId, String orderDate, int tenantId) throws StorageException {
        try {
            Date date = DateTimeUtils.converStringToDate(orderDate);
            if (date != null) {
                String query = "SELECT d FROM Order a JOIN OrderDetail d on a.orderId = d.orderId " +
                        "WHERE a.postCourseId = :postCourseId and a.orderDate = :orderDate and a.tenantId = :tenantId";
                List<OrderDetail> list = entityManager.createQuery(query, OrderDetail.class)
                        .setParameter(Constants.POST_COURSE_ID, postCourseId)
                        .setParameter("orderDate", date)
                        .setParameter("tenantId", tenantId)
                        .getResultList();
                if (list != null && list.size() > 0) {
                    return list;
                }

            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new StorageException(Constants.PARSE_CSV_FAILED);
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteByTenantIdAndOrderId(int tenantId, List<String> OrderIds) {
        String query = "DELETE FROM Order l WHERE l.orderId in (:OrderIds) AND l.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("OrderIds", OrderIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
    }

}
