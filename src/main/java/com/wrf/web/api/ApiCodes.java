package com.wrf.web.api;

/**
 * @program: bookStore
 * @description: SPA 接口统一错误码与默认状态码
 * @author: Rifu Wu
 **/
public enum ApiCodes {
    UNKNOWN("000000", 500),
    NOT_LOGIN("10001", 401),
    WRONG_CREDENTIALS("10002", 400),
    USER_EXISTS("10003", 400),
    FORBIDDEN("10004", 403),
    NOT_FOUND("20001", 404),
    CART_EMPTY("20002", 400),
    INVALID_PARAM("20003", 400),
    INTERNAL("50000", 500);

    private final String codeValue;
    private final int status;

    ApiCodes(String codeValue, int status) {
        this.codeValue = codeValue;
        this.status = status;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public int getStatus() {
        return status;
    }
}
