package com.lurodev.aquilestenant.dto;

import com.lurodev.aquilestenant.microservice.dto.User;
import com.lurodev.aquilestenant.model.Tenant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TenantRegisterContainer {
    private Tenant tenant;
    private User owner;
}
