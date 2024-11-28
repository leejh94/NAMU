package com.namu.common.exception;

import com.namu.common.dto.StatusDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusDTO> handleGenericException(HttpServletRequest request, Exception ex) {
        // 정적 파일 요청은 무시
        String requestUri = request.getRequestURI();
        if (requestUri.endsWith(".css") || requestUri.endsWith(".js") || requestUri.endsWith(".gif") ||
                requestUri.endsWith(".png") || requestUri.endsWith(".ttf") || requestUri.endsWith(".woff2") || requestUri.endsWith(".ico")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 정적 파일 요청은 404 응답
        }

        // 일반적인 예외 처리
        StatusDTO status = new StatusDTO();
        status.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // HTTP 500 Internal Server Error
        status.setMessage("서버 내부 오류: " + ex.getMessage());
        status.setData(null);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(status);
    }
}
