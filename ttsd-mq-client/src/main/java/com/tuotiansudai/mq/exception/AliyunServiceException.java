package com.tuotiansudai.mq.exception;

import com.aliyun.mns.common.ServiceException;

public class AliyunServiceException extends ServiceException {
    public AliyunServiceException(ServiceException e) {
        super(e.getMessage(), e.getCause(), e.getErrorCode(), e.getRequestId(), e.getHostId());
    }

    @Override
    public String toString() {
        return "code:" + getErrorCode() + ", "
                + "message:" + getMessage() + ", "
                + "requestId: " + getRequestId();
    }
}
