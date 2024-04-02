package com.lurodev.aquilestenant.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "services")
public class ApplicationService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private String serviceName;

    @Setter
    private String dbAdmin;

    @Setter
    private String companyId;

    @Setter
    @Column(nullable = false)
    private Boolean isDefault;
}
