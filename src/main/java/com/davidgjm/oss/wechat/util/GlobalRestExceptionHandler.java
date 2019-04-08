package com.davidgjm.oss.wechat.util;

import com.davidgjm.oss.wechat.model.ApiErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public void defaultExceptionHandler() {
        // Nothing to do
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleConflict() {
        // Nothing to do
    }


    private ApiErrorDetails buildErrorDetails(Exception e) {
        log.warn("Thrown exception type: {}", e.getClass().getSimpleName());
        return ApiErrorDetails.builder()
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(getMessage(e))
                .build();
    }

    private String getMessage(Exception e) {
        String message = e.getMessage();
        if (StringUtils.hasText(message)) return message;

        ResponseStatus status = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
        if (status != null) return status.reason();

        return "";
    }
}
