package com.tuotiansudai.point.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class GoodsActiveDto implements Serializable {
    private String loginName;
    @NotNull(message = "商品Id不能为空")
    private Long productId;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
