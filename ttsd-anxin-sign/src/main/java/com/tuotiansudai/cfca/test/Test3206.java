package com.tuotiansudai.cfca.test;

import java.io.FileInputStream;
import java.io.IOException;

import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.util.Base64;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignContractVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.request.tx3.Tx3206ReqVO;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.util.SecurityUtil;

public class Test3206 {
    public static void main(String[] args) throws PKIException, IOException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3206ReqVO tx3206ReqVO = new Tx3206ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20160102235959");

        SignContractVO signContract = new SignContractVO();
        signContract.setContractNo("ZL20160826000000111");
        SignInfoVO signInfo = new SignInfoVO();
        signInfo.setUserId("39126292503E6231E050007F01005DB1");
        signInfo.setLocation("211.94.108.226");
        signInfo.setProjectCode("003");
        signInfo.setSignLocation("Signature2");
        signInfo.setAuthorizationTime("20160801095509");

        FileInputStream fis = new FileInputStream("./image/赵六.png");
        byte[] imageBytes = new byte[fis.available()];
        fis.read(imageBytes);
        fis.close();
        signInfo.setImageData(Base64.toBase64String(imageBytes));
        signContract.setSignInfo(signInfo);

        tx3206ReqVO.setHead(head);
        tx3206ReqVO.setSignContract(signContract);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3206ReqVO);
        System.out.println("req:" + req);

        String txCode = "3206";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
