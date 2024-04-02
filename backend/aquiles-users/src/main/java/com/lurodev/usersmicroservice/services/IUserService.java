package com.lurodev.usersmicroservice.services;

import com.lurodev.usersmicroservice.microservice.dto.TenantUserClientDTO;
import com.lurodev.usersmicroservice.models.dto.RequestResponse;
import com.lurodev.usersmicroservice.models.dto.UserDTO;
import com.lurodev.usersmicroservice.models.User;
import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();
    List<UserDTO> getActiveUsers(Boolean isActive);
    List<UserDTO> getUsersByRegionalId(Short regionalId);
    UserDTO getUserById(Long id);
    UserDTO getUserByName(String name);
    UserDTO getUserByDni(String dni);
    UserDTO getUserByEmail(String email);
    List<TenantUserClientDTO> getTenantUserByUsername(String username);
    RequestResponse registerUser(User user);
    RequestResponse updateUser(User user);
    RequestResponse deleteUserById(Long userId);
}
