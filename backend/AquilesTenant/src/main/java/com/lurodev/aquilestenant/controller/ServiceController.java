package com.lurodev.aquilestenant.controller;

import com.lurodev.aquilestenant.dto.RequestResponse;
import com.lurodev.aquilestenant.model.ApplicationService;
import com.lurodev.aquilestenant.service.ApplicationServiceServ;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ApplicationServiceServ service;

    @GetMapping
    List<ApplicationService> getAllServices(){
        return service.getAllServices();
    }

    @GetMapping("/params")
    ApplicationService getServiceByParams(@RequestParam("companyId") @NonNull String companyId,
                                          @RequestParam("serviceName") @NonNull String serviceName
    ){
        return service.getByCompanyIdAndServiceName(companyId, serviceName);
    }

    @PostMapping
    RequestResponse saveService(@RequestBody ApplicationService appService){
        return service.createService(appService);
    }
}
