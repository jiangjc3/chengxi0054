package com.chengxi.p2p.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
/**
 * @author CHENGXI
 * @date 2019/8/3
 */
public class CommException implements Serializable{
    private static final int MAX_STACK_TRACE_LEN = 2000;

    private String exceptionName;

    private String errorMessage;

    private String stackTrace;

    public CommException(String exceptionName, String errorMessage, String stackTrace) {
        this.exceptionName = exceptionName;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    public CommException() {
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        if (StringUtils.length(stackTrace) > MAX_STACK_TRACE_LEN) {
            stackTrace = StringUtils.substring(stackTrace, 0, MAX_STACK_TRACE_LEN);
        }
        this.stackTrace = stackTrace;
    }
}
