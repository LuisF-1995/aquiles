package com.lurodev.usersmicroservice.multitenancy.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;


public final class TenantContext {
    private static final Logger log = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<String> tenantIdentifier = new ThreadLocal<>();

    public static void setTenantId(String tenant){
        log.debug("Setting current tenant to {}", tenant);
        tenantIdentifier.set(tenant);
    }

    @Nullable
    public static String getTenantId(){
        return tenantIdentifier.get();
    }

    public static void clear(){
        tenantIdentifier.remove();
    }
}
