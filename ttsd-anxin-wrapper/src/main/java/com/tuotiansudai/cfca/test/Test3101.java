package com.tuotiansudai.cfca.test;

import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.ProxySignVO;
import cfca.trustsign.common.vo.request.tx3.Tx3101ReqVO;
import com.tuotiansudai.cfca.connector.AnxinClient;
import com.tuotiansudai.cfca.constant.TxCode;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import org.joda.time.DateTime;

public class Test3101 {
    public static void main(String[] args) throws Exception {
        AnxinClient anxinClient = AnxinClient.getClient();

        Tx3101ReqVO tx3101ReqVO = new Tx3101ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(DateTime.now().toString("yyyyMMddHHmmss"));

        ProxySignVO proxySignVO = new ProxySignVO();
        proxySignVO.setUserId("4027A45BC12E29E9E05311016B0AA19C");
        proxySignVO.setProjectCode("003");

        tx3101ReqVO.setHead(head);
        tx3101ReqVO.setProxySign(proxySignVO);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3101ReqVO);
        System.out.println("req:" + req);

        String res = anxinClient.send(TxCode.SEND_CAPTCHA, req);
        System.out.println("res:" + res);
    }
}
