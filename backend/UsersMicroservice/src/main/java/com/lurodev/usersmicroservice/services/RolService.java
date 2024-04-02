package com.lurodev.usersmicroservice.services;

import com.lurodev.usersmicroservice.models.Rol;
import com.lurodev.usersmicroservice.models.UserRoles;
import com.lurodev.usersmicroservice.repository.IRolRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolService implements IRolService{
    private final IRolRepository rolRepository;

    @Override
    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Rol getRolById(Long id) {
        Optional<Rol> rolOpt = rolRepository.findById(id);
        Rol rol = null;

        if(rolOpt.isPresent()){
            rol = rolOpt.get();
        }

        return rol;
    }

    @Override
    public Rol createRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public Rol updateRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public Boolean deleteRolById(Long id) {
        boolean deletionSuccess = false;
        Rol rol = this.getRolById(id);

        if(rol != null){
            rolRepository.deleteById(id);
            deletionSuccess = true;
        }

        return deletionSuccess;
    }
}
