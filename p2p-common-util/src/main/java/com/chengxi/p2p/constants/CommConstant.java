package com.chengxi.p2p.constants;

import com.chengxi.p2p.utils.ResponseCode;
/**
 * @author CHENGXI
 * @date 2019/8/3
 */
public class CommConstant {

    public static final ResponseCode SUCCESS = new ResponseCode("000000", "数据请求成功");

    public static final ResponseCode FAIL =  new ResponseCode("000001", "数据请求失败");

    public static final String SUCCESS_CODE = "000000";

    public static final String SUCCESS_MESG = "数据请求成功";

    public static final ResponseCode SYSTEM_ERROR = new ResponseCode("000002", "系统异常请联系管理员");


}
