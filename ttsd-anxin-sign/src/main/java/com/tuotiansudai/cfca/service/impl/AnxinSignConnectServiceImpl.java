package com.tuotiansudai.cfca.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.*;
import cfca.trustsign.common.vo.request.tx3.*;
import cfca.trustsign.common.vo.response.ErrorResVO;
import cfca.trustsign.common.vo.response.tx3.*;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.cfca.dto.ContractResponseView;
import com.tuotiansudai.cfca.mapper.*;
import com.tuotiansudai.cfca.model.*;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.cfca.util.SecurityUtil;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AnxinSignConnectServiceImpl implements AnxinSignConnectService {

    static Logger logger = Logger.getLogger(AnxinSignConnectService.class);

    @Autowired
    private AnxinCreateAccountRequestMapper anxinCreateAccountRequestMapper;

    @Autowired
    private AnxinCreateAccountResponseMapper anxinCreateAccountResponseMapper;

    @Autowired
    private AnxinSignRequestMapper anxinSignRequestMapper;

    @Autowired
    private AnxinContractRequestMapper anxinContractRequestMapper;

    @Autowired
    private AnxinContractResponseMapper anxinContractResponseMapper;

    @Override
    public Tx3ResVO createAccount3001(AccountModel accountModel, UserModel userModel) throws PKIException {

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

        insertCreateAccountRequestRecord(tx3001ReqVO);

        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        logger.debug("res:" + res);

        Tx3ResVO tx3001ResVO = readResponse(res, Tx3001ResVO.class);
        return tx3001ResVO;
    }

    private void insertCreateAccountRequestRecord(Tx3001ReqVO tx3001ReqVO) {

        AnxinCreateAccountRequestModel requestModel = new AnxinCreateAccountRequestModel();
        DateTime now = DateTime.now();
        requestModel.setTxTime(now.toString("yyyyMMddHHmmss"));
        requestModel.setPersonName(tx3001ReqVO.getPerson().getPersonName());
        requestModel.setIdentTypeCode(tx3001ReqVO.getPerson().getIdentTypeCode());
        requestModel.setIdentNo(tx3001ReqVO.getPerson().getIdentNo());
        requestModel.setEmail(tx3001ReqVO.getPerson().getEmail());
        requestModel.setMobilePhone(tx3001ReqVO.getPerson().getMobilePhone());
        requestModel.setAddress(tx3001ReqVO.getPerson().getAddress());
        requestModel.setAuthenticationMode(tx3001ReqVO.getPerson().getAuthenticationMode());
        requestModel.setNotSendPwd(tx3001ReqVO.getNotSendPwd() == null ? "0" : tx3001ReqVO.getNotSendPwd().toString());
        requestModel.setCreatedTime(now.toDate());
        anxinCreateAccountRequestMapper.create(requestModel);
    }


    private void insertCreateAccountResponseRecord(Tx3001ResVO tx3001ResVO) {

        AnxinCreateAccountResponseModel responseModel = new AnxinCreateAccountResponseModel();

        responseModel.setRetCode(tx3001ResVO.getHead().getRetCode());
        responseModel.setRetMessage(tx3001ResVO.getHead().getRetMessage());

        if ("60000000".equals(tx3001ResVO.getHead().getRetCode())) {
            responseModel.setTxTime(tx3001ResVO.getHead().getTxTime());
            responseModel.setPersonName(tx3001ResVO.getPerson().getPersonName());
            responseModel.setIdentTypeCode(tx3001ResVO.getPerson().getIdentTypeCode());
            responseModel.setIdentNo(tx3001ResVO.getPerson().getIdentNo());
            responseModel.setEmail(tx3001ResVO.getPerson().getEmail());
            responseModel.setMobilePhone(tx3001ResVO.getPerson().getMobilePhone());
            responseModel.setAddress(tx3001ResVO.getPerson().getAddress());
            responseModel.setAuthenticationMode(tx3001ResVO.getPerson().getAuthenticationMode());
            responseModel.setNotSendPwd(tx3001ResVO.getNotSendPwd() == null ? "0" : tx3001ResVO.getNotSendPwd().toString());
        }
        responseModel.setCreatedTime(new Date());
        anxinCreateAccountResponseMapper.create(responseModel);
    }

    @Override
    public Tx3ResVO sendCaptcha3101(String userId, String projectCode, boolean isVoice) throws PKIException {
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

        logger.debug("res:" + res);
        Tx3ResVO tx3101ResVO = readResponse(res, Tx3101ResVO.class);

        return tx3101ResVO;
    }

    @Override
    public Tx3ResVO verifyCaptcha3102(String userId, String projectCode, String checkCode) throws PKIException {
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

        logger.debug("res:" + res);

        Tx3ResVO tx3ResVO = readResponse(res, Tx3102ResVO.class);
        return tx3ResVO;
    }


    private Tx3ResVO readResponse(String res, Class<? extends Tx3ResVO> cla) {
        if (res.indexOf("\"60000000\"") > 0) {
            // 成功：
            JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
            return jsonObjectMapper.readValue(res, cla);
        } else {
            // 失败：
            JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
            ErrorResVO errorResVO = jsonObjectMapper.readValue(res, ErrorResVO.class);

            HeadVO headVO = new HeadVO();
            headVO.setRetCode(errorResVO.getErrorCode());
            headVO.setRetMessage(errorResVO.getErrorMessage());
            try {
                Tx3ResVO tx3ResVO = cla.newInstance();
                tx3ResVO.setHead(headVO);
                return tx3ResVO;
            } catch (Exception e) {
                logger.error("read response of " + cla.getName() + " fail.", e);
            }
            return null;
        }
    }

    @Override
    public Tx3202ResVO generateContractBatch3202(long businessId, String batchNo,AnxinContractType anxinContractType, List<CreateContractVO> createContractList) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3202ReqVO tx3202ReqVO = new Tx3202ReqVO();

        tx3202ReqVO.setHead(getHeadVO());
        tx3202ReqVO.setBatchNo(batchNo);
        tx3202ReqVO.setCreateContracts(createContractList.toArray(new CreateContractVO[0]));

        generateRequestRecord(businessId, tx3202ReqVO.getBatchNo(), tx3202ReqVO.getHead().getTxTime(), anxinContractType, createContractList);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3202ReqVO);
        logger.debug(MessageFormat.format("[安心签] loanId:{0},batchNo:{1} created contract request date:{2}", businessId, batchNo, req));

        String txCode = "3202";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        Tx3202ResVO tx3202ResVO = jsonObjectMapper.readValue(res, Tx3202ResVO.class);

        if (Strings.isNullOrEmpty(tx3202ResVO.getBatchNo())) {
            Map<String, String> response = coverJson(res);
            anxinContractResponseMapper.create(new AnxinContractResponseModel(businessId, batchNo, response.get("errorCode"), response.get("errorMessage")));
        } else {
            for (CreateContractVO createContractVO : tx3202ResVO.getCreateContracts()) {
                anxinContractRequestMapper.updateContractNoByInvestId(createContractVO.getContractNo(),
                        Long.parseLong(createContractVO.getInvestmentInfo().get("orderId")),
                        tx3202ReqVO.getHead().getTxTime());
                anxinContractResponseMapper.create(new AnxinContractResponseModel(businessId,
                        tx3202ResVO.getBatchNo(), createContractVO.getContractNo(), tx3202ResVO.getHead().getTxTime(), tx3202ResVO.getHead().getLocale(),
                        DateTime.now().toDate()));
            }
        }
        logger.debug(MessageFormat.format("[安心签] loanId:{0},batchNo:{1} created contract response date:{2}", businessId, batchNo, res));
        return tx3202ResVO;
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
        logger.debug(MessageFormat.format("[安心签] find contract response , batchNo:{0}", batchNo));

        String txCode = "3211";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        return jsonObjectMapper.readValue(res, Tx3202ResVO.class);
    }

    @Override
    public List updateContractResponse(long loanId,AnxinContractType anxinContractType) {
        List<String> batchNos = anxinContractRequestMapper.findBatchNoByBusinessId(loanId);
        List<ContractResponseView> contractResponseViews = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(batchNos)) {
            batchNos.forEach(batchNo -> {
                Tx3202ResVO tx3202ResVO = null;
                try {
                    tx3202ResVO = findContractResponseByBatchNo(batchNo);
                } catch (PKIException e) {
                    logger.error(MessageFormat.format("[安心签] find contract response error, loanId:{0} , batchNo:{1}", loanId, batchNo), e);
                }
                if (tx3202ResVO != null && tx3202ResVO.getCreateContracts() != null) {
                    for (CreateContractVO createContractVO : tx3202ResVO.getCreateContracts()) {
                        anxinContractResponseMapper.updateRetByContractNo(createContractVO.getContractNo(),
                                createContractVO.getCode(),
                                createContractVO.getMessage(),
                                DateTime.now().toDate());
                        contractResponseViews.add(new ContractResponseView(Long.parseLong(createContractVO.getInvestmentInfo().get("orderId")),
                                createContractVO.getContractNo(), createContractVO.getCode()));
                    }
                }
            });
        }
        return contractResponseViews;
    }

    @Override
    public byte[] downLoanContractByBatchNo(String contractNo) throws PKIException, FileNotFoundException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();
        logger.debug(MessageFormat.format("[安心签] down loan contract , contractNo:{0}", contractNo));
        return httpConnector.getFile("platId/" + Request.PLAT_ID + "/contractNo/" + contractNo + "/downloading");
    }

    private Map<String, String> coverJson(String response) {
        Map<String, String> responseMap = Maps.newConcurrentMap();
        String[] split = response.split(",");
        for (String str : split) {
            responseMap.put(str.split(":")[0].replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\"", ""), str.split(":")[1].replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\"", ""));
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

    private void generateRequestRecord(long businessId, String batchNo, String txTime,AnxinContractType anxinContractType, List<CreateContractVO> createContractList) {
        createContractList.forEach(createContractVO -> {
            if (createContractVO.getSignInfos() != null) {
                long agentSignId = 0l;
                long investorSignId = 0l;
                for (SignInfoVO signInfoVo : createContractVO.getSignInfos()) {
                    AnxinSignRequestModel anxinSignRequestModel = new AnxinSignRequestModel(signInfoVo.getUserId(), signInfoVo.getAuthorizationTime(), signInfoVo.getLocation(),
                            signInfoVo.getSignLocation(), signInfoVo.getProjectCode(), signInfoVo.getIsProxySign() != null ? String.valueOf(signInfoVo.getIsProxySign()) : "0",
                            signInfoVo.getIsCopy() != null ? String.valueOf(signInfoVo.getIsCopy()) : "1", DateTime.now().toDate());
                    anxinSignRequestMapper.create(anxinSignRequestModel);
                    if (signInfoVo.getSignLocation().equals("agentUserName")) {
                        agentSignId = anxinSignRequestModel.getId();
                    } else {
                        investorSignId = anxinSignRequestModel.getId();
                    }
                }

                Map<String, String> investmentInfo = createContractVO.getInvestmentInfo();
                anxinContractRequestMapper.create(new AnxinContractRequestModel(businessId,anxinContractType.name(), Long.parseLong(investmentInfo.get("orderId")),
                        agentSignId, investorSignId, txTime, batchNo, createContractVO.getTemplateId(),
                        createContractVO.getIsSign() != null ? String.valueOf(createContractVO.getIsSign()) : "0", investmentInfo.get("agentMobile"),
                        investmentInfo.get("loanerIdentityNumber"), investmentInfo.get("recheckTime"), investmentInfo.get("totalRate"),
                        investmentInfo.get("investorMobile"), investmentInfo.get("agentIdentityNumber"), investmentInfo.get("periods"),
                        investmentInfo.get("pledge"), investmentInfo.get("endTime"), investmentInfo.get("investorIdentityNumber"),
                        investmentInfo.get("loanerUserName"), investmentInfo.get("loanAmount"), DateTime.now().toDate()));
            }
        });
    }

}
