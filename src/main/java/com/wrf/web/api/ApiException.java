package com.wrf.web.api;

/**
 * @program: bookStore
 * @description: SPA 接口的业务异常，由全局处理器转换为统一的 JSON 错误
 * @author: Rifu Wu
 **/
public class ApiException extends RuntimeException {

    /** 默认 HTTP 状态码（由各 Controller 决定，通常取 ApiCodes 默认值） */
    public static final int DEFAULT_STATUS = 400;

    private final ApiCodes code;

    public ApiException(ApiCodes code) {
        super(code.name());
        this.code = code;
    }

    public ApiException(ApiCodes code, String message) {
        super(message);
        this.code = code;
    }

    public ApiCodes getCode() {
        return code;
    }

    public int getStatus() {
        return code.getStatus();
    }
}
