package com.davidgjm.oss.wechat.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
@Data
public class ApiErrorDetails {
    private Integer errorCode;
    private String error;
    private String message;

    public static ApiErrorDetails from(@NotNull Exception e) {
        return ApiErrorDetails.builder()
                .message(e.getMessage())
                .build()
                ;
    }

    public static ApiErrorDetails from(@NotNull HttpStatus status, @NotNull Exception e) {
        return ApiErrorDetails.builder()
                .errorCode(status.value())
                .error(status.getReasonPhrase())
                .message(e.getMessage())
                .build()
                ;
    }
}
