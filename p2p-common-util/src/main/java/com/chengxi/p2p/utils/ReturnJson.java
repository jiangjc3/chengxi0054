package com.chengxi.p2p.utils;


import com.chengxi.p2p.constants.CommConstant;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.io.Serializable;
/**
 * @author CHENGXI
 * @date 2019/8/3
 */
public class ReturnJson<T> implements Serializable {
    /**
     * 信息码
     */
    private final String resultCode;

    /**
     * 信息详情
     */
    private final String resultMessage;

    /**
     * 信息对象
     */
    private final T data;

    public ReturnJson(String resultCode, String resultMessage, T data) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = data;
    }
    public ReturnJson() {
        this.resultCode = CommConstant.SUCCESS_CODE;
        this.resultMessage = CommConstant.SUCCESS_MESG;
        this.data = null;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public T getData() {
        return data;
    }

    public static ReturnJson json(ResponseCode responseCode, Object data) {
        return new ReturnJson(responseCode.getCode(), responseCode.getMesg(), data);
    }
    public static ReturnJson json(ResponseCode responseCode) {
        return new ReturnJson(responseCode.getCode(), responseCode.getMesg(), null);
    }

    public static ReturnJson json(String resultCode, String resultMessage, Object data) {
        return new ReturnJson(resultCode, resultMessage, data);
    }

    /**
     * 返回成功JSON串
     * @param data
     * @return
     */
    public static ReturnJson success(Object data) {
        if (data instanceof ResponseCode) {
            return json((ResponseCode) data);
        }
        return ReturnJson.json(CommConstant.SUCCESS, data);
    }

    public static ReturnJson success() {
        return ReturnJson.success(null);
    }

    public static ReturnJson fail(Object data) {
        if (data instanceof ResponseCode) {
            return json((ResponseCode) data);
        }
        return ReturnJson.json(CommConstant.FAIL, data);
    }

    public static ReturnJson fail(Exception exception) {
        if (exception == null ) {
            return ReturnJson.fail(null);
        }
        CommException commException = new CommException();
        commException.setExceptionName(exception.getClass().getSimpleName());
        commException.setErrorMessage(exception.getMessage());
        commException.setStackTrace(ExceptionUtils.getStackTrace(exception));
        return ReturnJson.json(CommConstant.FAIL,commException);
    }


}
