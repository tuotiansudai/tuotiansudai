package com.tuotiansudai.cfca.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.util.Base64;
import cfca.trustsign.common.vo.cs.GetContractSignatureAttrVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.request.tx3.Tx3204ReqVO;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.util.SecurityUtil;

import java.io.FileInputStream;
import java.io.IOException;

public class Test3204 {
    public static void main(String[] args) throws PKIException, IOException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3204ReqVO tx3204ReqVO = new Tx3204ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20160102235959");

        GetContractSignatureAttrVO getContractSignatureAttr = new GetContractSignatureAttrVO();
        getContractSignatureAttr.setContractNo("ZL20160804000000121");

        SignInfoVO signInfo = new SignInfoVO();
        signInfo.setUserId("30E27C8AD9BBD55FE050007F0100577B");
        signInfo.setLocation("210.74.41.0");
        signInfo.setSignLocation("Signature2");
        signInfo.setSignCert(SecurityUtil.getSignCert("./jks/test.pfx", "11111111"));

        FileInputStream fis = new FileInputStream("./image/赵六.png");
        byte[] imageBytes = new byte[fis.available()];
        fis.read(imageBytes);
        fis.close();

        signInfo.setImageData(Base64.toBase64String(imageBytes));
        getContractSignatureAttr.setSignInfo(signInfo);

        tx3204ReqVO.setHead(head);
        tx3204ReqVO.setGetContractSignatureAttr(getContractSignatureAttr);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3204ReqVO);
        System.out.println("req:" + req);

        String txCode = "3204";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
