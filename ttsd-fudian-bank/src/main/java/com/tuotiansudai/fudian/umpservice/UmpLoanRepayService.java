package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.callback.RepayNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.callback.TransferNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.ProjectTransferRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.TransferRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.TransferResponseModel;
import com.tuotiansudai.fudian.umpdto.*;
import com.tuotiansudai.fudian.umpmessage.*;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.fudian.util.UmpUtils;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UmpLoanRepayService {

    private final static Logger logger = LoggerFactory.getLogger(UmpLoanRepayService.class);

    private final static String UMP_REPAY_DATA_KEY = "UMP_REPAY_DATA_KEY";

    private final static String UMP_REPAY_PAYBACK_DATA_KEY = "UMP_REPAY_PAYBACK_DATA_KEY";

    private final static String UMP_COUPON_REPAY_DATA_KEY = "UMP_COUPON_REPAY_DATA_KEY";

    private final static String UMP_EXTRA_REPAY_DATA_KEY = "UMP_EXTRA_REPAY_DATA_KEY";

    private final static String UMP_REPAY_FEE_DATA_KEY = "UMP_REPAY_FEE_DATA_KEY";

    private final static String UMP_REPAY_PAYBACK_QUEUE = "UMP_REPAY_PAYBACK_QUEUE";

    private final static String UMP_COUPON_REPAY_QUEUE = "UMP_COUPON_REPAY_QUEUE";

    private final static String UMP_EXTRA_REPAY_QUEUE = "UMP_EXTRA_REPAY_QUEUE";

    private final static String UMP_REPAY_FEE_QUEUE = "UMP_REPAY_FEE_QUEUE";

    private final static String ORDER_ID_TEMPLATE = "{0}X{1}";

    private final InsertRequestMapper insertRequestMapper;

    private final UpdateRequestMapper updateRequestMapper;

    private final InsertResponseMapper insertResponseMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    private final MessageQueueClient messageQueueClient;

    private final UmpUtils umpUtils;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    private UmpLoanRepayService(InsertRequestMapper insertRequestMapper, UpdateRequestMapper updateRequestMapper, InsertResponseMapper insertResponseMapper, InsertNotifyMapper insertNotifyMapper, RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, MessageQueueClient messageQueueClient, UmpUtils umpUtils) {
        this.insertRequestMapper = insertRequestMapper;
        this.updateRequestMapper = updateRequestMapper;
        this.insertResponseMapper = insertResponseMapper;
        this.insertNotifyMapper = insertNotifyMapper;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.messageQueueClient = messageQueueClient;
        this.umpUtils = umpUtils;
    }

    public ProjectTransferRequestModel loanRepay(UmpLoanRepayDto dto) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();

        operations.put(UMP_REPAY_DATA_KEY, String.valueOf(dto.getLoanRepayId()), gson.toJson(dto));

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newRepayRequest(String.valueOf(dto.getLoanId()),
                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(dto.getLoanRepayId()), String.valueOf(new Date().getTime())),
                dto.getPayUserId(),
                String.valueOf(dto.getAmount()));

        umpUtils.sign(requestModel);

        insertRequestMapper.insertProjectTransfer(requestModel);

        if (requestModel.getField().isEmpty()) {
            logger.error("[UMP LOAN REPAY] failed to sign, data: {}", requestModel);
            return null;
        }

        return requestModel;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString) {
        ProjectTransferNotifyRequestModel projectTransferNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, ProjectTransferNotifyRequestModel.class);
        if (Strings.isNullOrEmpty(projectTransferNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyProjectTransfer(projectTransferNotifyModel);

        if (!projectTransferNotifyModel.isSuccess()) {
            logger.error("[UMP LOAN REPAY] failed to loan repay, orderId: {}, retCode: {}, message: {}", projectTransferNotifyModel.getOrderId(),
                    projectTransferNotifyModel.getRetCode(),
                    projectTransferNotifyModel.getRetMsg());
            return projectTransferNotifyModel.getResponseData();
        }

        try {
            long loanRepayId = Long.parseLong(projectTransferNotifyModel.getOrderId().split("X")[0]);
            HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

            String repayData = hashOperations.get(UMP_REPAY_DATA_KEY, String.valueOf(loanRepayId));

            if (hashOperations.delete(UMP_REPAY_DATA_KEY, String.valueOf(loanRepayId)) > 0) {
                UmpLoanRepayDto umpLoanRepayDto = gson.fromJson(repayData, UmpLoanRepayDto.class);

                ListOperations<String, String> listOperations = redisTemplate.opsForList();

                if (umpLoanRepayDto.getUmpInvestRepayDtoList() != null && umpLoanRepayDto.getUmpInvestRepayDtoList().size() > 0) {
                    listOperations.leftPushAll(UMP_REPAY_PAYBACK_QUEUE, umpLoanRepayDto.getUmpInvestRepayDtoList().stream().map(gson::toJson).collect(Collectors.toList()));
                    umpLoanRepayDto.getUmpInvestRepayDtoList().forEach(dto -> hashOperations.put(UMP_REPAY_PAYBACK_DATA_KEY, String.valueOf(dto.getInvestRepayId()), gson.toJson(dto)));
                }

                if (umpLoanRepayDto.getUmpCouponRepayDtoList() != null && umpLoanRepayDto.getUmpCouponRepayDtoList().size() > 0) {
                    listOperations.leftPushAll(UMP_COUPON_REPAY_QUEUE, umpLoanRepayDto.getUmpCouponRepayDtoList().stream().map(gson::toJson).collect(Collectors.toList()));
                    umpLoanRepayDto.getUmpCouponRepayDtoList().forEach(dto -> hashOperations.put(UMP_COUPON_REPAY_DATA_KEY, String.valueOf(dto.getCouponRepayId()), gson.toJson(dto)));
                }

                if (umpLoanRepayDto.getUmpExtraRateRepayDtoList() != null && umpLoanRepayDto.getUmpExtraRateRepayDtoList().size() > 0) {
                    listOperations.leftPushAll(UMP_EXTRA_REPAY_QUEUE, umpLoanRepayDto.getUmpExtraRateRepayDtoList().stream().map(gson::toJson).collect(Collectors.toList()));
                    umpLoanRepayDto.getUmpExtraRateRepayDtoList().forEach(dto -> hashOperations.put(UMP_EXTRA_REPAY_DATA_KEY, String.valueOf(dto.getInvestExtraRateId()), gson.toJson(dto)));
                }

                if (umpLoanRepayDto.getUmpRepayFeeDto().getFee() > 0) {
                    listOperations.leftPush(UMP_REPAY_FEE_QUEUE, gson.toJson(umpLoanRepayDto.getUmpRepayFeeDto()));
                    hashOperations.put(UMP_REPAY_FEE_DATA_KEY, String.valueOf(umpLoanRepayDto.getUmpRepayFeeDto().getLoanRepayId()), gson.toJson(umpLoanRepayDto.getUmpRepayFeeDto()));
                }

                messageQueueClient.sendMessage(MessageQueue.UmpLoanRepay_Success, new UmpLoanRepayMessage(umpLoanRepayDto.getLoanId(), umpLoanRepayDto.getLoanRepayId(), umpLoanRepayDto.getAmount(), umpLoanRepayDto.getLoginName(), umpLoanRepayDto.getIsNormalRepay()));
            }

            logger.info("[UMP LOAN REPAY] success to loan repay callback, repay data is {}", repayData);
        } catch (Exception ex) {
            logger.error("[UMP LOAN REPAY] fail to loan repay callback, ump query string: {}", queryString);
        }

        return projectTransferNotifyModel.getResponseData();
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void repayPaybackSchedule() {
        RLock lock = redissonClient.getLock("UMP_REPAY_PAYBACK_QUEUE_LOCK");
        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        if (lock.tryLock()) {
            try {
                Long size = listOperations.size(UMP_REPAY_PAYBACK_QUEUE);
                size = size == null ? 0 : size;
                for (int index = 0; index < size; index++) {
                    try {
                        String value = listOperations.rightPop(UMP_REPAY_PAYBACK_QUEUE);
                        logger.info("[UMP invest repay] pop repay payback data: {}", value);

                        UmpInvestRepayDto umpInvestRepayDto = gson.fromJson(value, UmpInvestRepayDto.class);

                        long amount = umpInvestRepayDto.getCorpus() + umpInvestRepayDto.getInterest() + umpInvestRepayDto.getDefaultFee() - umpInvestRepayDto.getFee();

                        if (amount == 0) {
                            messageQueueClient.sendMessage(MessageQueue.UmpRepayPayback_Success, new UmpRepayPaybackMessage(
                                    umpInvestRepayDto.getLoginName(),
                                    umpInvestRepayDto.getLoanId(),
                                    umpInvestRepayDto.getInvestId(),
                                    umpInvestRepayDto.getInvestRepayId(),
                                    umpInvestRepayDto.getCorpus(),
                                    umpInvestRepayDto.getInterest(),
                                    umpInvestRepayDto.getFee(),
                                    umpInvestRepayDto.getDefaultFee(),
                                    umpInvestRepayDto.getIsNormalRepay()));

                            continue;
                        }

                        ProjectTransferRequestModel model = ProjectTransferRequestModel.newRepayPaybackRequest(
                                String.valueOf(umpInvestRepayDto.getLoanId()),
                                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(umpInvestRepayDto.getInvestRepayId()), String.valueOf(new Date().getTime())),
                                umpInvestRepayDto.getPayUserId(),
                                String.valueOf(amount));

                        umpUtils.sign(model);

                        insertRequestMapper.insertProjectTransfer(model);

                        if (model.getField().isEmpty()) {
                            logger.error("[UMP invest repay] failed to sign, data: {}", value);
                            continue;
                        }
                        updateRequestMapper.updateProjectTransfer(SyncRequestStatus.SENT, model.getId());
                        String responseBody = umpUtils.send(model.getRequestUrl(), model.getField());
                        if (responseBody != null) {
                            ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
                            umpUtils.generateResponse(model.getId(), responseBody, responseModel);
                            updateRequestMapper.updateProjectTransfer(responseModel.isSuccess() ? SyncRequestStatus.SUCCESS : SyncRequestStatus.FAILURE, model.getId());
                            insertResponseMapper.insertProjectTransfer(responseModel);
                            logger.info("[UMP invest repay] sent invest repay request, payback data: {}, status: {}", value, responseModel.isSuccess());
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getLocalizedMessage(), ex);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public String repayPaybackNotifyCallBack(Map<String, String> paramsMap, String queryString) {
        RepayNotifyRequestModel repayNotifyRequestModel = umpUtils.parseCallbackRequest(paramsMap, queryString, RepayNotifyRequestModel.class);
        if (Strings.isNullOrEmpty(repayNotifyRequestModel.getResponseData())) {
            return null;
        }

        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String orderId = repayNotifyRequestModel.getOrderId().split("X")[0];

        String data = operations.get(UMP_REPAY_PAYBACK_DATA_KEY, orderId);

        if (Strings.isNullOrEmpty(data)) {
            return repayNotifyRequestModel.getResponseData();
        }

        logger.info("[UMP invest repay notify] orderId: {}, data: {}, status: {}", orderId, data, repayNotifyRequestModel.isSuccess());

        if (repayNotifyRequestModel.isSuccess() && operations.delete(UMP_REPAY_PAYBACK_DATA_KEY, orderId) > 0) {
            UmpInvestRepayDto umpInvestRepayDto = gson.fromJson(data, UmpInvestRepayDto.class);
            if (umpInvestRepayDto.getIsNormalRepay()) {
                insertNotifyMapper.insertNotifyNormalRepay(repayNotifyRequestModel);
            } else {
                insertNotifyMapper.insertNotifyAdvanceRepay(repayNotifyRequestModel);
            }
            messageQueueClient.sendMessage(MessageQueue.UmpRepayPayback_Success, new UmpRepayPaybackMessage(
                    umpInvestRepayDto.getLoginName(),
                    umpInvestRepayDto.getLoanId(),
                    umpInvestRepayDto.getInvestId(),
                    umpInvestRepayDto.getInvestRepayId(),
                    umpInvestRepayDto.getCorpus(),
                    umpInvestRepayDto.getInterest(),
                    umpInvestRepayDto.getFee(),
                    umpInvestRepayDto.getDefaultFee(),
                    umpInvestRepayDto.getIsNormalRepay()));
        }

        return repayNotifyRequestModel.getResponseData();
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void couponRepaySchedule() {
        RLock lock = redissonClient.getLock("UMP_COUPON_REPAY_QUEUE_LOCK");
        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        if (lock.tryLock()) {
            try {
                Long size = listOperations.size(UMP_COUPON_REPAY_QUEUE);
                size = size == null ? 0 : size;
                for (int index = 0; index < size; index++) {
                    try {
                        String value = listOperations.rightPop(UMP_COUPON_REPAY_QUEUE);
                        logger.info("[UMP coupon repay] pop coupon repay data: {}", value);

                        UmpCouponRepayDto umpCouponRepayDto = gson.fromJson(value, UmpCouponRepayDto.class);
                        long amount = umpCouponRepayDto.getInterest() - umpCouponRepayDto.getFee();

                        if (amount == 0) {
                            messageQueueClient.sendMessage(MessageQueue.UmpCouponRepay_Success, new UmpCouponRepayMessage(
                                    umpCouponRepayDto.getLoginName(),
                                    umpCouponRepayDto.getCouponRepayId(),
                                    umpCouponRepayDto.getInterest(),
                                    umpCouponRepayDto.getFee(),
                                    umpCouponRepayDto.getIsNormalRepay()));
                            continue;
                        }

                        TransferRequestModel model = TransferRequestModel.newCouponRepayRequest(
                                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(umpCouponRepayDto.getCouponRepayId()), String.valueOf(new Date().getTime())),
                                umpCouponRepayDto.getPayUserId(),
                                String.valueOf(amount));

                        umpUtils.sign(model);

                        insertRequestMapper.insertCouponRepay(model);

                        if (model.getField().isEmpty()) {
                            logger.error("[UMP coupon repay] failed to sign, data: {}", value);
                            continue;
                        }

                        updateRequestMapper.updateCouponRepay(SyncRequestStatus.SENT, model.getId());
                        String responseBody = umpUtils.send(model.getRequestUrl(), model.getField());
                        if (responseBody != null) {
                            TransferResponseModel responseModel = new TransferResponseModel();
                            umpUtils.generateResponse(model.getId(), responseBody, responseModel);
                            updateRequestMapper.updateCouponRepay(responseModel.isSuccess() ? SyncRequestStatus.SUCCESS : SyncRequestStatus.FAILURE, model.getId());
                            insertResponseMapper.insertResponseCouponRepay(responseModel);
                            logger.info("[UMP coupon repay] sent coupon repay request, payback data: {}, status: {}", value, responseModel.isSuccess());
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getLocalizedMessage(), ex);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public String couponRepayNotifyCallBack(Map<String, String> paramsMap, String queryString) {
        TransferNotifyRequestModel transferNotifyRequestModel = umpUtils.parseCallbackRequest(paramsMap, queryString, TransferNotifyRequestModel.class);
        if (Strings.isNullOrEmpty(transferNotifyRequestModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyCouponRepay(transferNotifyRequestModel);

        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String orderId = transferNotifyRequestModel.getOrderId().split("X")[0];

        String data = operations.get(UMP_COUPON_REPAY_DATA_KEY, orderId);

        if (Strings.isNullOrEmpty(data)) {
            return transferNotifyRequestModel.getResponseData();
        }

        logger.info("[UMP coupon repay notify] orderId: {}, data: {}, status: {}", orderId, data, transferNotifyRequestModel.isSuccess());

        if (transferNotifyRequestModel.isSuccess() && operations.delete(UMP_COUPON_REPAY_DATA_KEY, orderId) > 0) {
            UmpCouponRepayDto umpCouponRepayDto = gson.fromJson(data, UmpCouponRepayDto.class);
            messageQueueClient.sendMessage(MessageQueue.UmpCouponRepay_Success, new UmpCouponRepayMessage(
                    umpCouponRepayDto.getLoginName(),
                    umpCouponRepayDto.getCouponRepayId(),
                    umpCouponRepayDto.getInterest(),
                    umpCouponRepayDto.getFee(),
                    umpCouponRepayDto.getIsNormalRepay()));
        }

        return transferNotifyRequestModel.getResponseData();
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void extraRepaySchedule() {
        RLock lock = redissonClient.getLock("UMP_EXTRA_REPAY_QUEUE_LOCK");
        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        if (lock.tryLock()) {
            try {
                Long size = listOperations.size(UMP_EXTRA_REPAY_QUEUE);
                size = size == null ? 0 : size;
                for (int index = 0; index < size; index++) {
                    try {
                        String value = listOperations.rightPop(UMP_EXTRA_REPAY_QUEUE);
                        logger.info("[UMP extra repay] pop extra repay data: {}", value);

                        UmpExtraRateRepayDto umpExtraRateRepayDto = gson.fromJson(value, UmpExtraRateRepayDto.class);
                        long amount = umpExtraRateRepayDto.getInterest() - umpExtraRateRepayDto.getFee();

                        if (amount == 0) {
                            messageQueueClient.sendMessage(MessageQueue.UmpExtraRepay_Success, new UmpExtraRepayMessage(
                                    umpExtraRateRepayDto.getLoginName(),
                                    umpExtraRateRepayDto.getInvestExtraRateId(),
                                    umpExtraRateRepayDto.getInterest(),
                                    umpExtraRateRepayDto.getFee()));

                            continue;
                        }

                        TransferRequestModel model = TransferRequestModel.newExtraRateRequest(
                                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(umpExtraRateRepayDto.getInvestExtraRateId()), String.valueOf(new Date().getTime())),
                                umpExtraRateRepayDto.getPayUserId(),
                                String.valueOf(amount));

                        umpUtils.sign(model);

                        insertRequestMapper.insertExtraRate(model);

                        if (model.getField().isEmpty()) {
                            logger.error("[UMP extra repay] failed to sign, data: {}", value);
                            continue;
                        }

                        updateRequestMapper.updateExtraRate(SyncRequestStatus.SENT, model.getId());
                        String responseBody = umpUtils.send(model.getRequestUrl(), model.getField());
                        if (responseBody != null) {
                            TransferResponseModel responseModel = new TransferResponseModel();
                            umpUtils.generateResponse(model.getId(), responseBody, responseModel);
                            updateRequestMapper.updateExtraRate(responseModel.isSuccess() ? SyncRequestStatus.SUCCESS : SyncRequestStatus.FAILURE, model.getId());
                            insertResponseMapper.insertResponseExtraRate(responseModel);
                            logger.info("[UMP extra repay] sent extra repay request, payback data: {}, status: {}", value, responseModel.isSuccess());
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getLocalizedMessage(), ex);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public String extraRepayNotifyCallBack(Map<String, String> paramsMap, String queryString) {
        TransferNotifyRequestModel transferNotifyRequestModel = umpUtils.parseCallbackRequest(paramsMap, queryString, TransferNotifyRequestModel.class);
        if (Strings.isNullOrEmpty(transferNotifyRequestModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyExtraRate(transferNotifyRequestModel);

        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String orderId = transferNotifyRequestModel.getOrderId().split("X")[0];

        String data = operations.get(UMP_EXTRA_REPAY_DATA_KEY, orderId);

        if (Strings.isNullOrEmpty(data)) {
            return transferNotifyRequestModel.getResponseData();
        }

        logger.info("[UMP extra repay notify] orderId: {}, data: {}, status: {}", orderId, data, transferNotifyRequestModel.isSuccess());

        if (transferNotifyRequestModel.isSuccess() && operations.delete(UMP_EXTRA_REPAY_DATA_KEY, orderId) > 0) {
            UmpExtraRateRepayDto umpExtraRateRepayDto = gson.fromJson(data, UmpExtraRateRepayDto.class);
            messageQueueClient.sendMessage(MessageQueue.UmpExtraRepay_Success, new UmpExtraRepayMessage(
                    umpExtraRateRepayDto.getLoginName(),
                    umpExtraRateRepayDto.getInvestExtraRateId(),
                    umpExtraRateRepayDto.getInterest(),
                    umpExtraRateRepayDto.getFee()));
        }

        return transferNotifyRequestModel.getResponseData();
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void repayFeeSchedule() {
        RLock lock = redissonClient.getLock("UMP_REPAY_FEE_QUEUE_LOCK");
        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        if (lock.tryLock()) {
            try {
                Long size = listOperations.size(UMP_REPAY_FEE_QUEUE);
                size = size == null ? 0 : size;
                for (int index = 0; index < size; index++) {
                    try {
                        String value = listOperations.rightPop(UMP_REPAY_FEE_QUEUE);
                        logger.info("[UMP fee repay] pop repay fee data: {}", value);

                        UmpRepayFeeDto umpRepayFeeDto = gson.fromJson(value, UmpRepayFeeDto.class);

                        if (umpRepayFeeDto.getFee() == 0) {
                            messageQueueClient.sendMessage(MessageQueue.UmpRepayFee_Success, new UmpRepayFeeMessage(
                                    umpRepayFeeDto.getLoanId(),
                                    umpRepayFeeDto.getLoanRepayId(),
                                    umpRepayFeeDto.getFee()));

                            continue;
                        }

                        ProjectTransferRequestModel model = ProjectTransferRequestModel.newRepayFeeRequest(
                                String.valueOf(umpRepayFeeDto.getLoanId()),
                                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(umpRepayFeeDto.getLoanRepayId()), String.valueOf(new Date().getTime())),
                                String.valueOf(umpRepayFeeDto.getFee()));

                        umpUtils.sign(model);

                        insertRequestMapper.insertProjectTransfer(model);

                        if (model.getField().isEmpty()) {
                            logger.error("[UMP fee repay] failed to sign, data: {}", value);
                            continue;
                        }

                        updateRequestMapper.updateProjectTransfer(SyncRequestStatus.SENT, model.getId());
                        String responseBody = umpUtils.send(model.getRequestUrl(), model.getField());
                        if (responseBody != null) {
                            ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
                            umpUtils.generateResponse(model.getId(), responseBody, responseModel);
                            updateRequestMapper.updateProjectTransfer(responseModel.isSuccess() ? SyncRequestStatus.SUCCESS : SyncRequestStatus.FAILURE, model.getId());
                            insertResponseMapper.insertProjectTransfer(responseModel);
                            logger.info("[UMP fee repay] sent fee repay request, payback data: {}, status: {}", value, responseModel.isSuccess());
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getLocalizedMessage(), ex);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public String repayFeeNotifyCallBack(Map<String, String> paramsMap, String queryString) {
        ProjectTransferNotifyRequestModel transferNotifyRequestModel = umpUtils.parseCallbackRequest(paramsMap, queryString, ProjectTransferNotifyRequestModel.class);
        if (Strings.isNullOrEmpty(transferNotifyRequestModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyProjectTransfer(transferNotifyRequestModel);

        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String orderId = transferNotifyRequestModel.getOrderId().split("X")[0];

        String data = operations.get(UMP_REPAY_FEE_DATA_KEY, orderId);

        if (Strings.isNullOrEmpty(data)) {
            return transferNotifyRequestModel.getResponseData();
        }

        logger.info("[UMP repay fee notify] orderId: {}, data: {}, status: {}", orderId, data, transferNotifyRequestModel.isSuccess());

        if (transferNotifyRequestModel.isSuccess() && operations.delete(UMP_REPAY_FEE_DATA_KEY, orderId) > 0) {
            UmpRepayFeeDto umpRepayFeeDto = gson.fromJson(data, UmpRepayFeeDto.class);
            messageQueueClient.sendMessage(MessageQueue.UmpRepayFee_Success, new UmpRepayFeeMessage(
                    umpRepayFeeDto.getLoanId(),
                    umpRepayFeeDto.getLoanRepayId(),
                    umpRepayFeeDto.getFee()));
        }

        return transferNotifyRequestModel.getResponseData();
    }
}