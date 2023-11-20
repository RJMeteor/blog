package com.personal.blog.config;


import com.personal.blog.exceptions.GrantException;
import com.personal.blog.exceptions.HeaderException;
import com.personal.blog.utils.Metadata;
import com.personal.blog.utils.R;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler({HeaderException.class})
    public ResponseEntity<R> headerException(HeaderException e) {
        R r = new R(Metadata.HeadException.getCode(), Metadata.HeadException.getMessage());
        return new ResponseEntity<R>(r, HttpStatus.OK);
    }

    @ExceptionHandler({SignatureException.class})
    public ResponseEntity<R> signatureException(SignatureException e) {
        R r = new R(Metadata.SignatureException.getCode(), Metadata.SignatureException.getMessage());
        return new ResponseEntity<R>(r, HttpStatus.OK);
    }

    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<R> malformedJwtException(MalformedJwtException e) {
        R r = new R(Metadata.MalformedJwtException.getCode(), Metadata.MalformedJwtException.getMessage());
        return new ResponseEntity<R>(r, HttpStatus.OK);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<R> tamperTokenException(ExpiredJwtException e) {
        R r = new R(Metadata.ExpiredJwtException.getCode(), Metadata.ExpiredJwtException.getMessage());
        return new ResponseEntity<R>(r, HttpStatus.OK);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<R> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<String> list = new ArrayList<>();
        result.getFieldErrors().forEach(error -> {
            String msg = error.getDefaultMessage();
            list.add(msg);
        });
        R<List<String>> r = new R(Metadata.MethodArgumentNotValidException.getCode(),list);
        return new ResponseEntity<R>(r, HttpStatus.OK);
    }

    @ExceptionHandler({LoginException.class})
    public ResponseEntity<R> loginException(LoginException e){
        R r = new R(Metadata.LogingException.getCode(), Metadata.LogingException.getMessage());
        return new ResponseEntity<R>(r, HttpStatus.OK);
    }


    @ExceptionHandler({GrantException.class})
    public ResponseEntity<R> grantException(GrantException e){
        R r = new R(Metadata.GrantException.getCode(), Metadata.GrantException.getMessage());
        return new ResponseEntity<R>(r, HttpStatus.OK);
    }

}
