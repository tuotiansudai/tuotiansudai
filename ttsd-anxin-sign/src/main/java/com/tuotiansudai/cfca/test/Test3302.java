package com.tuotiansudai.cfca.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CertUnbindingVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3302ReqVO;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.util.SecurityUtil;

public class Test3302 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3302ReqVO tx3302ReqVO = new Tx3302ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20160102235959");

        tx3302ReqVO.setHead(head);
        CertUnbindingVO certUnbinding = new CertUnbindingVO();
        String userId = "39126292503E6231E050007F01005DB1";
        certUnbinding.setUserId(userId);
        certUnbinding.setSerialNo("2016529117");

        tx3302ReqVO.setCertUnbinding(certUnbinding);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3302ReqVO);
        System.out.println("req:" + req);

        String txCode = "3302";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
