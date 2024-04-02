package com.lurodev.aquilestenant.repository;

import com.lurodev.aquilestenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Integer> {
    Optional<Tenant> findByCompanyId(String companyId); //Company ID es el mismo NIT en colombia
    Optional<Tenant> findByCompanyName(String companyName);
    List<Tenant> findAllByCountry(String country);
    List<Tenant> findAllByIsActive(Boolean isActive);
}
