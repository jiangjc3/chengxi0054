package com.chengxi.p2p.utils;

import java.io.Serializable;
/**
 * @author CHENGXI
 * @date 2019/8/3
 */
public class ResponseCode implements Serializable {
    private String code;
    private String mesg;

    public ResponseCode(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }

    public String getCode() {
        return code;
    }

    public String getMesg() {
        return mesg;
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                "code='" + code + '\'' +
                ", mesg='" + mesg + '\'' +
                '}';
    }
}
