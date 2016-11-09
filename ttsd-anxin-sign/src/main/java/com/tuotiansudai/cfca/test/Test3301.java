package com.tuotiansudai.cfca.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CertBindingVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3301ReqVO;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.util.SecurityUtil;

public class Test3301 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3301ReqVO tx3301ReqVO = new Tx3301ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20160102235959");

        tx3301ReqVO.setHead(head);
        CertBindingVO certBindingVO = new CertBindingVO();
        String userId = "AE5F7586B154481386E109BB2C4CDF55";
        certBindingVO.setUserId(userId);
        certBindingVO.setSignCert(SecurityUtil.getSignCert("./jks/test.pfx", "11111111"));

        tx3301ReqVO.setCertBinding(certBindingVO);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3301ReqVO);
        System.out.println("req:" + req);

        String txCode = "3301";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
