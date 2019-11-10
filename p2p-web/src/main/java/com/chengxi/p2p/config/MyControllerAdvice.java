package com.chengxi.p2p.config;

import com.chengxi.p2p.utils.ReturnJson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author CHENGXI
 * @date 2019/10/4
 */
@ControllerAdvice
public class MyControllerAdvice {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(Exception ex) {
        System.out.println("发生了异常" + ex);
        //发生异常进行日志记录，写入数据库或者其他处理，此处省略
        return ReturnJson.fail(ex);
    }
}
