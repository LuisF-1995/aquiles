package com.lurodev.aquilestenant.controller;

import com.lurodev.aquilestenant.dto.RequestResponse;
import com.lurodev.aquilestenant.dto.TenantRegisterContainer;
import com.lurodev.aquilestenant.model.Tenant;
import com.lurodev.aquilestenant.service.ITenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tenant")
@RequiredArgsConstructor
public class TenantController {
    private final ITenantService tenantService;

    @GetMapping
    List<Tenant> getAllTenants(){
        return tenantService.getAllTenants();
    }

    @PostMapping
    RequestResponse registerTenant(@RequestBody TenantRegisterContainer tenantRegisterContainer){
        return tenantService.registerTenant(tenantRegisterContainer);
    }

}
