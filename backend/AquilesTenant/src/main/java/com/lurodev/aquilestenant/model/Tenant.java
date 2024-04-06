package com.lurodev.aquilestenant.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tenants")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(unique = true, nullable = false)
    private String companyName;

    @Setter
    @Column(unique = true, nullable = false)
    private String companyId; // Es el mismo NIT en colombia

    @Setter
    @Column(nullable = false)
    private String country;

    @Setter
    @Column(nullable = false)
    private Boolean isolated;

    @Setter
    @Column(nullable = false)
    private Date registrationDate = new Date(System.currentTimeMillis());

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationPlans plan;

    @Setter
    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"tenant"})
    private Set<TenantUser> users = new HashSet<>();

    @Setter
    @Column
    private Integer tenantPort;
}
