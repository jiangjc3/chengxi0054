package com.chengxi.p2p.model.vo;

import java.io.Serializable;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
public class ResultObject implements Serializable {

    /**
     * 错误码
     */
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
