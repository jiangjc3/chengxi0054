package com.chengxi.p2p.model.vo;

import java.io.Serializable;
/**
 * @author CHENGXI
 * @date 2019/8/3
 */
public class BidUserTop implements Serializable {

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 分数：累计投资金额
     */
    private Double score;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
