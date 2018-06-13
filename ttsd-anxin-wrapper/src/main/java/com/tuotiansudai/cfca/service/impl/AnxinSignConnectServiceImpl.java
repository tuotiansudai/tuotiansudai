package com.tuotiansudai.cfca.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.PersonVO;
import cfca.trustsign.common.vo.cs.ProxySignVO;
import cfca.trustsign.common.vo.request.tx3.*;
import cfca.trustsign.common.vo.response.ErrorResVO;
import cfca.trustsign.common.vo.response.tx3.*;
import com.tuotiansudai.cfca.connector.AnxinClient;
import com.tuotiansudai.cfca.constant.AnxinRetCode;
import com.tuotiansudai.cfca.constant.TxCode;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;
import com.tuotiansudai.cfca.dto.ContractResponseView;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.cfca.service.RequestResponseService;
import com.tuotiansudai.repository.model.AnxinContractType;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnxinSignConnectServiceImpl implements AnxinSignConnectService {

    private final static Logger logger = Logger.getLogger(AnxinSignConnectService.class);

    @Autowired
    private RequestResponseService requestResponseService;

    private AnxinClient anxinClient = AnxinClient.getClient();

    private JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();

    @Override
    public Tx3001ResVO createAccount3001(UserModel userModel) throws PKIException {
        PersonVO person = convertAccountToPersonVO(userModel);

        Tx3001ReqVO tx3001ReqVO = new Tx3001ReqVO();
        tx3001ReqVO.setHead(getHeadVO());
        tx3001ReqVO.setPerson(person);

        String req = jsonObjectMapper.writeValueAsString(tx3001ReqVO);
        logger.info("[安心签] create account request:" + req);

        // 记录请求日志
        requestResponseService.insertCreateAccountRequest(tx3001ReqVO);

        String res = anxinClient.send(TxCode.CREATE_ACCOUNT, req);

        logger.info("[安心签] create account response:" + res);

        Tx3ResVO tx3ResVO = readResponse(res, Tx3001ResVO.class);

        // 记录响应日志
        if (tx3ResVO != null) {
            requestResponseService.insertCreateAccountResponse((Tx3001ResVO) tx3ResVO);
            return (Tx3001ResVO) tx3ResVO;
        } else {
            return null;
        }
    }


    @Override
    public Tx3101ResVO sendCaptcha3101(String userId, String projectCode, boolean isVoice) throws PKIException {
        ProxySignVO proxySignVO = new ProxySignVO();
        proxySignVO.setUserId(userId);
        proxySignVO.setProjectCode(projectCode);
        proxySignVO.setIsSendVoice(isVoice ? 1 : 0);

        Tx3101ReqVO tx3101ReqVO = new Tx3101ReqVO();
        tx3101ReqVO.setHead(getHeadVO());
        tx3101ReqVO.setProxySign(proxySignVO);

        String req = jsonObjectMapper.writeValueAsString(tx3101ReqVO);
        logger.info("[安心签] send captcha request:" + req);

        // 记录请求日志
        requestResponseService.insertSendCaptchaRequest(tx3101ReqVO);

        String res = anxinClient.send(TxCode.SEND_CAPTCHA, req);

        logger.info("[安心签] send captcha response:" + res);

        Tx3ResVO tx3ResVO = readResponse(res, Tx3101ResVO.class);

        // 记录响应日志
        if (tx3ResVO != null) {
            requestResponseService.insertSendCaptchaResponse((Tx3101ResVO) tx3ResVO);
            return (Tx3101ResVO) tx3ResVO;
        } else {
            return null;
        }
    }

    @Override
    public Tx3102ResVO verifyCaptcha3102(String userId, String projectCode, String checkCode) throws PKIException {

        ProxySignVO proxySignVO = new ProxySignVO();
        proxySignVO.setUserId(userId);
        proxySignVO.setProjectCode(projectCode);
        proxySignVO.setCheckCode(checkCode);

        Tx3102ReqVO tx3102ReqVO = new Tx3102ReqVO();
        tx3102ReqVO.setHead(getHeadVO());
        tx3102ReqVO.setProxySign(proxySignVO);

        String req = jsonObjectMapper.writeValueAsString(tx3102ReqVO);

        logger.info(MessageFormat.format("[安心签] verifyCaptcha. userId:{0}, projectCode:{1}, checkCode:{2}, request date:{3}", userId, projectCode, checkCode, req));

        // 记录请求日志
        requestResponseService.insertVerifyCaptchaRequest(tx3102ReqVO);

        String res = anxinClient.send(TxCode.VERIFY_CAPTCHA, req);

        logger.info(MessageFormat.format("[安心签] verifyCaptcha. userId:{0}, projectCode:{1}, checkCode:{2}, response date:{3}", userId, projectCode, checkCode, res));

        Tx3ResVO tx3ResVO = readResponse(res, Tx3102ResVO.class);

        // 记录响应日志
        if (tx3ResVO != null) {
            requestResponseService.insertVerifyCaptchaResponse((Tx3102ResVO) tx3ResVO);
            return (Tx3102ResVO) tx3ResVO;
        } else {
            return null;
        }
    }

    @Override
    public Tx3202ResVO createContractBatch3202(long businessId, String batchNo, AnxinContractType contractType, List<CreateContractVO> createContractList) throws PKIException {
        Tx3202ReqVO tx3202ReqVO = new Tx3202ReqVO();
        tx3202ReqVO.setHead(getHeadVO());
        tx3202ReqVO.setBatchNo(batchNo);
        tx3202ReqVO.setCreateContracts(createContractList.toArray(new CreateContractVO[createContractList.size()]));

        String req = jsonObjectMapper.writeValueAsString(tx3202ReqVO);

        logger.info(MessageFormat.format("[安心签] create contract batch request, businessId:{0}, batchNo:{1}, data:{2}", String.valueOf(businessId), batchNo, req));

        // 记录请求日志
        requestResponseService.insertBatchGenerateContractRequest(businessId, tx3202ReqVO);

        String res = anxinClient.send(TxCode.CREATE_CONTRACT_BATCH, req);

        logger.info(MessageFormat.format("[安心签] create contract batch response, businessId:{0}, batchNo:{1}, data:{2}", String.valueOf(businessId), batchNo, res));

        Tx3ResVO tx3ResVO = readResponse(res, Tx3202ResVO.class);

        // 记录响应日志
        if (tx3ResVO != null) {
            requestResponseService.insertBatchGenerateContractResponse(businessId, batchNo, (Tx3202ResVO) tx3ResVO);
            return (Tx3202ResVO) tx3ResVO;
        } else {
            return null;
        }
    }

    @Override
    public Tx3211ResVO queryContractBatch3211(long businessId, String batchNo) throws PKIException {
        Tx3211ReqVO tx3211ReqVO = new Tx3211ReqVO();
        tx3211ReqVO.setHead(getHeadVO());
        tx3211ReqVO.setBatchNo(batchNo);

        String req = jsonObjectMapper.writeValueAsString(tx3211ReqVO);

        logger.info(MessageFormat.format("[安心签] Query contract batch request, businessId:{0}, batchNo:{1}, data:{2}", String.valueOf(businessId), batchNo, req));

        // 记录请求日志
        requestResponseService.insertBatchQueryContractRequest(businessId, tx3211ReqVO);

        String res = anxinClient.send(TxCode.QUERY_CONTRACT_BATCH, req);

        logger.info(MessageFormat.format("[安心签] Query contract batch response, businessId:{0}, batchNo:{1}, data:{2} ", String.valueOf(businessId), batchNo, res));

        Tx3ResVO tx3ResVO = readResponse(res, Tx3211ResVO.class);

        // 记录响应日志
        if (tx3ResVO != null) {
            requestResponseService.insertBatchQueryContractResponse(businessId, batchNo, (Tx3211ResVO) tx3ResVO);
            return (Tx3211ResVO) tx3ResVO;
        } else {
            return null;
        }
    }


    @Override
    public List<ContractResponseView> queryContract(long businessId, List<String> batchNoList, AnxinContractType anxinContractType) {

        // 合同还没有生成完毕的batchNo
        List<ContractResponseView> contractResponseViews = new ArrayList<>();

        if (CollectionUtils.isEmpty(batchNoList)) {
            logger.info("[安心签] Query contract response error, batchNoList is empty, businessId: " + businessId);
            return contractResponseViews;
        }

        for (String batchNo : batchNoList) {
            Tx3211ResVO tx3211ResVO;

            try {
                tx3211ResVO = queryContractBatch3211(businessId, batchNo);
                if (tx3211ResVO == null || !isSuccess(tx3211ResVO)) {
                    logger.error(MessageFormat.format("[安心签] Query contract response error. businessId:{0}, batchNo:{1}.", String.valueOf(businessId), batchNo));
                    return null;
                }
            } catch (PKIException e) {
                logger.error(MessageFormat.format("[安心签] Query contract response error, businessId:{0}, batchNo:{1}", String.valueOf(businessId) + "", batchNo), e);
                return null;
            }

            for (CreateContractVO createContractVO : tx3211ResVO.getCreateContracts()) {
                contractResponseViews.add(new ContractResponseView(Long.parseLong(createContractVO.getInvestmentInfo().get("orderId")),
                        createContractVO.getContractNo(), createContractVO.getCode()));
            }
        }
        return contractResponseViews;
    }

    @Override
    public byte[] downLoanContractByBatchNo(String contractNo) {
        logger.info("[安心签] download contract, contractNo: " + contractNo);
        return anxinClient.getContractFile(contractNo);
    }

    private Tx3ResVO readResponse(String res, Class<? extends Tx3ResVO> cla) {
        try {
            if (res.indexOf("\"" + AnxinRetCode.SUCCESS + "\"") > 0) {
                // 成功：
                return jsonObjectMapper.readValue(res, cla);
            } else {
                // 失败：
                ErrorResVO errorResVO = jsonObjectMapper.readValue(res, ErrorResVO.class);

                HeadVO headVO = new HeadVO();
                headVO.setRetCode(errorResVO.getErrorCode());
                headVO.setRetMessage(errorResVO.getErrorMessage());
                Tx3ResVO tx3ResVO = cla.newInstance();
                tx3ResVO.setHead(headVO);
                return tx3ResVO;
            }
        } catch (Exception e) {
            logger.error("[安心签] read response of " + cla.getName() + " fail. response: " + res, e);
            return null;
        }
    }

    private HeadVO getHeadVO() {
        HeadVO head = new HeadVO();
        head.setTxTime(DateTime.now().toString("yyyyMMddHHmmss"));
        return head;
    }

    private PersonVO convertAccountToPersonVO(UserModel userModel) {
        PersonVO person = new PersonVO();
        person.setPersonName(userModel.getUserName());
        person.setIdentTypeCode("0");
        person.setIdentNo(userModel.getIdentityNumber());
        person.setMobilePhone(userModel.getMobile());
        person.setAddress(userModel.getCity());
        person.setAuthenticationMode("公安部");
        return person;
    }

    private boolean isSuccess(Tx3ResVO tx3ResVO) {
        return tx3ResVO != null && tx3ResVO.getHead() != null && AnxinRetCode.SUCCESS.equals(tx3ResVO.getHead().getRetCode());
    }
}
