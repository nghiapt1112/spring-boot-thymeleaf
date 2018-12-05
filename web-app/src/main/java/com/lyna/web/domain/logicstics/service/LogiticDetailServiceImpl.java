package com.lyna.web.domain.logicstics.service;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.logicstics.repository.LogiticDetailRepository;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.mpackage.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogiticDetailServiceImpl extends BaseService implements LogiticDetailService {

    @Autowired
    private LogiticDetailRepository logiticDetailRepository;

    @Override
    public boolean deletebyPackageId(List<String> listPackageId) {
        return logiticDetailRepository.deletebyPackageId(listPackageId);
    }
}
