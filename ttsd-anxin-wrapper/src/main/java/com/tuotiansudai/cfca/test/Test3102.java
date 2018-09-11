package com.tuotiansudai.cfca.test;

import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.ProxySignVO;
import cfca.trustsign.common.vo.request.tx3.Tx3102ReqVO;
import com.tuotiansudai.cfca.connector.AnxinClient;
import com.tuotiansudai.cfca.constant.TxCode;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import org.joda.time.DateTime;

public class Test3102 {
    public static void main(String[] args) throws Exception {
        AnxinClient anxinClient = AnxinClient.getClient();

        Tx3102ReqVO tx3102ReqVO = new Tx3102ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(DateTime.now().toString("yyyyMMddHHmmss"));

        ProxySignVO proxySignVO = new ProxySignVO();
        proxySignVO.setUserId("7383BF7E05445019E05311016B0AAE7A");
        proxySignVO.setProjectCode("222222");
        proxySignVO.setCheckCode("788771");

        tx3102ReqVO.setHead(head);
        tx3102ReqVO.setProxySign(proxySignVO);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3102ReqVO);
        System.out.println("req:" + req);

        String res = anxinClient.send(TxCode.VERIFY_CAPTCHA, req);
        System.out.println("res:" + res);
    }
}
