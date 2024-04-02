package com.lurodev.usersmicroservice.multitenancy;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TenantHttpProperties {
    @Value("${multitenancy.http.header-name}")
    String headerName;
    public String headerName(){
        return headerName;
    }
}
