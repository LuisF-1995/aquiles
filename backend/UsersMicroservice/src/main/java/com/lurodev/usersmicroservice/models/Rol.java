package com.lurodev.usersmicroservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"roles", "name", "lastName", "dni", "email", "phone", "regionalId", "professionalCardNumber", "enabled", "password", "isActive", "sign", "authorities", "tenantId", "competences"})
    private User user;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoles rol;

    @Setter
    @NonNull
    @Column(nullable = false)
    private String tenantId;
}
