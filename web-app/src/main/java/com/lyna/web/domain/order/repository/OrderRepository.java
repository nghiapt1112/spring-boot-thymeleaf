package com.lyna.web.domain.order.repository;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.OrderView;
import com.lyna.web.domain.view.CsvOrder;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public interface OrderRepository extends JpaRepository<Order, String> {

    Iterator<CsvOrder> getMapOrder(Reader targetReader, Map<Integer, String> mapHeader);

    Order save(Order order);

    boolean checkExists(String postCourseId, String productId, String quantity);

    List<Order> findByTenantId(int tenantId);

    List<Order> findByTenantIdAndPostCourseId(int tenantId, String postCourseId);

    List<String> findByTenantIdAndPostCourseId(int tenantId, List<String> postCourseIds);

    List<OrderView> findOverViews(int tenantId, RequestPage orderRequestPage);

    String getByPostCourseIdOrderDateTenantId(String postcourseId, String orderDate, int tenantId);

    List<OrderDetail> getMapByPostCourseIdOrderDateTenantId(String postCourseId, String orderDate, int tenantId);

    void deleteByTenantIdAndOrderId(int tenantId, List<String> OrderIds);

    Map<String, Integer> getHeaderOrder(Reader reader);

    List<CSVRecord> getDataOrder(Reader reader);
}
