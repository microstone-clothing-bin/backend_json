package com.example.clothing_backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User Not Found")
// 이 예외가 발생하면 HTTP 상태 코드 404(Not Found)와 함께 "User Not Found" 메시지를 반환
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message); // 부모(RuntimeException)에 메시지 전달
    }
}