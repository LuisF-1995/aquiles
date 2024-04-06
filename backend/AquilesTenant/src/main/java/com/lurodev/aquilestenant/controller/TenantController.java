package com.lurodev.aquilestenant.controller;

import com.lurodev.aquilestenant.dto.RequestResponse;
import com.lurodev.aquilestenant.dto.TenantRegisterContainer;
import com.lurodev.aquilestenant.model.Tenant;
import com.lurodev.aquilestenant.service.ITenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("tenant")
@RequiredArgsConstructor
public class TenantController {
    private final ITenantService tenantService;

    @GetMapping
    List<Tenant> getAllTenants(){
        return tenantService.getAllTenants();
    }

    @GetMapping("/{id}")
    RequestResponse getTenantById(@PathVariable Integer id){
        Tenant tenant = tenantService.getTenantById(id);
        RequestResponse response = new RequestResponse(null, false, HttpStatus.NOT_FOUND.value(), "Tenant not found");

        if(tenant != null){
            response.setModel(tenant);
            response.setSuccess(true);
            response.setHttpStatus(HttpStatus.OK.value());
            response.setMessage("Tenant found");
        }

        return response;
    }

    @GetMapping("/company/{companyId}")
    RequestResponse getTenantByCompanyId(@PathVariable String companyId){
        Tenant tenant = tenantService.getTenantByCompanyId(companyId);
        RequestResponse response = new RequestResponse(null, false, HttpStatus.NOT_FOUND.value(), "Tenant not found");

        if(tenant != null){
            response.setModel(tenant);
            response.setSuccess(true);
            response.setHttpStatus(HttpStatus.OK.value());
            response.setMessage("Tenant found");
        }

        return response;
    }

    @PostMapping
    RequestResponse registerTenant(@RequestBody TenantRegisterContainer tenantRegisterContainer){
        return tenantService.registerTenant(tenantRegisterContainer);
    }

}
