package com.lurodev.usersmicroservice.multitenancy.resolver;

import com.lurodev.usersmicroservice.multitenancy.TenantHttpProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpHeaderTenantResolver implements TenantResolver<HttpServletRequest>{
    private final TenantHttpProperties tenantHttpProperties;

    @Override
    public String resolveTenantIdentifier(HttpServletRequest request) {
        return request.getHeader(tenantHttpProperties.headerName());
    }
}
