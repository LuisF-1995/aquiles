package com.lurodev.usersmicroservice.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lurodev.usersmicroservice.models.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String lastName;
    private String dni; // Es la cedula en Colombia o el numero de documento de identificacion
    private String email;
    private String tenantId;
    private String phone;
    private Short regionalId;
    @JsonIgnoreProperties({"user", "tenantId"})
    private Set<Rol> roles = new HashSet<>();
}
