package com.lurodev.aquilestenant.repository;

import com.lurodev.aquilestenant.model.TenantUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITenantUserRepository extends JpaRepository<TenantUser, UUID> {
    List<TenantUser> findByUsername(String username);
    Optional<TenantUser> findByTenantCompanyIdAndUsername(String tenant_companyId, String username);
}
