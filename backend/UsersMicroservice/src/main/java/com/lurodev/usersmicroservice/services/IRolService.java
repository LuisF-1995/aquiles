package com.lurodev.usersmicroservice.services;

import com.lurodev.usersmicroservice.models.Rol;
import com.lurodev.usersmicroservice.models.UserRoles;
import org.springframework.lang.NonNull;

import java.util.List;

public interface IRolService {
    List<Rol> getAllRoles();
    Rol getRolById(Long id);
    Rol createRol(Rol rol);
    Rol updateRol(Rol rol);
    Boolean deleteRolById(Long id);
}
