package com.githubreader.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T>{
    private String status;
    private Integer statusCode;
    private String message;
    private Boolean error;
    private T data;

    public ApiResponse(String status, Integer statusCode, String message, Boolean error, T data) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
        this.data = data;
    }

}
