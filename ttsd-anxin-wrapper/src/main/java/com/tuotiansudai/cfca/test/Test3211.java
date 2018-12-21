package com.tuotiansudai.cfca.test;

import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3211ReqVO;
import com.tuotiansudai.cfca.connector.AnxinClientTest;
import com.tuotiansudai.cfca.constant.TxCode;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;

public class Test3211 {
    public static void main(String[] args) throws Exception {
        AnxinClientTest anxinClient = new AnxinClientTest();
        anxinClient.initSSL();

        Tx3211ReqVO tx3211ReqVO = new Tx3211ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20181221235959");

        tx3211ReqVO.setHead(head);
        tx3211ReqVO.setBatchNo("B2018122102");

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3211ReqVO);
        System.out.println("req:" + req);

        String res = anxinClient.send(TxCode.QUERY_CONTRACT_BATCH, req);
        System.out.println("res:" + res);
    }
}
