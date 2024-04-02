package com.lurodev.usersmicroservice.repository;

import com.lurodev.usersmicroservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByDni(String dni);
    Optional<User> findByEmail(String email);
    List<User> findAllByIsActive(Boolean isActive);
    List<User> findAllByRegionalId(Short regionalId);
}
