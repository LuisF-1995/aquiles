package com.lurodev.usersmicroservice.models.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponse {
    private Object model;
    @NonNull
    private Boolean success;
    @NonNull
    private Integer httpStatus;
    private String message;
}
