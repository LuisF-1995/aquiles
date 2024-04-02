package com.lurodev.usersmicroservice.microservice.dto;

import lombok.*;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantClientDTO {
    private Integer id;
    private String companyName;
    private String companyId; // Es el mismo NIT en colombia
    private String country;
    private Boolean isolated;
    private Date registrationDate;
    private String plan;
    private Boolean isActive;
    //private Set<TenantUserClientDTO> users;
    private Integer tenantPort;
}
