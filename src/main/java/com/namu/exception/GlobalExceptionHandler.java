package com.namu.exception;

import com.namu.dto.StatusDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public StatusDTO handleGenericException(Exception ex) {
        // 일반적인 예외 처리
        StatusDTO status = new StatusDTO();
        status.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // HTTP 500 Internal Server Error
        status.setMessage("서버 내부 오류: " + ex.getMessage());
        status.setData(null);
        return status;
    }
}
