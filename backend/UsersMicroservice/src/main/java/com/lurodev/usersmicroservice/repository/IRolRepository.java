package com.lurodev.usersmicroservice.repository;

import com.lurodev.usersmicroservice.models.Rol;
import com.lurodev.usersmicroservice.models.User;
import com.lurodev.usersmicroservice.models.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Long> {
}
