package com.tuotiansudai.cfca.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3211ReqVO;
import com.tuotiansudai.cfca.connector.HttpConnectorTest;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.util.SecurityUtil;

public class Test3211 {
    public static void main(String[] args) throws PKIException {
        HttpConnectorTest httpConnector = new HttpConnectorTest();
        httpConnector.init();

        Tx3211ReqVO tx3211ReqVO = new Tx3211ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20160102235959");

        tx3211ReqVO.setHead(head);
        tx3211ReqVO.setBatchNo("a1d58b4bd234458fb7c2c1da1f0cee8f");

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3211ReqVO);
        System.out.println("req:" + req);

        String txCode = "3211";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnectorTest.JKS_PATH, HttpConnectorTest.JKS_PWD, HttpConnectorTest.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
