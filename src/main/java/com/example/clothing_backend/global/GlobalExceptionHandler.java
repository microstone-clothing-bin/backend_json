package com.example.clothing_backend.global;

import com.example.clothing_backend.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice // 전역 예외 처리기 (모든 Controller에서 발생하는 예외를 여기서 처리)
public class GlobalExceptionHandler {

    // 1. UserNotFoundException 처리
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        log.warn("요청한 사용자를 찾을 수 없습니다: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 Not Found
                .body(Map.of(
                        "status", "error",
                        "message", ex.getMessage()
                ));
    }

    // 2. DB 무결성 제약 위반 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("데이터베이스 무결성 제약 조건 위반: {}", ex.getMessage());

        // 기본 메시지
        String message = "데이터베이스 오류가 발생했습니다.";

        // 예외 메시지 분석 → 좀 더 사용자 친화적인 에러 메시지 반환
        if (ex.getMessage().contains("Duplicate entry")) {
            message = "이미 사용 중인 아이디, 이메일 또는 닉네임입니다.";
        } else if (ex.getMessage().contains("foreign key constraint fails")) {
            message = "존재하지 않는 사용자 ID 또는 게시물 ID를 참조했습니다.";
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(Map.of(
                        "status", "error",
                        "message", message
                ));
    }

    // 3. 그 외 RuntimeException 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleGenericRuntimeException(RuntimeException ex) {
        log.error("예상치 못한 런타임 예외 발생!", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(Map.of(
                        "status", "error",
                        "message", "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요."
                ));
    }
}