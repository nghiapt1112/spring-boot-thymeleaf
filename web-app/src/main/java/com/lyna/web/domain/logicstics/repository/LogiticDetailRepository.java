package com.lyna.web.domain.logicstics.repository;

import java.util.List;

public interface LogiticDetailRepository {
    boolean deleteByPackageIds(List<String> packageIds);
}
