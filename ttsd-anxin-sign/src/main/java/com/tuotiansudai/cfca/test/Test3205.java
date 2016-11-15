package com.tuotiansudai.cfca.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.util.Base64;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignContractSignatureAttrVO;
import cfca.trustsign.common.vo.request.tx3.Tx3205ReqVO;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.util.SecurityUtil;

public class Test3205 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3205ReqVO tx3205ReqVO = new Tx3205ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20160102235959");

        SignContractSignatureAttrVO signContractSignatureAttr = new SignContractSignatureAttrVO();
        signContractSignatureAttr.setContractNo("ZL20160816000000002");

        String signatureAttr = "MWkwGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMTYwODE2MTAzNTMyWjAvBgkqhkiG9w0BCQQxIgQg8CtcU/grAhg8tP2Lr1pw60sY/9nYCUJEO5+u9u6ky3Q=";
        String signatureValue = SecurityUtil.p1SignMessage("./jks/test.pfx", "11111111", Base64.decode(signatureAttr));
        signContractSignatureAttr.setSignatureOfAttr(signatureValue);

        tx3205ReqVO.setHead(head);
        tx3205ReqVO.setSignContractSignatureAttr(signContractSignatureAttr);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3205ReqVO);
        System.out.println("req:" + req);

        String txCode = "3205";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
