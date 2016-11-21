package com.tuotiansudai.cfca.service;

import cfca.trustsign.common.vo.request.tx3.*;
import cfca.trustsign.common.vo.response.tx3.*;

public interface RequestResponseService {

    void insertCreateAccountRequest(Tx3001ReqVO tx3001ReqVO);

    void insertCreateAccountResponse(Tx3001ResVO tx3001ResVO);

    void insertSendCaptchaRequest(Tx3101ReqVO tx3101ReqVO);

    void insertSendCaptchaResponse(Tx3101ResVO tx3101ResVO);

    void insertVerifyCaptchaRequest(Tx3102ReqVO tx3102ReqVO);

    void insertVerifyCaptchaResponse(Tx3102ResVO tx3102ResVO);

    void insertBatchGenerateContractRequest(long businessId, Tx3202ReqVO tx3202ReqVO);

    void insertBatchGenerateContractResponse(long businessId, String batchNo, Tx3202ResVO tx3202ResVO);

    void insertBatchQueryContractRequest(long businessId, Tx3211ReqVO tx3211ReqVO);

    void insertBatchQueryContractResponse(long businessId, String batchNo, Tx3211ResVO tx3211ResVO);
}
