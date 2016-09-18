package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class ProductListResponseDto extends BaseResponseDataDto {

    private String myPoints;

    private List<ProductDetailResponseDto> virtuals;

    private List<ProductDetailResponseDto> physicals;

    public String getMyPoints() {
        return myPoints;
    }

    public void setMyPoints(String myPoints) {
        this.myPoints = myPoints;
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
