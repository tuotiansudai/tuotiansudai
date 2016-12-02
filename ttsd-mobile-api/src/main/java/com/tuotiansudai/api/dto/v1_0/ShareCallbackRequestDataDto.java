package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class ShareCallbackRequestDataDto extends BaseParamDto{

    @ApiModelProperty(value = "分析类型", example = "BANNER")
    private ShareType shareType;

    @ApiModelProperty(value = "objectId", example = "1")
    private String objectId;

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
