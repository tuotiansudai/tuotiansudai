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
import com.tuotiansudai.cfca.mapper.AnxinContractRequestMapper;
import com.tuotiansudai.cfca.mapper.AnxinContractResponseMapper;
import com.tuotiansudai.cfca.mapper.AnxinSignRequestMapper;
import com.tuotiansudai.cfca.model.AnxinContractRequestModel;
import com.tuotiansudai.cfca.model.AnxinContractResponseModel;
import com.tuotiansudai.cfca.model.AnxinSignRequestModel;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.cfca.service.RequestResponseService;
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
import java.util.List;
import java.util.Map;

@Service
public class AnxinSignConnectServiceImpl implements AnxinSignConnectService {

    static Logger logger = Logger.getLogger(AnxinSignConnectService.class);

    @Autowired
    private RequestResponseService requestResponseService;

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

        // 记录请求日志
        requestResponseService.insertCreateAccountRequest(tx3001ReqVO);

        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        logger.debug("res:" + res);

        Tx3ResVO tx3001ResVO = readResponse(res, Tx3001ResVO.class);

        // 记录响应日志
        requestResponseService.insertCreateAccountResponse((Tx3001ResVO) tx3001ResVO);
        return tx3001ResVO;
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

        // 记录请求日志
        requestResponseService.insertSendCaptchaRequest(tx3101ReqVO);

        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        logger.debug("res:" + res);
        Tx3ResVO tx3101ResVO = readResponse(res, Tx3101ResVO.class);

        // 记录响应日志
        requestResponseService.insertSendCaptchaResponse((Tx3101ResVO) tx3101ResVO);
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

        // 记录请求日志
        requestResponseService.insertVerifyCaptchaRequest(tx3102ReqVO);

        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);

        logger.debug("res:" + res);

        Tx3ResVO tx3102ResVO = readResponse(res, Tx3102ResVO.class);

        // 记录请求日志
        requestResponseService.insertVerifyCaptchaResponse((Tx3102ResVO) tx3102ResVO);

        return tx3102ResVO;
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
    public Tx3202ResVO generateContractBatch3202(long businessId, String batchNo, AnxinContractType anxinContractType, List<CreateContractVO> createContractList) throws PKIException {
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
            ErrorResVO errorResVO = jsonObjectMapper.readValue(res, ErrorResVO.class);
            anxinContractResponseMapper.create(new AnxinContractResponseModel(businessId, batchNo, errorResVO.getErrorCode(), errorResVO.getErrorMessage()));
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
    public List updateContractResponse(long loanId, AnxinContractType anxinContractType) {
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
        person.setAddress(userModel.getCity());
        person.setAuthenticationMode("公安部");
        return person;
    }

    private void generateRequestRecord(long businessId, String batchNo, String txTime, AnxinContractType anxinContractType, List<CreateContractVO> createContractList) {
        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        Tx3202ReqVO tx3202ReqVO = new Tx3202ReqVO();

        tx3202ReqVO.setHead(getHeadVO());
        tx3202ReqVO.setBatchNo(batchNo);
        createContractList.forEach(createContractVO -> {
            if (createContractVO.getSignInfos() != null) {
                tx3202ReqVO.setCreateContracts(new CreateContractVO[]{createContractVO});
                String req = jsonObjectMapper.writeValueAsString(tx3202ReqVO);

                Map<String, String> investmentInfo = createContractVO.getInvestmentInfo();
                anxinContractRequestMapper.create(new AnxinContractRequestModel(businessId, Long.parseLong(investmentInfo.get("orderId")),createContractVO.getContractNo(),
                        anxinContractType.name(), txTime, batchNo, createContractVO.getTemplateId(),
                        createContractVO.getIsSign() != null ? String.valueOf(createContractVO.getIsSign()) : "0", jsonObjectMapper.writeValueAsString(tx3202ReqVO),
                        DateTime.now().toDate()));
            }
        });
    }

}
