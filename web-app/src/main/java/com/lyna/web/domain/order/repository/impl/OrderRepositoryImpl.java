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

        int[] dx = {0};
        dataOrder.forEach(csvRecord -> {
            CsvOrder csvOrder = new CsvOrder();

            mapHeader.forEach((index, key) -> {
                switch (key) {
                    case "日付":
                        csvOrder.setOrderDate(csvRecord.get(index));
                        break;
                    case "店舗":
                        csvOrder.setStoreName(csvRecord.get(index));
                        break;
                    case "店舗コード":
                        csvOrder.setStoreCode(csvRecord.get(index));
                        break;
                    case "便":
                        csvOrder.setPost(csvRecord.get(index));
                        break;
                    case "商品コード":
                        csvOrder.setProductCode(csvRecord.get(index));
                        break;
                    case "商品":
                        csvOrder.setProductName(csvRecord.get(index));
                        break;
                    case "個数":
                        csvOrder.setQuantity(csvRecord.get(index));
                        break;
                    case "大分類":
                        csvOrder.setCategory1(csvRecord.get(index));
                        break;
                    case "中分類":
                        csvOrder.setCategory2(csvRecord.get(index));
                        break;
                    case "小分類":
                        csvOrder.setCategory3(csvRecord.get(index));
                        break;
                }
            });

            csvOrders.add(csvOrder);
        });

        return csvOrders.iterator();
    }

    @Override
    public Map<String, Integer> getHeaderOrder(Reader reader) {
        CSVParser csvParser = null;
        try {
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().withTrim());
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new StorageException("CSVのデータが不正。");

        }
        Map<String, Integer> hashMap = csvParser.getHeaderMap().entrySet().stream().
                sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        return hashMap;
    }

    @Override
    public List<CSVRecord> getDataOrder(Reader reader) {
        CSVParser csvParser = null;
        try {
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            return csvParser.getRecords();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new StorageException("CSVのデータが不正。");
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
    public List<String> findByTenantIdAndPostCourseId(int tenantId, List<String> postCourseIds) {
        return entityManager.createQuery("SELECT o.orderId FROM Order o WHERE o.tenantId = :tenantId AND o.postCourseId in (:postCourseIds)")
                .setParameter("tenantId", tenantId)
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

    @Override
    public void deleteByTenantIdAndOrderId(int tenantId, List<String> OrderIds) {
        String query = "DELETE FROM Order l WHERE l.orderId in (:OrderIds) AND l.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("OrderIds", OrderIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
    }

}
