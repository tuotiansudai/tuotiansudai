package com.tuotiansudai.cfca.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.*;
import cfca.trustsign.common.vo.request.tx3.*;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3101ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3102ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3202ResVO;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.dto.ContractResponseView;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;

@Service
public class AnxinSignConnectServiceImpl implements AnxinSignConnectService {

    static Logger logger = Logger.getLogger(AnxinSignConnectService.class);

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
        logger.debug("req:" + req);

        String txCode = "3001";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        Tx3001ResVO tx3001ResVO = jsonObjectMapper.readValue(res, Tx3001ResVO.class);
        logger.debug("res:" + res);
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
        logger.debug("req:" + req);

        String txCode = "3101";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        Tx3101ResVO tx3101ResVO = jsonObjectMapper.readValue(res, Tx3101ResVO.class);

        logger.debug("res:" + res);
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
        logger.debug("req:" + req);

        String txCode = "3102";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        Tx3102ResVO tx3102ResVO = jsonObjectMapper.readValue(res, Tx3102ResVO.class);

        logger.debug("res:" + res);
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
        logger.debug("req:" + req);

        String txCode = "3202";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        Tx3202ResVO tx3202ResVO = jsonObjectMapper.readValue(res, Tx3202ResVO.class);

        if (Strings.isNullOrEmpty(tx3202ResVO.getBatchNo())) {
            Map<String, String> response = coverJson(res);
            anxinContractResponseMapper.create(new AnxinContractResponseModel(loanId, batchNo, response.get("errorCode"), response.get("errorMessage")));
        } else {
            for(CreateContractVO createContractVO : tx3202ResVO.getCreateContracts()){
                long investId = Long.parseLong(createContractVO.getInvestmentInfo().get("investId"));
                anxinContractRequestMapper.updateContractNoByInvestId(createContractVO.getContractNo(),investId);
                anxinContractResponseMapper.create(new AnxinContractResponseModel(loanId,
                        tx3202ResVO.getBatchNo(), createContractVO.getContractNo(), tx3202ResVO.getHead().getTxTime(), tx3202ResVO.getHead().getLocale(),
                        "wait", "wait", DateTime.now().toDate()));
            }
        }
        System.out.println("res:" + res);
        return null;
    }

    @Override
    public Tx3202ResVO findContractResponseByBatchNo(String batchNo) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3211ReqVO tx3211ReqVO = new Tx3211ReqVO();

        tx3211ReqVO.setHead(getHeadVO());
        tx3211ReqVO.setBatchNo(batchNo);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3211ReqVO);
        System.out.println("req:" + req);

        String txCode = "3211";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        return jsonObjectMapper.readValue(res, Tx3202ResVO.class);
    }

    @Override
    public List updateContractResponse(long loanId){
        List<String> batchNos = anxinContractRequestMapper.findBatchNoByLoanId(loanId);
        List<ContractResponseView> contractResponseViews = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(batchNos)){
            batchNos.forEach(batchNo -> {
                Tx3202ResVO tx3202ResVO = null;
                try {
                    tx3202ResVO = findContractResponseByBatchNo(batchNo);
                } catch (PKIException e) {
                    e.printStackTrace();
                }
                if (tx3202ResVO != null && tx3202ResVO.getCreateContracts() != null) {
                    for (CreateContractVO createContractVO : tx3202ResVO.getCreateContracts()) {
                        anxinContractResponseMapper.updateRetByContractNo(createContractVO.getContractNo(),
                                createContractVO.getCode(),
                                createContractVO.getMessage(),
                                DateTime.now().toDate());
                        contractResponseViews.add(new ContractResponseView(Long.parseLong(createContractVO.getInvestmentInfo().get("investId")),
                                createContractVO.getContractNo(), createContractVO.getCode()));
                    }
                }
            });
        }
        return contractResponseViews;
    }

    @Override
    public byte[] downLoanContractByBatchNo(String contractNo) throws PKIException, FileNotFoundException{
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        byte[] fileBtye = httpConnector.getFile("platId/" + Request.PLAT_ID + "/contractNo/" + contractNo + "/downloading");

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String filePath = "./file";
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + contractNo + ".pdf");
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(fileBtye);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return httpConnector.getFile("platId/" + Request.PLAT_ID + "/contractNo/" + contractNo + "/downloading");
    }

    private Map<String, String> coverJson(String response) {
        Map<String, String> responseMap = Maps.newConcurrentMap();
        String[] split = response.split(",");
        for (String str : split) {
            responseMap.put(str.split(":")[0].replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\"", ""), str.split(":")[1].replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\"",""));
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
        person.setIdentTypeCode("0");
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
                    AnxinSignRequestModel anxinSignRequestModel = anxinSignRequestMapper.findByUserId(signInfoVo.getUserId());
                    if(anxinSignRequestModel == null){
                        anxinSignRequestModel = new AnxinSignRequestModel(signInfoVo.getUserId(), signInfoVo.getAuthorizationTime(), signInfoVo.getLocation(),
                                signInfoVo.getSignLocation(), signInfoVo.getProjectCode(), signInfoVo.getIsProxySign() != null ? String.valueOf(signInfoVo.getIsProxySign()) : "0",
                                signInfoVo.getIsCopy() != null ? String.valueOf(signInfoVo.getIsCopy()) : "1", DateTime.now().toDate());
                        anxinSignRequestMapper.create(anxinSignRequestModel);
                    }
                    if (signInfoVo.getSignLocation().equals("agentLoginName")) {
                        agentSignId = anxinSignRequestModel.getId();
                        investorSignId = anxinSignRequestModel.getId();
                    } else {
                        agentSignId = anxinSignRequestModel.getId();
                        investorSignId = anxinSignRequestModel.getId();
                    }
                }

                Map<String, String> investmentInfo = createContractVO.getInvestmentInfo();
                if(anxinContractRequestMapper.findSuccessRequestByInvestId(Long.parseLong(investmentInfo.get("investId"))) == 0){
                    anxinContractRequestMapper.create(new AnxinContractRequestModel(loanId,Long.parseLong(investmentInfo.get("investId")),
                            agentSignId, investorSignId, txTime, batchNo, createContractVO.getTemplateId(),
                            createContractVO.getIsSign() != null ? String.valueOf(createContractVO.getIsSign()) : "0", investmentInfo.get("agentMobile"),
                            investmentInfo.get("loanerIdentityNumber"), investmentInfo.get("recheckTime"), investmentInfo.get("totalRate"),
                            investmentInfo.get("investorMobile"), investmentInfo.get("agentIdentityNumber"), investmentInfo.get("periods"),
                            investmentInfo.get("pledge"), investmentInfo.get("endTime"), investmentInfo.get("investorIdentityNumber"),
                            investmentInfo.get("loanerUserName"), investmentInfo.get("loanAmount"), DateTime.now().toDate()));
                }
            }
        });
    }
}
