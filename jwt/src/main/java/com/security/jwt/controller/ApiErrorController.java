package com.security.jwt.controller;
import com.security.jwt.domain.response.Response;
import com.security.jwt.exception.DuplicateUsernameException;
import com.security.jwt.exception.NotFoundRefreshTokenException;
import com.security.jwt.exception.NotMatchedRefreshTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class ApiErrorController {

    /**
     * Valid 에러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validate(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        Response response = Response.builder()
                .result("fail")
                .status(400)
//                .message(builder.toString())
                .message(fieldErrors.get(0).getDefaultMessage())    //첫번째 에러만
                .build();
        return ResponseEntity.badRequest().body(response);
    }



    /**
     * 유저 찾기 실패
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity notFoundStore(UsernameNotFoundException e) {
        log.error(e.getMessage());
        Response response = Response.builder()
                .result("fail")
                .status(404)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    /**
     * 아이디 중복 에러
     */
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity duplicatedUsername(DuplicateUsernameException e) {
        log.error(e.getMessage());
        Response response = Response.builder()
                .result("fail")
                .status(400)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * RefreshToken 못찾음
     */
    @ExceptionHandler(NotFoundRefreshTokenException.class)
    public ResponseEntity notFoundRefreshToken(NotFoundRefreshTokenException e) {
        log.error(e.getMessage());
        Response response = Response.builder()
                .result("fail")
                .status(404)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * RefreshToken(Client), RefreshToken(DB) 비교 오류
     */
    @ExceptionHandler(NotMatchedRefreshTokenException.class)
    public ResponseEntity notMatchedRefreshToken(NotMatchedRefreshTokenException e) {
        log.error(e.getMessage());
        Response response = Response.builder()
                .result("fail")
                .status(400)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
   
}
