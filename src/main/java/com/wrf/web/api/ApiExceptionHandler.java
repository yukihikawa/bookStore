package com.wrf.web.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: bookStore
 * @description: 仅作用于 com.wrf.web.api 下 REST 控制器的全局异常处理，
 *               统一返回 {code,message} JSON，避免 SPA 收到 HTML 错误页
 * @author: Rifu Wu
 **/
@RestControllerAdvice(basePackages = "com.wrf.web.api")
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiException(ApiException e) {
        log.warn("api exception: [{}] {}", e.getCode(), e.getMessage());
        Map<String, String> body = new LinkedHashMap<>();
        body.put("code", e.getCode().getCodeValue());
        body.put("message", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(body);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            NoHandlerFoundException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(Exception e) {
        log.warn("bad request: {}", e.getMessage());
        Map<String, String> body = new LinkedHashMap<>();
        body.put("code", ApiCodes.INVALID_PARAM.getCodeValue());
        body.put("message", "请求参数无效");
        return ResponseEntity.status(e instanceof NoHandlerFoundException ? 404 : 400).body(body);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleUnreadable(Exception e) {
        log.warn("unreadable body: {}", e.getMessage());
        Map<String, String> body = new LinkedHashMap<>();
        body.put("code", ApiCodes.INVALID_PARAM.getCodeValue());
        body.put("message", "请求体格式错误");
        return ResponseEntity.status(400).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherException(Exception e) {
        log.error("unexpected api exception", e);
        Map<String, String> body = new LinkedHashMap<>();
        body.put("code", ApiCodes.INTERNAL.getCodeValue());
        body.put("message", "服务器内部错误");
        return ResponseEntity.status(500).body(body);
    }
}

