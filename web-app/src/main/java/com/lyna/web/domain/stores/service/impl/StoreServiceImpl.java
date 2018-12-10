package com.lyna.web.domain.stores.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.sevice.PostCourseService;
import com.lyna.web.domain.stores.Store;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class StoreServiceImpl extends BaseService implements StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PostCourseService postCourseService;

    @Autowired
    private UserStoreAuthorityRepository userStoreAuthorityRepository;

    public List<Store> findAll(int tenantId) {
        //TODO: =>nghia.pt replace with storeRepository.findByTenant(...)
        return this.storeRepository.findAll(tenantId);
    }

    @Override
    public List<Store> getStoreList(int tenantId, String search) {
        return storeRepository.getAll(tenantId, search);
    }

    @Override
    public List<Store> getStoreList(int tenantId) {
        return storeRepository.findAll(tenantId);
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
    public String deleteStore(String storeIds) {
        boolean isDeletedStore = false;
        List<String> listStoreId = new ArrayList();

        String[] arrayStoreId = storeIds.split(",");
        for (String storeId : arrayStoreId) {
            listStoreId.add(storeId);
        }

        if (userStoreAuthorityRepository.deleteStoreAuthorityByStoreId(listStoreId)) {
            isDeletedStore = storeRepository.deletebyStoreId(listStoreId);
        }

        if (isDeletedStore)
            return toStr("delete.msg.success.code");
        else
            return toStr("err.store.delete.msg");
    }

    @Transactional
    @Override
    public void createStore(Store store, UsernamePasswordAuthenticationToken principal) throws DomainException {
        User currentUser = (User) principal.getPrincipal();
        Date date = new Date();
        store.setCreateDate(date);
        store.setTenantId(currentUser.getTenantId());
        store.setCreateUser(currentUser.getId());
        List<PostCourse> postCourses = store.getPostCourses();
        if (!Objects.isNull(postCourses) && !postCourses.isEmpty()) {
            for (PostCourse postCourse : postCourses) {
                postCourse.setTenantId(currentUser.getTenantId());
                postCourse.setStoreId(store.getStoreId());
                postCourse.setCreateDate(date);
                postCourse.setCreateUser(currentUser.getId());
            }
            store.setPostCourses(postCourses);
        }
        try {
            storeRepository.save(store);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Transactional
    @Override
    public void updateStore(Store store, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        Date date = new Date();
        List<PostCourse> postCourses = postCourseService.findAllByStoreIdAndTenantId(store.getStoreId(), store.getTenantId());
        List<PostCourse> postCoursesUpdate = store.getPostCourses();
        for (PostCourse postCourse : postCoursesUpdate) {
            if (!postCourses.contains(postCourse)) {
                postCourse.setCreateDate(date);
                postCourse.setCreateUser(currentUser.getId());
                postCourse.setTenantId(currentUser.getTenantId());
                postCourse.setStoreId(store.getStoreId());
                postCourses.add(postCourse);
            } else {
                for (int i = 0; i < postCourses.size(); i++) {
                    if (postCourse.getPostCourseId().equals(postCourses.get(i).getPostCourseId())) {
                        postCourse.setUpdateDate(date);
                        postCourse.setUpdateUser(currentUser.getId());
                        postCourse.setTenantId(currentUser.getTenantId());
                        postCourses.set(i, postCourse);
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
        store.setUpdateUser(currentUser.getId());
        store.setTenantId(currentUser.getTenantId());
        store.setPostCourses(postCourses);
        storeRepository.save(store);
    }

    @Override
    public Store findOneByStoreId(String storeId) {
        return storeRepository.findOneByStoreId(storeId);
    }

    @Override
    public Store findOneByCode(String code) {
        return storeRepository.findOneByCode(code);
    }

}
