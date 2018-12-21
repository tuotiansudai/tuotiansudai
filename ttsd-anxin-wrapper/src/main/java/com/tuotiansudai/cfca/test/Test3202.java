package com.tuotiansudai.cfca.test;

import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.request.tx3.Tx3202ReqVO;
import com.tuotiansudai.cfca.connector.AnxinClientTest;
import com.tuotiansudai.cfca.constant.TxCode;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test3202 {
    public static void main(String[] args) throws Exception {
        AnxinClientTest anxinClient = new AnxinClientTest();
        anxinClient.initSSL();

        Tx3202ReqVO tx3202ReqVO = new Tx3202ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime("20181221235959");

        List<CreateContractVO> createContractlist = new ArrayList<CreateContractVO>();

//        CreateContractVO createContract = loanContract();
//        CreateContractVO createContract = transferContract();
        CreateContractVO createContract = loanServiceContract();
//        CreateContractVO createContract = loanConsumeContract();

        createContractlist.add(createContract);

        tx3202ReqVO.setHead(head);
        tx3202ReqVO.setBatchNo("B2018122104");
        tx3202ReqVO.setCreateContracts(createContractlist.toArray(new CreateContractVO[1]));

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3202ReqVO);
        System.out.println("req:" + req);

        String res = anxinClient.send(TxCode.CREATE_CONTRACT_BATCH, req);
        System.out.println("res:" + res);
    }

    public static CreateContractVO loanContract(){
        CreateContractVO createContract = new CreateContractVO();

        Map<String, String> dataModel = new HashMap<>();
        dataModel.put("agentMobile", "18895730992");
        dataModel.put("agentIdentityNumber", "111111111111111111");
        dataModel.put("investorMobile", "18895730992");
        dataModel.put("investorIdentityNumber", "111111111111111111");
        dataModel.put("loanerUserName", "借款人");
        dataModel.put("loanerIdentityNumber", "111111111111111111");
        dataModel.put("loanAmount1", "100元");
        dataModel.put("loanAmount2", "100元");
        dataModel.put("periods1", "3");
        dataModel.put("periods2", "3");
        dataModel.put("totalRate", "10%");
        dataModel.put("recheckTime1", "2018-10-10");
        dataModel.put("recheckTime2", "2018-10-10");
        dataModel.put("endTime1", "2018-10-10");
        dataModel.put("endTime2", "2018-10-10");
        dataModel.put("orderId", "20181010");
        dataModel.put("pledge", "房产");
        dataModel.put("purpose", "借款用途");
        dataModel.put("repayType", "到期还本付息");

        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId("40C1F3CB74D35AE3E05312016B0AA49B");
        agentSignInfo.setAuthorizationTime("20160214171200");
        agentSignInfo.setLocation("172.16.10.1");
        agentSignInfo.setSignLocation("agentUserName");
        agentSignInfo.setProjectCode("8735358083014a0dbe88e2cbf711c734");
        agentSignInfo.setIsProxySign(1);

        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId("4074F0BDDC1263F9E05311016B0A0D35");
        investorSignInfo.setAuthorizationTime("20160214171200");
        investorSignInfo.setLocation("118.187.56.162");
        investorSignInfo.setSignLocation("investorUserName");
        investorSignInfo.setProjectCode("f337f9748c5146ed9c520d26f02e06b1");
        investorSignInfo.setIsProxySign(1);

        createContract.setInvestmentInfo(dataModel);
        createContract.setSignInfos(new SignInfoVO[]{agentSignInfo, investorSignInfo});
        createContract.setTemplateId("JK_2303");  //抵押
//        createContract.setTemplateId("JK_2304");   //消费
        createContract.setIsSign(1);
        return createContract;
    }

    private static CreateContractVO transferContract(){
        CreateContractVO createContractVO = new CreateContractVO();
        Map<String, String> dataModel = new HashMap<>();
        dataModel.put("transferMobile", "11111111111");
        dataModel.put("transferIdentity", "111111111111111111");
        dataModel.put("transfereeMobile", "11111111111");
        dataModel.put("transfereeIdentity", "111111111111111111");
        dataModel.put("userName", "userName");
        dataModel.put("identity", "111111111111111111");
        dataModel.put("amount", "100元");
        dataModel.put("totalRate", "10%");
        dataModel.put("periods", "3");
        dataModel.put("transferStartTime", "2018-11-11");
        dataModel.put("transferEndTime", "2018-11-11");
        dataModel.put("investAmount", "100元");
        dataModel.put("transferTime", "2018-11-11");
        dataModel.put("leftPeriod", "3");
        dataModel.put("orderId", "1123123");
        dataModel.put("msg1", "msg1");
        dataModel.put("msg2", "msg2");
        dataModel.put("msg3", "msg3");
        dataModel.put("purpose", "借款用途");
        dataModel.put("repayType", "到期还本付息");
        dataModel.put("pledge", "车辆");

        createContractVO.setInvestmentInfo(dataModel);

        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId("40C1F3CB74D35AE3E05312016B0AA49B");
        agentSignInfo.setAuthorizationTime("20160214171200");
        agentSignInfo.setLocation("172.16.10.1");
        agentSignInfo.setSignLocation("transferUserName");
        agentSignInfo.setProjectCode("8735358083014a0dbe88e2cbf711c734");
        agentSignInfo.setIsProxySign(1);

        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId("4074F0BDDC1263F9E05311016B0A0D35");
        investorSignInfo.setAuthorizationTime("20160214171200");
        investorSignInfo.setLocation("118.187.56.162");
        investorSignInfo.setSignLocation("transfereeUserName");
        investorSignInfo.setProjectCode("f337f9748c5146ed9c520d26f02e06b1");
        investorSignInfo.setIsProxySign(1);

        createContractVO.setSignInfos(new SignInfoVO[]{agentSignInfo, investorSignInfo});
        createContractVO.setTemplateId("JK_2315");
        createContractVO.setIsSign(1);
        return createContractVO;
    }

    private static CreateContractVO loanServiceContract(){
        CreateContractVO createContractVO = new CreateContractVO();
        Map<String, String> dataModel = new HashMap<>();
        dataModel.put("loanerName", "朱坤");
        dataModel.put("loanerIdentityNumber", "111111111111111111");
        dataModel.put("loanerMobile", "11111111111");
        dataModel.put("loanAmountChinese", "十万元整");
        dataModel.put("loanAmount", "100000.00");
        dataModel.put("duration", "360");
        dataModel.put("periods1", "12");
        dataModel.put("serviceAmountChinese", "零");
        dataModel.put("serviceAmount", "0.00");
        dataModel.put("avgServiceAmountChinese", "零");
        dataModel.put("avgServiceAmount", "0");
        dataModel.put("periods2", "12");
        dataModel.put("signDate", "2018-12-21");

        createContractVO.setInvestmentInfo(dataModel);

        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId("40C1F3CB74D35AE3E05312016B0AA49B");
        agentSignInfo.setAuthorizationTime("20160214171200");
        agentSignInfo.setLocation("172.16.10.1");
        agentSignInfo.setSignLocation("loaner");
        agentSignInfo.setProjectCode("8735358083014a0dbe88e2cbf711c734");
        agentSignInfo.setIsProxySign(1);

        createContractVO.setSignInfos(new SignInfoVO[]{agentSignInfo});
        createContractVO.setTemplateId("JK_2496");
        createContractVO.setIsSign(1);
        return createContractVO;
    }

    private static CreateContractVO loanConsumeContract(){
        CreateContractVO createContractVO = new CreateContractVO();
        Map<String, String> dataModel = new HashMap<>();
        dataModel.put("investorMobile", "11111111111");
        dataModel.put("investorIdentityNumber", "111111111111111111");
        dataModel.put("agentIdentityNumber", "222222222222222222");
        dataModel.put("loanerUserName", "高应龙");
        dataModel.put("loanerIdentityNumber", "222222222222222222");
        dataModel.put("purpose", "娶媳妇");
        dataModel.put("amount", "100000.00");
        dataModel.put("amountChinese", "十万元整");
        dataModel.put("remark", "");
        dataModel.put("overdueRate1", "0");
        dataModel.put("overdueRate2", "0");
        dataModel.put("overdueDays", "0");

        createContractVO.setInvestmentInfo(dataModel);

        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId("40C1F3CB74D35AE3E05312016B0AA49B");
        agentSignInfo.setAuthorizationTime("20160214171200");
        agentSignInfo.setLocation("172.16.10.1");
        agentSignInfo.setSignLocation("agentUserName");
        agentSignInfo.setProjectCode("8735358083014a0dbe88e2cbf711c734");
        agentSignInfo.setIsProxySign(1);

        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId("4074F0BDDC1263F9E05311016B0A0D35");
        investorSignInfo.setAuthorizationTime("20160214171200");
        investorSignInfo.setLocation("118.187.56.162");
        investorSignInfo.setSignLocation("investorUserName");
        investorSignInfo.setProjectCode("f337f9748c5146ed9c520d26f02e06b1");
        investorSignInfo.setIsProxySign(1);

        createContractVO.setSignInfos(new SignInfoVO[]{agentSignInfo, investorSignInfo});
        createContractVO.setTemplateId("JK_2315");
        createContractVO.setIsSign(1);
        return createContractVO;
    }
}
