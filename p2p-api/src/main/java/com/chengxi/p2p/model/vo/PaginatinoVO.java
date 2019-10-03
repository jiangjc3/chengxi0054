package com.chengxi.p2p.model.vo;

import java.io.Serializable;
import java.util.List;
/**
 * @author CHENGXI
 * @date 2019/8/3
 */
public class PaginatinoVO<T> implements Serializable {

    /**
     * 总记录条数
     */
    private Long total;

    /**
     * 显示数据
     */
    private List<T> dataList;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
