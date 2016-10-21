package com.tuotiansudai.point.dto;


import java.io.Serializable;

public class GoodsConsignmentDto implements Serializable {
    private String loginName;
    private Long[] productOrderId;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long[] getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(Long[] productOrderId) {
        this.productOrderId = productOrderId;
    }
}
