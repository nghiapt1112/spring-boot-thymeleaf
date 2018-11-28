package com.lyna.web.domain.stores.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.postCourse.Exception.PostCourseException;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.exception.StoreException;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class StoreServiceImpl extends BaseService implements StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PostCourseRepository postCourseRepository;


    public List<Store> findAll(int tenantId) {
        //TODO: =>nghia.pt replace with storeRepository.findByTenant(...)
        return this.storeRepository.findAll(tenantId);
    }

    @Override
    public List<Store> getStoreList(int principal) {
        return storeRepository.getAll(principal);
    }


    @Transactional
    @Override
    public void createStore(Store store, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        int tenantId = currentUser.getTenantId();
        String username = currentUser.getId();
        Date date = new Date();

        store.setCreateDate(date);
        store.setTenantId(tenantId);
        store.setCreateUser(username);
        List<PostCourse> postCourses = store.getPostCourses();
        if (Objects.isNull(postCourses)) {
            throw new PostCourseException(toInteger("err.store.savePostCourseNull.code"), toStr("err.store.savePostCourseNull.code"));
        } else if (postCourses.isEmpty()) {
            throw new PostCourseException(toInteger("err.store.savePostCourseEmpty.code"), toStr("err.store.savePostCourseEmpty.code"));
        } else {
            for (PostCourse postCourse : postCourses) {
                postCourse.setTenantId(tenantId);
                postCourse.setStoreId(store.getStoreId());
                postCourse.setCreateDate(date);
                postCourse.setCreateUser(username);
            }
        }

        store.setPostCourses(postCourses);
        store.setPostCourses(postCourses);
        try {
            storeRepository.save(store);
        } catch (Exception ex) {
            throw new StoreException(toInteger("err.store.saveFailed.code"), toStr("err.store.saveFailed.msg"));
        }
    }


    @Transactional
    @Override
    public void updateStore(Store store, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        int tenantId = currentUser.getTenantId();
        String id = currentUser.getId();
        Date date = new Date();

        store.setUpdateDate(date);
        store.setUpdateUser(id);
        store.setTenantId(tenantId);
        List<PostCourse> postCourses = store.getPostCourses();
        System.out.println("postCourses");
        if (Objects.isNull(postCourses)) {
            throw new PostCourseException(toInteger("err.store.savePostCourseNull.code"), toStr("err.store.savePostCourseNull.code"));
        } else if (postCourses.isEmpty()) {
            throw new PostCourseException(toInteger("err.store.savePostCourseEmpty.code"), toStr("err.store.savePostCourseEmpty.code"));
        } else {
            for (PostCourse postCourse : postCourses) {
                PostCourse pc = null;
                if (Objects.isNull(postCourse.getStoreId()) || postCourse.getStoreId().isEmpty()) {
                    System.out.println("PostCourseId NULL");
                    pc = new PostCourse();
                    postCourse.setPostCourseId(pc.getPostCourseId());
                    postCourse.setCreateUser(id);
                    postCourse.setCreateDate(date);
                    postCourse.setTenantId(tenantId);
                    postCourse.setStoreId(store.getStoreId());
                } else {
                    postCourse.setUpdateDate(date);
                    postCourse.setUpdateUser(id);

                }
            }

        }
        store.setPostCourses(postCourses);
        try {
            storeRepository.save(store);
        } catch (Exception ex) {
            throw new StoreException(toInteger("err.store.saveFailed.code"), toStr("err.store.saveFailed.msg"));
        }
    }

    @Override
    public Store findOneByStoreId(String storeId) {
        return storeRepository.findOneByStoreId(storeId);
    }

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }
}
