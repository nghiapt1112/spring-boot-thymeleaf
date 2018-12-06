package com.lyna.web.domain.order.repository.repositoryImpl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.view.CsvOrder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Reader;
import java.util.Iterator;

@Repository
@Transactional(readOnly = true)
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
        Iterator<CsvOrder> csvUserIterator = csvToBean.iterator();
        return csvUserIterator;
    }

    @Override
    @Transactional
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
}
