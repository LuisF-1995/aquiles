package com.lurodev.aquilestenant.microservice.dto;

import lombok.*;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String lastname;
    private String dni; // Es la cedula en Colombia o el numero de documento de identificacion
    private String email;
    private String password;
    private Boolean isActive;
    private String tenantId;
    private String phone;
    private Short regionalId;
    private Set<String> roles = new HashSet<>();
}
