package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.InvestStatus;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import java.util.List;

public class PromotionRecordResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "图片地址", example = "upload/image/a.png")
    private String imgUrl;

    @ApiModelProperty(value = "链接地址", example = "http://www.tuotiansudai.com")
    private String linkUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
