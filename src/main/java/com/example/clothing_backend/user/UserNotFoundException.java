package com.example.clothing_backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// [수정] 이 예외가 발생하면 GlobalExceptionHandler가 404 Not Found 상태 코드를 응답합니다.
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User Not Found")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
