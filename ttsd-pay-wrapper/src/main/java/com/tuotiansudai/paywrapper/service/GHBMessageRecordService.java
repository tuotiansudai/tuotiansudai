package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.paywrapper.ghb.message.request.RequestBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.request.RequestMessageContent;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseMessageContent;

public interface GHBMessageRecordService {

    void autoCreateMessageRecordTables();

    <T extends RequestBaseOGW> void saveRequestMessage(RequestMessageContent<T> data);

    <T extends ResponseBaseOGW> void saveResponseMessage(ResponseMessageContent<T> data, String originXmlpara, String fullMessage);
}
