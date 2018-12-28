package com.lyna.web.domain.stores.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.repository.DeliveryRepository;
import com.lyna.web.domain.logicstics.repository.LogisticDetailRepository;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.postCourse.sevice.PostCourseService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.exception.StoreException;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.repository.UserStoreAuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class StoreServiceImpl extends BaseService implements StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private PostCourseService postCourseService;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PostCourseRepository postCourseRepository;
    @Autowired
    private UserStoreAuthorityRepository userStoreAuthorityRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private LogisticRepository logisticRepository;
    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private LogisticDetailRepository logisticDetailRepository;


    public List<Store> findByTenantId(int tenantId) {
        return this.storeRepository.findByTenantId(tenantId);
    }

    public List<Store> findAll(int tenantId) {
        return this.storeRepository.findAll(tenantId);
    }

    @Override
    public List<Store> getStoreList(int tenantId, String search) {
        return storeRepository.getAll(tenantId, search);
    }

    @Override
    public List<Store> getStoreList(int tenantId) {
        return storeRepository.findByTenantId(tenantId);
    }

    @Override
    public Page<Store> findPaginated(int tenantId) throws DomainException {
        List<Store> stores = getStoreList(tenantId);
        Page<Store> storePage =
                new PageImpl(stores, PageRequest.of(1, 5), stores.size());
        return storePage;
    }

    @SuppressWarnings("unused")
    @Override
    public Page<Store> findPaginated(Pageable pageable, int tenantId, String searchText) throws DomainException {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Store> list;
        List<Store> stores = getStoreList(tenantId, searchText);

        if (stores.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, stores.size());
            list = stores.subList(startItem, toIndex);
        }

        Page<Store> storePage =
                new PageImpl(list, PageRequest.of(currentPage, pageSize), stores.size());
        return storePage;
    }

    @Override
    @Transactional
    public String deleteStoreAndTenantId(List<String> storeIds, int tenantId) {
        boolean isDeletedStore = false;

        userStoreAuthorityRepository.deleteStoreAuthorityByStoreId(storeIds);

        List<String> postCoursesIds = postCourseRepository.findAllByStoreIdAndTenantId(tenantId, storeIds);

        if (postCoursesIds != null && postCoursesIds.size() > 0) {
            List<String> orderIds = orderRepository.findByTenantIdAndPostCourseId(tenantId, postCoursesIds);

            if (orderIds != null && orderIds.size() > 0) {
                List<String> deliveryIds = deliveryRepository.findByTenantIdAndOrderId(tenantId, orderIds);

                List<String> logisticIds = logisticRepository.findByTenantIdAndOrderIds(tenantId, orderIds);

                if (logisticIds != null && logisticIds.size() > 0)
                    logisticDetailRepository.deleteByLogisticsIdAndTenantId(logisticIds, tenantId);

                if (deliveryIds != null && deliveryIds.size() > 0)
                    deliveryDetailRepository.deleteDeliveryDetailByDeliveryIdsAndTenantId(deliveryIds, tenantId);

                if (orderIds != null && orderIds.size() > 0)
                    orderDetailRepository.deleteByOrderIdsAndTenantId(orderIds, tenantId);

                if (deliveryIds != null && deliveryIds.size() > 0)
                    deliveryRepository.deleteByTenantIdAndDeliveryIds(tenantId, deliveryIds);

                if (logisticIds != null && logisticIds.size() > 0)
                    logisticRepository.deleteByLogisticsIdsAndTenantId(logisticIds, tenantId);

                if (orderIds != null && orderIds.size() > 0)
                    orderRepository.deleteByTenantIdAndOrderId(tenantId, orderIds);
            }

            if (postCoursesIds != null && postCoursesIds.size() > 0)
                postCourseRepository.deleteByPostCourseIdsAndTenantId(postCoursesIds, tenantId);
        }

        if (storeIds != null && storeIds.size() > 0)
            isDeletedStore = storeRepository.deleteByStoreIdsAndTenantId(storeIds, tenantId);

        if (isDeletedStore)
            return toStr("delete.msg.success.code");
        else
            return toStr("err.store.delete.msg");
    }

    @Override
    @Transactional
    public void create(Store store, User user) {
        Date date = new Date();
        store.setCreateDate(date);
        store.setTenantId(user.getTenantId());
        store.setCreateUser(user.getId());
        List<PostCourse> postCourses = store.getPostCourses();
        if (!Objects.isNull(postCourses) && !postCourses.isEmpty()) {
            for (PostCourse postCourse : postCourses) {
                postCourse.setTenantId(user.getTenantId());
                postCourse.setStoreId(store.getStoreId());
                postCourse.setCreateDate(date);
                postCourse.setCreateUser(user.getId());
            }
            store.setPostCourses(postCourses);
        }
        try {
            storeRepository.save(store);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.store.createFailed.code"), toStr("err.store.createFailed.msg"));
        }

    }

    @Override
    @Transactional
    public void update(Store store, User user) {
        Date date = new Date();
        List<PostCourse> postCourses = postCourseService.findAllByStoreIdAndTenantId(user.getTenantId(), store.getStoreId());
        List<PostCourse> postCoursesUpdate = store.getPostCourses();
        if (!Objects.isNull(postCoursesUpdate)) {
            for (PostCourse postCourse : postCoursesUpdate) {
                if (!postCourses.contains(postCourse)) {
                    postCourse.setCreateDate(date);
                    postCourse.setCreateUser(user.getId());
                    postCourse.setTenantId(user.getTenantId());
                    postCourse.setStoreId(store.getStoreId());
                    postCourses.add(postCourse);
                } else {
                    for (int i = 0; i < postCourses.size(); i++) {
                        if (postCourse.getPostCourseId().equals(postCourses.get(i).getPostCourseId())) {
                            List<Order> orders = orderRepository.findByTenantIdAndPostCourseId(user.getTenantId(), postCourse.getPostCourseId());
                            if (!Objects.isNull(orders)) {
                                postCourse.setOrders(new HashSet<Order>(orders));
                            }
                            postCourse.setUpdateDate(date);
                            postCourse.setUpdateUser(user.getId());
                            postCourses.set(i, postCourse);
                        }
                    }
                }
            }
        }
        store.setCode(store.getCode());
        store.setName(store.getName());
        store.setMajorArea(store.getMajorArea());
        store.setArea(store.getArea());
        store.setAddress(store.getAddress());
        store.setPhoneNumber(store.getPhoneNumber());
        store.setPersonCharge(store.getPersonCharge());
        store.setUpdateDate(date);
        store.setUpdateUser(user.getId());
        store.setPostCourses(postCourses);
        try {
            storeRepository.save(store);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.store.updateFailed.code"), toStr("err.store.updateFailed.msg"));
        }

    }

    @Override
    public Store findOneByStoreIdAndTenantId(String storeId, int tenantId) {
        try {
            return storeRepository.findOneByStoreIdAndTenantId(storeId, tenantId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.store.notFound.code"), toStr("err.store.notFound.msg"));
        }

    }

    @Override
    public Store findByCodeAndTenantId(String code, int tenantId) {
        try {
            return storeRepository.findByCodeAndTenantId(code, tenantId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.store.notFound.code"), toStr("err.store.notFound.msg"));
        }
    }
}
