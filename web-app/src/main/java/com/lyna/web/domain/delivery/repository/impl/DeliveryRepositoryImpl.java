package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.delivery.Delivery;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.repository.DeliveryRepository;
import com.lyna.web.domain.view.CsvDelivery;
import com.lyna.web.domain.view.CsvOrder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

@Repository
public class DeliveryRepositoryImpl extends BaseRepository<Delivery, Long> implements DeliveryRepository {

    public DeliveryRepositoryImpl(EntityManager em) {
        super(Delivery.class, em);
    }

    @Override
    public String checkExistByOrderIdAndOrderDate(String orderId, String orderDate) {
        List list = entityManager
                .createQuery("SELECT d.deliveryId FROM Delivery d WHERE d.orderId = :orderId")
                .setParameter("orderId", orderId)
                .getResultList();
        if (list != null && list.size() > 0)
            return (String) list.get(0);

        return null;
    }

    @Override
    public Iterator<CsvDelivery> getMapDelivery(Reader reader) {
        CsvToBean<CsvDelivery> csvToBean = new CsvToBeanBuilder(reader)
                .withType(CsvDelivery.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<CsvDelivery> csvUserIterator = csvToBean.iterator();
        return csvUserIterator;
    }
}
