package com.kplian.msaccess.domain.exception;

public class BusinessException extends RuntimeException {
    private final String messageKey;
    private final String code;
    private final Object[] params;

    public BusinessException(String messageKey, String code, Object... params) {
        super(messageKey);
        this.messageKey = messageKey;
        this.code = code;
        this.params = params == null ? new Object[0] : params;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getCode() {
        return code;
    }

    public Object[] getParams() {
        return params;
    }
}
