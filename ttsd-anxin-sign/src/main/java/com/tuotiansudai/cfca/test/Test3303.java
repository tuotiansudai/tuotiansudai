package com.tuotiansudai.cfca.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CertApplyVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3303ReqVO;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.util.SecurityUtil;

public class Test3303 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3303ReqVO tx3303ReqVO = new Tx3303ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20160102235959");

        tx3303ReqVO.setHead(head);
        CertApplyVO certApply = new CertApplyVO();
        String userId = "39126292503E6231E050007F01005DB1";
        certApply.setUserId(userId);
        certApply.setP10(SecurityUtil.getP10(SecurityUtil.getP10Request(), 2048));

        tx3303ReqVO.setCertApply(certApply);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3303ReqVO);
        System.out.println("req:" + req);

        String txCode = "3303";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
