package com.tuotiansudai.cfca.test;

import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.PersonVO;
import cfca.trustsign.common.vo.request.tx3.Tx3001ReqVO;
import com.tuotiansudai.cfca.connector.AnxinClient;
import com.tuotiansudai.cfca.constant.TxCode;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import org.joda.time.DateTime;

public class Test3001 {
    public static void main(String[] args) throws Exception {
        AnxinClient anxinClient = AnxinClient.getClient();

        Tx3001ReqVO tx3001ReqVO = new Tx3001ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(DateTime.now().toString("yyyyMMddHHmmss"));

        PersonVO person = new PersonVO();
        person.setPersonName("周宝鑫");
        person.setIdentTypeCode("0");
        person.setIdentNo("34112719840523021X");
        person.setMobilePhone("18611445119");
        person.setEmail("");
        person.setAddress("北京");
        person.setAuthenticationMode("公安部");

        tx3001ReqVO.setHead(head);
        tx3001ReqVO.setPerson(person);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3001ReqVO);
        System.out.println("req:" + req);

        String res = anxinClient.send(TxCode.CREATE_ACCOUNT, req);
        System.out.println("res:" + res);
    }
}
