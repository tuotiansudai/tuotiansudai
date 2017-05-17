package com.tuotiansudai.mq.exception;

import com.aliyun.mns.common.ClientException;

public class AliyunClientException extends ClientException {
    public AliyunClientException(ClientException e) {
        super(e.getErrorCode(), e.getMessage(), e.getRequestId(), e.getCause());
    }

    @Override
    public String toString() {
        return "code:" + getErrorCode() + ", "
                + "message:" + getMessage() + ", "
                + "requestId: " + getRequestId();
    }
}
