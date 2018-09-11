package com.tuotiansudai.cfca.test;

import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3211ReqVO;
import com.tuotiansudai.cfca.connector.AnxinClient;
import com.tuotiansudai.cfca.constant.TxCode;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;

public class Test3211 {
    public static void main(String[] args) throws Exception {
        AnxinClient anxinClient = AnxinClient.getClient();

        Tx3211ReqVO tx3211ReqVO = new Tx3211ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20160102235959");

        tx3211ReqVO.setHead(head);
        tx3211ReqVO.setBatchNo("B141");

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3211ReqVO);
        System.out.println("req:" + req);

        String res = anxinClient.send(TxCode.QUERY_CONTRACT_BATCH, req);
        System.out.println("res:" + res);
    }
}
