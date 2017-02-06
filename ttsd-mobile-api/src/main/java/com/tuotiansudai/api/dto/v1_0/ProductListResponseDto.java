package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ProductListResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "我的积分", example = "1000")
    private String myPoints;

    @ApiModelProperty(value = "未完成任务数量", example = "5")
    private long unFinishedTaskCount; //未完成任务数量

    @ApiModelProperty(value = "虚拟商品集合", example = "list")
    private List<ProductDetailResponseDto> virtuals;

    @ApiModelProperty(value = "实物商品集合", example = "list")
    private List<ProductDetailResponseDto> physicals;

    public String getMyPoints() {
        return myPoints;
    }

    public void setMyPoints(String myPoints) {
        this.myPoints = myPoints;
    }

    public long getUnFinishedTaskCount() {
        return unFinishedTaskCount;
    }

    public void setUnFinishedTaskCount(long unFinishedTaskCount) {
        this.unFinishedTaskCount = unFinishedTaskCount;
    }

    public List<ProductDetailResponseDto> getVirtuals() {
        return virtuals;
    }

    public void setVirtuals(List<ProductDetailResponseDto> virtuals) {
        this.virtuals = virtuals;
    }

    public List<ProductDetailResponseDto> getPhysicals() {
        return physicals;
    }

    public void setPhysicals(List<ProductDetailResponseDto> physicals) {
        this.physicals = physicals;
    }

}
