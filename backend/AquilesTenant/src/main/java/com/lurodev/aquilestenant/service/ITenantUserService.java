package com.lurodev.aquilestenant.service;

import com.lurodev.aquilestenant.dto.RequestResponse;
import com.lurodev.aquilestenant.model.TenantUser;

import java.util.List;
import java.util.UUID;

public interface ITenantUserService {
    TenantUser getUserById(UUID userId);
    List<TenantUser> getUserByUsername(String username);
    TenantUser registerUser(TenantUser user);
    TenantUser updateUser(TenantUser user);
    Boolean deleteUserById(UUID userId);
}
