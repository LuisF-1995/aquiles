package com.lurodev.usersmicroservice.microservice.client;

import com.lurodev.usersmicroservice.microservice.dto.AppServiceClientDTO;
import com.lurodev.usersmicroservice.microservice.dto.TenantUserClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "aquiles-tenant", url = "localhost:1095/api/v1/aquiles/tenant-service")
public interface TenantClient {
    @GetMapping("/user/params")
    List<TenantUserClientDTO> getUserByParams (@RequestParam("username") @NonNull String username);

    @GetMapping("/services/params")
    AppServiceClientDTO getServiceByParams(@RequestParam("companyId") @NonNull String companyId,
                                           @RequestParam("serviceName") @NonNull String serviceName
    );
}
