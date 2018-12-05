package com.lyna.web.domain.logicstics.repository;

import java.util.List;

public interface LogiticDetailRepository {
    boolean deletebyPackageId(List<String> listPackageId);
}
