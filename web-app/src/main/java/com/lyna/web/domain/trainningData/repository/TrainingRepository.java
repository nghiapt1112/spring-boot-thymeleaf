package com.lyna.web.domain.trainningData.repository;

import com.lyna.web.domain.trainningData.Training;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TrainingRepository extends JpaRepository<Training, String>, PagingRepository {
    List<Training> findByOrderIdTenantId(List<String> orderIds, int tenantId);

    List<Training> findByTenantId(int tenantId);
}
