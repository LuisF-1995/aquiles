package com.lurodev.aquilestenant.repository;

import com.lurodev.aquilestenant.model.ApplicationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IServiceRepository extends JpaRepository<ApplicationService, Integer> {
    ApplicationService findByServiceName(String serviceName);
    ApplicationService findFirstByCompanyIdAndServiceName(String companyId, String serviceName);
    List<ApplicationService> findAllByIsDefault(Boolean isDefault);
}
