package com.tuotiansudai.api.dto;

public class ShareCallbackRequestDataDto extends BaseParamDto{

    private ShareType shareType;

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
