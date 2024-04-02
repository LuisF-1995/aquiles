package com.lurodev.aquilestenant.service;

import com.lurodev.aquilestenant.dto.RequestResponse;
import com.lurodev.aquilestenant.dto.TenantRegisterContainer;
import com.lurodev.aquilestenant.model.Tenant;

import java.util.List;

public interface ITenantService {
    List<Tenant> getAllTenants();
    List<Tenant> getTenantsByCountry(String country);
    List<Tenant> getTenantsByStatus(Boolean status);
    Tenant getTenantById(Integer tenantId);
    Tenant getTenantByCompanyName(String companyName);
    Tenant getTenantByCompanyId(String companyId); //Company ID es el mismo NIT en colombia
    RequestResponse registerTenant(TenantRegisterContainer tenantRegisterContainer);
    RequestResponse updateTenant(Tenant tenant);
    RequestResponse deleteTenantById(Integer tenantId);
}
