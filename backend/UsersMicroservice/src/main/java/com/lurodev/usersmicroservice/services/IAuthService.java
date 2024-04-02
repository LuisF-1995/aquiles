package com.lurodev.usersmicroservice.services;

import com.lurodev.usersmicroservice.models.dto.RequestResponse;
import com.lurodev.usersmicroservice.models.dto.UserLogin;
import org.springframework.lang.NonNull;

import java.util.UUID;

public interface IAuthService {
    RequestResponse authenticateUser(UserLogin userLogin);
    Boolean validateToken(@NonNull String tenantId, String token, Long userId);
    Boolean sameTenantId(Object object, Short tenantId);
}
