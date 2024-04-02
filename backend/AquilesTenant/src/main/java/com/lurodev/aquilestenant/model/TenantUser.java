package com.lurodev.aquilestenant.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tenant_users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "tenant_id", referencedColumnName="id", nullable = false)
    @JsonIgnoreProperties({"users"})
    private Tenant tenant;

    @Setter
    @Column
    private String username;
}
