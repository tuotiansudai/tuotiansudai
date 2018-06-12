package com.tuotiansudai.cfca.service.impl;

import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.request.tx3.*;
import cfca.trustsign.common.vo.response.tx3.*;
import com.tuotiansudai.cfca.constant.AnxinRetCode;
import com.tuotiansudai.cfca.mapper.*;
import com.tuotiansudai.cfca.model.*;
import com.tuotiansudai.cfca.service.RequestResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RequestResponseServiceImpl implements RequestResponseService {

    @Autowired
    AnxinCreateAccountRequestMapper anxinCreateAccountRequestMapper;

    @Autowired
    AnxinCreateAccountResponseMapper anxinCreateAccountResponseMapper;

    @Autowired
    AnxinSendCaptchaRequestMapper anxinSendCaptchaRequestMapper;

    @Autowired
    AnxinSendCaptchaResponseMapper anxinSendCaptchaResponseMapper;

    @Autowired
    AnxinVerifyCaptchaRequestMapper anxinVerifyCaptchaRequestMapper;

    @Autowired
    AnxinVerifyCaptchaResponseMapper anxinVerifyCaptchaResponseMapper;

    @Autowired
    AnxinCreateContractBatchRequestMapper anxinCreateContractBatchRequestMapper;

    @Autowired
    AnxinCreateContractBatchResponseMapper anxinCreateContractBatchResponseMapper;

    @Autowired
    AnxinQueryContractBatchRequestMapper anxinQueryContractBatchRequestMapper;

    @Autowired
    AnxinQueryContractBatchResponseMapper anxinQueryContractBatchResponseMapper;

    @Override
    public void insertCreateAccountRequest(Tx3001ReqVO tx3001ReqVO) {
        AnxinCreateAccountRequestModel requestModel = new AnxinCreateAccountRequestModel();
        requestModel.setTxTime(tx3001ReqVO.getHead().getTxTime());
        requestModel.setPersonName(tx3001ReqVO.getPerson().getPersonName());
        requestModel.setIdentTypeCode(tx3001ReqVO.getPerson().getIdentTypeCode());
        requestModel.setIdentNo(tx3001ReqVO.getPerson().getIdentNo());
        requestModel.setEmail(tx3001ReqVO.getPerson().getEmail());
        requestModel.setMobilePhone(tx3001ReqVO.getPerson().getMobilePhone());
        requestModel.setAddress(tx3001ReqVO.getPerson().getAddress());
        requestModel.setAuthenticationMode(tx3001ReqVO.getPerson().getAuthenticationMode());
        requestModel.setNotSendPwd(tx3001ReqVO.getNotSendPwd() == null ? "0" : tx3001ReqVO.getNotSendPwd().toString());
        requestModel.setCreatedTime(new Date());
        anxinCreateAccountRequestMapper.create(requestModel);
    }

    @Override
    public void insertCreateAccountResponse(Tx3001ResVO tx3001ResVO) {

        AnxinCreateAccountResponseModel responseModel = new AnxinCreateAccountResponseModel();

        responseModel.setRetCode(tx3001ResVO.getHead().getRetCode());
        responseModel.setRetMessage(tx3001ResVO.getHead().getRetMessage());

        if (AnxinRetCode.SUCCESS.equals(tx3001ResVO.getHead().getRetCode())) {
            responseModel.setTxTime(tx3001ResVO.getHead().getTxTime());
            responseModel.setUserId(tx3001ResVO.getPerson().getUserId());
            responseModel.setPersonName(tx3001ResVO.getPerson().getPersonName());
            responseModel.setIdentTypeCode(tx3001ResVO.getPerson().getIdentTypeCode());
            responseModel.setIdentNo(tx3001ResVO.getPerson().getIdentNo());
            responseModel.setEmail(tx3001ResVO.getPerson().getEmail());
            responseModel.setMobilePhone(tx3001ResVO.getPerson().getMobilePhone());
            responseModel.setAddress(tx3001ResVO.getPerson().getAddress());
            responseModel.setAuthenticationMode(tx3001ResVO.getPerson().getAuthenticationMode());
            responseModel.setNotSendPwd(tx3001ResVO.getNotSendPwd() == null ? "0" : tx3001ResVO.getNotSendPwd().toString());
            responseModel.setAnxinSignEmail(tx3001ResVO.getPerson().getAnXinSignEmail());
            responseModel.setAnxinSignMobilePhone(tx3001ResVO.getPerson().getAnXinSignMobilePhone());
        }
        responseModel.setCreatedTime(new Date());
        anxinCreateAccountResponseMapper.create(responseModel);
    }


    @Override
    public void insertSendCaptchaRequest(Tx3101ReqVO tx3101ReqVO) {

        AnxinSendCaptchaRequestModel requestModel = new AnxinSendCaptchaRequestModel();
        requestModel.setTxTime(tx3101ReqVO.getHead().getTxTime());
        requestModel.setUserId(tx3101ReqVO.getProxySign().getUserId());
        requestModel.setProjectCode(tx3101ReqVO.getProxySign().getProjectCode());
        requestModel.setIsSendVoice(tx3101ReqVO.getProxySign().getIsSendVoice() == null ? "0" : tx3101ReqVO.getProxySign().getIsSendVoice().toString());
        requestModel.setCreatedTime(new Date());
        anxinSendCaptchaRequestMapper.create(requestModel);
    }


    @Override
    public void insertSendCaptchaResponse(Tx3101ResVO tx3101ResVO) {

        AnxinSendCaptchaResponseModel responseModel = new AnxinSendCaptchaResponseModel();

        responseModel.setRetCode(tx3101ResVO.getHead().getRetCode());
        responseModel.setRetMessage(tx3101ResVO.getHead().getRetMessage());

        if (AnxinRetCode.SUCCESS.equals(tx3101ResVO.getHead().getRetCode())) {
            responseModel.setTxTime(tx3101ResVO.getHead().getTxTime());
            responseModel.setUserId(tx3101ResVO.getProxySign().getUserId());
            responseModel.setProjectCode(tx3101ResVO.getProxySign().getProjectCode());
            responseModel.setIsSendVoice(tx3101ResVO.getProxySign().getIsSendVoice() == null ? "0" : tx3101ResVO.getProxySign().getIsSendVoice().toString());
        }
        responseModel.setCreatedTime(new Date());
        anxinSendCaptchaResponseMapper.create(responseModel);
    }

    @Override
    public void insertVerifyCaptchaRequest(Tx3102ReqVO tx3102ReqVO) {

        AnxinVerifyCaptchaRequestModel requestModel = new AnxinVerifyCaptchaRequestModel();
        requestModel.setTxTime(tx3102ReqVO.getHead().getTxTime());
        requestModel.setUserId(tx3102ReqVO.getProxySign().getUserId());
        requestModel.setProjectCode(tx3102ReqVO.getProxySign().getProjectCode());
        requestModel.setCheckCode(tx3102ReqVO.getProxySign().getCheckCode());
        requestModel.setCreatedTime(new Date());
        anxinVerifyCaptchaRequestMapper.create(requestModel);
    }


    @Override
    public void insertVerifyCaptchaResponse(Tx3102ResVO tx3102ResVO) {

        AnxinVerifyCaptchaResponseModel responseModel = new AnxinVerifyCaptchaResponseModel();

        responseModel.setRetCode(tx3102ResVO.getHead().getRetCode());
        responseModel.setRetMessage(tx3102ResVO.getHead().getRetMessage());

        if (AnxinRetCode.SUCCESS.equals(tx3102ResVO.getHead().getRetCode())) {
            responseModel.setTxTime(tx3102ResVO.getHead().getTxTime());
            responseModel.setUserId(tx3102ResVO.getProxySign().getUserId());
            responseModel.setProjectCode(tx3102ResVO.getProxySign().getProjectCode());
            responseModel.setCheckCode(tx3102ResVO.getProxySign().getCheckCode());
        }
        responseModel.setCreatedTime(new Date());
        anxinVerifyCaptchaResponseMapper.create(responseModel);
    }

    @Override
    public void insertBatchGenerateContractRequest(long businessId, Tx3202ReqVO tx3202ReqVO) {
        for (CreateContractVO contractVO : tx3202ReqVO.getCreateContracts()) {
            if (contractVO.getSignInfos() != null) {
                AnxinCreateContractBatchRequestModel requestModel = new AnxinCreateContractBatchRequestModel(businessId, tx3202ReqVO, contractVO);
                anxinCreateContractBatchRequestMapper.create(requestModel);
            }
        }
    }


    @Override
    public void insertBatchGenerateContractResponse(long businessId, String batchNo, Tx3202ResVO tx3202ResVO) {

        if (!tx3202ResVO.getHead().getRetCode().equals(AnxinRetCode.SUCCESS)) {
            AnxinCreateContractBatchResponseModel responseModel = new AnxinCreateContractBatchResponseModel(businessId, batchNo, tx3202ResVO.getHead().getRetCode(), tx3202ResVO.getHead().getRetMessage());
            anxinCreateContractBatchResponseMapper.create(responseModel);
        } else {
            for (CreateContractVO createContractVO : tx3202ResVO.getCreateContracts()) {
                AnxinCreateContractBatchResponseModel responseModel = new AnxinCreateContractBatchResponseModel(businessId, tx3202ResVO, createContractVO);
                anxinCreateContractBatchResponseMapper.create(responseModel);
            }
        }
    }


    @Override
    public void insertBatchQueryContractRequest(long businessId, Tx3211ReqVO tx3211ReqVO) {
        AnxinQueryContractBatchRequestModel requestModel = new AnxinQueryContractBatchRequestModel();
        requestModel.setBusinessId(businessId);
        requestModel.setBatchNo(tx3211ReqVO.getBatchNo());
        requestModel.setTxTime(tx3211ReqVO.getHead().getTxTime());
        requestModel.setCreatedTime(new Date());
        anxinQueryContractBatchRequestMapper.create(requestModel);
    }

    @Override
    public void insertBatchQueryContractResponse(long businessId, String batchNo, Tx3211ResVO tx3211ResVO) {

        if (tx3211ResVO != null && tx3211ResVO.getCreateContracts() != null) {
            // 请求成功返回
            for (CreateContractVO createContractVO : tx3211ResVO.getCreateContracts()) {
                AnxinQueryContractBatchResponseModel responseModel = new AnxinQueryContractBatchResponseModel(businessId, tx3211ResVO, createContractVO);
                anxinQueryContractBatchResponseMapper.create(responseModel);
            }
        } else {
            // 请求返回失败
            AnxinQueryContractBatchResponseModel responseModel = new AnxinQueryContractBatchResponseModel();
            responseModel.setBusinessId(businessId);
            responseModel.setBatchNo(batchNo);
            responseModel.setRetCode(tx3211ResVO.getHead().getRetCode());
            responseModel.setRetMessage(tx3211ResVO.getHead().getRetMessage());
            responseModel.setCreatedTime(new Date());
            anxinQueryContractBatchResponseMapper.create(responseModel);
        }
    }


}
