package com.lyna.web.domain.stores.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.stores.service.StoreService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StoreServiceImpl extends BaseService implements StoreService {
    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);
    @Autowired
    private StoreRepository storeRepository;

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
    public Page<Store> findPaginated(Pageable pageable, int tenantId, String searchText) {
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

}
