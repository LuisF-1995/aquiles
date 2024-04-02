package com.lurodev.usersmicroservice.microservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantUserClientDTO {
    private TenantClientDTO tenant;
    private String username;
}
