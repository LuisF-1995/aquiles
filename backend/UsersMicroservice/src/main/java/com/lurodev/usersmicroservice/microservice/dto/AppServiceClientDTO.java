package com.lurodev.usersmicroservice.microservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppServiceClientDTO {
    private Integer id;
    private String serviceName;
    private String dbAdmin;
    private String companyId;
    private Boolean isDefault;
}
