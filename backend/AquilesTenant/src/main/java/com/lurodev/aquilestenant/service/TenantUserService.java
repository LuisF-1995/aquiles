package com.lurodev.aquilestenant.service;

import com.lurodev.aquilestenant.model.TenantUser;
import com.lurodev.aquilestenant.repository.ITenantUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantUserService implements ITenantUserService {
    private final ITenantUserRepository userRepository;

    @Override
    public TenantUser getUserById(UUID userId) {
        Optional<TenantUser> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    @Override
    public List<TenantUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public TenantUser registerUser(TenantUser user) {
        return userRepository.save(user);
    }

    @Override
    public TenantUser updateUser(TenantUser user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean deleteUserById(UUID userId) {
        boolean userDeleted = false;
        TenantUser user = this.getUserById(userId);

        if(user != null){
            userRepository.deleteById(userId);
            userDeleted = true;
        }

        return userDeleted;
    }
}
