package com.lurodev.usersmicroservice.controllers;

import com.lurodev.usersmicroservice.models.dto.RequestResponse;
import com.lurodev.usersmicroservice.models.dto.UserLogin;
import com.lurodev.usersmicroservice.services.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<RequestResponse> authenticate(@RequestBody UserLogin authUser){
        return ResponseEntity.ok(authService.authenticateUser(authUser));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam("tenantId") @NonNull String tenantId, @RequestParam("token") @NonNull String token, @RequestParam("userId") @NonNull Long userId){
        return ResponseEntity.ok(authService.validateToken(tenantId, token, userId));
    }
}
