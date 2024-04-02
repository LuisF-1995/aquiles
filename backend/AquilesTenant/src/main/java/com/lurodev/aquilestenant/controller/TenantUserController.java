package com.lurodev.aquilestenant.controller;

import com.lurodev.aquilestenant.model.TenantUser;
import com.lurodev.aquilestenant.service.ITenantUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class TenantUserController {
    private final ITenantUserService userService;

    @GetMapping("params")
    List<TenantUser> getUserByParams(@RequestParam("username") @NonNull String username){
        return userService.getUserByUsername(username);
    }

    @PostMapping
    TenantUser registerUser(@RequestBody TenantUser user){
        return userService.registerUser(user);
    }

    @DeleteMapping("/{id}")
    Boolean deleteUserById(@PathVariable UUID id){
        return userService.deleteUserById(id);
    }
}
