package com.tuotiansudai.cfca.test;

import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.request.tx3.Tx3202ReqVO;
import com.tuotiansudai.cfca.connector.AnxinClient;
import com.tuotiansudai.cfca.constant.TxCode;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.*;

public class Test3202 {
    public static void main(String[] args) throws Exception {
        AnxinClient anxinClient = AnxinClient.getClient();

        Tx3202ReqVO tx3202ReqVO = new Tx3202ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20180102235959");

        List<CreateContractVO> createContractlist = new ArrayList<CreateContractVO>();

        CreateContractVO createContract = new CreateContractVO();
        createContract.setTemplateId("JK_1742");
        createContract.setIsSign(1);

        Map<String, String> fieldMap = new HashMap<String, String>();
        fieldMap.put("investorIdentityNumber", "370786199111050610");
        fieldMap.put("loanerIdentityNumber", "370786199111050610");
        fieldMap.put("loanName", "房产抵押借款17015");
        fieldMap.put("amountUpper", AmountConverter.getRMBStr(900000).replace("元",""));
        fieldMap.put("amount", "9000");
        fieldMap.put("totalRate", "11");
        DateTime endTimeDate = new DateTime(new Date());
        fieldMap.put("endTimeYear", String.valueOf(endTimeDate.getYear()));
        fieldMap.put("endTimeMonth", String.valueOf(endTimeDate.getMonthOfYear()));
        fieldMap.put("endTimeDay", String.valueOf(endTimeDate.getDayOfMonth()));
        DateTime fullTimeDate = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2018-08-03 11:50:55");
        fieldMap.put("recheckTimeYear", String.valueOf(fullTimeDate.getYear()));
        fieldMap.put("recheckTimeMonth", String.valueOf(fullTimeDate.getMonthOfYear()));
        fieldMap.put("recheckTimeDay", String.valueOf(fullTimeDate.getDayOfMonth()));
        fieldMap.put("periods", "12");
        fieldMap.put("loanType", "先付收益后还投资本金，按天计息，放款后生息");
        fieldMap.put("investorName", "高兴费");

        createContract.setInvestmentInfo(fieldMap);

        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId("4074F0BDDC1263F9E05311016B0A0D35");
        agentSignInfo.setAuthorizationTime(new DateTime("2016-11-17").toString("yyyyMMddHHmmss"));
        agentSignInfo.setLocation("118.187.56.162");
        agentSignInfo.setSignLocation("agentUserName");
        agentSignInfo.setProjectCode("f337f9748c5146ed9c520d26f02e06b1");
        agentSignInfo.setIsProxySign(1);

        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId("40C1F3CB74D35AE3E05312016B0AA49B");
        investorSignInfo.setAuthorizationTime(new DateTime("2017-08-18").toString("yyyyMMddHHmmss"));
        investorSignInfo.setLocation("118.187.56.162");
        investorSignInfo.setSignLocation("investorUserName");
        investorSignInfo.setProjectCode("3cfa5dbae5fb43f7aeb7a86a8ce6f534");
        investorSignInfo.setIsProxySign(1);

        createContract.setSignInfos(new SignInfoVO[]{agentSignInfo, investorSignInfo});
        createContractlist.add(createContract);

        tx3202ReqVO.setHead(head);
        tx3202ReqVO.setBatchNo("B141");
        tx3202ReqVO.setCreateContracts(createContractlist.toArray(new CreateContractVO[1]));

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3202ReqVO);
        System.out.println("req:" + req);

        String res = anxinClient.send(TxCode.CREATE_CONTRACT_BATCH, req);
        System.out.println("res:" + res);
    }
}
