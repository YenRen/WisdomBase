package com.hnxx.wisdombase.framework.retrofit.exception;

public class ApiException extends Exception {
    private String code;
    private String displayMessage;

    public ApiException(Throwable throwable, String code) {
        super(throwable);
        this.code = code;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public String getCode() {
        return code;
    }

    /**
     * 是否是网络错误
     * @return
     */
    public boolean isNetException() {
        return ReqErr.HTTP_ERROR.equals(code) || ReqErr.NETWORK_ERROR.equals(code);
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "code='" + code + '\'' +
                ", displayMessage='" + displayMessage + '\'' +
                '}';
    }
}
