package com.lyna.web.domain.postCourse.sevice.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.postCourse.sevice.PostCourseService;
import com.lyna.web.domain.stores.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostCourseServiceImpl extends BaseService implements PostCourseService {

    private final Logger log = LoggerFactory.getLogger(PostCourseServiceImpl.class);

    @Autowired
    private PostCourseRepository postCourseRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public List<PostCourse> findAllByStoreIdAndTenantId(int tenantId, String storeId) {
        try {
            return postCourseRepository.findAllByStoreIdAndTenantId(tenantId, storeId);
        } catch (Exception e) {
            throw new DomainException(toInteger("err.general.notFound.code"), toStr("err.general.notFound.msg"));
        }

    }

    @Override
    @Transactional
    public boolean deleteByStoreIdsAndTenantId(List<String> storeIds, int tenantId) {
        try {
            postCourseRepository.deleteByStoreIdsAndTenantId(storeIds, tenantId);
            storeRepository.deleteByStoreIdsAndTenantId(storeIds, tenantId);
            return true;
        } catch (Exception e) {
            throw new DomainException(toInteger("err.general.deleteFailed.code"), toStr("err.general.deleteFailed.msg"));
        }
    }

}
