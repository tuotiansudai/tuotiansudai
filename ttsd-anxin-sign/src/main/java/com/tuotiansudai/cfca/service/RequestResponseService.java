package com.tuotiansudai.cfca.service;

import cfca.trustsign.common.vo.request.tx3.Tx3001ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3101ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3102ReqVO;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3101ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3102ResVO;

public interface RequestResponseService {

    void insertCreateAccountRequest(Tx3001ReqVO tx3001ReqVO);

    void insertCreateAccountResponse(Tx3001ResVO tx3001ResVO);

    void insertSendCaptchaRequest(Tx3101ReqVO tx3101ReqVO);

    void insertSendCaptchaResponse(Tx3101ResVO tx3101ResVO);

    void insertVerifyCaptchaRequest(Tx3102ReqVO tx3102ReqVO);

    void insertVerifyCaptchaResponse(Tx3102ResVO tx3102ResVO);
}
