package com.tuotiansudai.cfca.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.*;
import cfca.trustsign.common.vo.request.tx3.Tx3001ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3101ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3102ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3202ReqVO;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3101ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3102ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3202ResVO;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.mapper.AnxinContractRequestMapper;
import com.tuotiansudai.cfca.mapper.AnxinContractResponseMapper;
import com.tuotiansudai.cfca.mapper.AnxinSignRequestMapper;
import com.tuotiansudai.cfca.model.AnxinContractRequestModel;
import com.tuotiansudai.cfca.model.AnxinContractResponseModel;
import com.tuotiansudai.cfca.model.AnxinSignRequestModel;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.cfca.util.SecurityUtil;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnxinSignConnectServiceImpl implements AnxinSignConnectService {

    @Autowired
    private AnxinSignRequestMapper anxinSignRequestMapper;

    @Autowired
    private AnxinContractRequestMapper anxinContractRequestMapper;

    @Autowired
    private AnxinContractResponseMapper anxinContractResponseMapper;

    @Override
    public Tx3001ResVO createAccount3001(AccountModel accountModel, UserModel userModel) throws PKIException {

        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3001ReqVO tx3001ReqVO = new Tx3001ReqVO();

        PersonVO person = convertAccountToPersonVO(accountModel, userModel);

        tx3001ReqVO.setHead(getHeadVO());
        tx3001ReqVO.setPerson(person);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3001ReqVO);
        System.out.println("req:" + req);

        String txCode = "3001";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        Tx3001ResVO tx3001ResVO = jsonObjectMapper.readValue(res, Tx3001ResVO.class);
        System.out.println("res:" + res);
        System.out.println("tx3001ResVO:" + tx3001ResVO);

        return tx3001ResVO;
    }


    @Override
    public Tx3101ResVO sendCaptcha3101(String userId, String projectCode, boolean isVoice) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3101ReqVO tx3101ReqVO = new Tx3101ReqVO();

        ProxySignVO proxySignVO = new ProxySignVO();
        proxySignVO.setUserId(userId);
        proxySignVO.setProjectCode(projectCode);
        proxySignVO.setIsSendVoice(isVoice ? 1 : 0);

        tx3101ReqVO.setHead(getHeadVO());
        tx3101ReqVO.setProxySign(proxySignVO);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3101ReqVO);
        System.out.println("req:" + req);

        String txCode = "3101";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        Tx3101ResVO tx3101ResVO = jsonObjectMapper.readValue(res, Tx3101ResVO.class);

        System.out.println("res:" + res);
        return tx3101ResVO;
    }

    @Override
    public Tx3102ResVO verifyCaptcha3102(String userId, String projectCode, String checkCode) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3102ReqVO tx3102ReqVO = new Tx3102ReqVO();

        ProxySignVO proxySignVO = new ProxySignVO();
        proxySignVO.setUserId(userId);
        proxySignVO.setProjectCode(projectCode);
        proxySignVO.setCheckCode(checkCode);

        tx3102ReqVO.setHead(getHeadVO());
        tx3102ReqVO.setProxySign(proxySignVO);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3102ReqVO);
        System.out.println("req:" + req);

        String txCode = "3102";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        Tx3102ResVO tx3102ResVO = jsonObjectMapper.readValue(res, Tx3102ResVO.class);

        System.out.println("res:" + res);
        return tx3102ResVO;
    }

    @Override
    public Tx3202ResVO generateContractBatch3202(long loanId, String batchNo, List<CreateContractVO> createContractList) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3202ReqVO tx3202ReqVO = new Tx3202ReqVO();

        tx3202ReqVO.setHead(getHeadVO());
        tx3202ReqVO.setBatchNo(batchNo);
        tx3202ReqVO.setCreateContracts(createContractList.toArray(new CreateContractVO[0]));

        generateRequestRecord(loanId, tx3202ReqVO.getBatchNo(), tx3202ReqVO.getHead().getTxTime(), createContractList);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3202ReqVO);
        System.out.println("req:" + req);

        String txCode = "3202";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        Tx3202ResVO tx3202ResVO = jsonObjectMapper.readValue(res, Tx3202ResVO.class);
        AnxinContractResponseModel anxinContractResponseModel;
        if (Strings.isNullOrEmpty(tx3202ResVO.getBatchNo())) {
            Map<String, String> response = coverJson(res);
            anxinContractResponseModel = new AnxinContractResponseModel(loanId, batchNo, response.get("errorCode"), response.get("errorMessage"));
        } else {
            anxinContractResponseModel = new AnxinContractResponseModel(loanId,
                    tx3202ResVO.getBatchNo(), tx3202ResVO.getHead().getTxTime(), tx3202ResVO.getHead().getLocale(),
                    tx3202ResVO.getHead().getRetCode(), tx3202ResVO.getHead().getRetMessage(), DateTime.now().toDate());
        }

        anxinContractResponseMapper.create(anxinContractResponseModel);
        System.out.println("res:" + res);
        return null;
    }

    private Map<String, String> coverJson(String response) {
        Map<String, String> responseMap = Maps.newConcurrentMap();
        String[] split = response.split(",");
        for (String str : split) {
            responseMap.put("str.split(\":\")[0]", str.split(":")[1]);
        }
        return responseMap;
    }


    private HeadVO getHeadVO() {
        HeadVO head = new HeadVO();
        head.setTxTime(DateTime.now().toString("yyyyMMddHHmmss"));
        return head;
    }

    private PersonVO convertAccountToPersonVO(AccountModel accountModel, UserModel userModel) {

        PersonVO person = new PersonVO();
        person.setPersonName(accountModel.getUserName());
        person.setIdentTypeCode("1");
        person.setIdentNo(accountModel.getIdentityNumber());
        person.setMobilePhone(userModel.getMobile());
        person.setEmail(userModel.getEmail());
        person.setAddress(userModel.getCity());
        person.setAuthenticationMode("公安部");

        return person;
    }

    private void generateRequestRecord(long loanId, String batchNo, String txTime, List<CreateContractVO> createContractList) {
        createContractList.forEach(createContractVO -> {
            if (createContractVO.getSignInfos() != null) {
                long agentSignId = 0l;
                long investorSignId = 0l;
                for (SignInfoVO signInfoVo : createContractVO.getSignInfos()) {
                    AnxinSignRequestModel anxinSignRequestModel = new AnxinSignRequestModel(signInfoVo.getUserId(), signInfoVo.getAuthorizationTime(), signInfoVo.getLocation(),
                            signInfoVo.getSignLocation(), signInfoVo.getProjectCode(), signInfoVo.getIsProxySign() != null ? String.valueOf(signInfoVo.getIsProxySign()) : "0",
                            signInfoVo.getIsCopy() != null ? String.valueOf(signInfoVo.getIsCopy()) : "1", DateTime.now().toDate());
                    anxinSignRequestMapper.create(anxinSignRequestModel);
                    if (signInfoVo.getSignLocation().equals("agentLoginName")) {
                        agentSignId = anxinSignRequestModel.getId();
                        investorSignId = anxinSignRequestModel.getId();
                    } else {
                        agentSignId = anxinSignRequestModel.getId();
                        investorSignId = anxinSignRequestModel.getId();
                    }
                }

                Map<String, String> investmentInfo = createContractVO.getInvestmentInfo();
                anxinContractRequestMapper.create(new AnxinContractRequestModel(loanId,Long.parseLong(investmentInfo.get("investId")), agentSignId, investorSignId, txTime, batchNo, createContractVO.getTemplateId(),
                        createContractVO.getIsSign() != null ? String.valueOf(createContractVO.getIsSign()) : "0", investmentInfo.get("agentMobile"),
                        investmentInfo.get("loanerIdentityNumber"), investmentInfo.get("recheckTime"), investmentInfo.get("totalRate"),
                        investmentInfo.get("investorMobile"), investmentInfo.get("agentIdentityNumber"), investmentInfo.get("periods"),
                        investmentInfo.get("pledge"), investmentInfo.get("endTime"), investmentInfo.get("investorIdentityNumber"),
                        investmentInfo.get("loanerUserName"), investmentInfo.get("loanAmount"), DateTime.now().toDate()));
            }
        });
    }
}
