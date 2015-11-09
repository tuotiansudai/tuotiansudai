package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestSmsNotifyDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerUpdateProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerUpdateProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerUpdateProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.paywrapper.service.ReferrerRewardService;
import com.tuotiansudai.paywrapper.service.RepayGeneratorService;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.SendCloudMailUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LoanServiceImpl implements LoanService {

    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RepayGeneratorService repayGeneratorService;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private SendCloudMailUtil sendCloudMailUtil;

    @Autowired
    private ReferrerRewardService referrerRewardService;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> createLoan(long loanId) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        LoanModel loanModel = loanMapper.findById(loanId);
        String loanerId = accountMapper.findByLoginName(loanModel.getLoanerLoginName()).getPayUserId();
        MerBindProjectRequestModel merBindProjectRequestModel = new MerBindProjectRequestModel(
                loanerId,
                String.valueOf(loanModel.getLoanAmount()),
                String.valueOf(loanModel.getId()),
                loanModel.getName()
        );
        try {
            MerBindProjectResponseModel responseModel = paySyncClient.send(MerBindProjectMapper.class,
                    merBindProjectRequestModel,
                    MerBindProjectResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            payDataDto.setStatus(false);
            logger.error(e.getLocalizedMessage(), e);
        }
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> updateLoanStatus(long loanId, LoanStatus loanStatus) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        LoanModel loanModel = loanMapper.findById(loanId);
        MerUpdateProjectRequestModel merUpdateProjectRequestModel = new MerUpdateProjectRequestModel(
                loanModel.getLoanAmount(),
                loanModel.getId(),
                loanModel.getName(),
                loanStatus.getCode(),
                new SimpleDateFormat("yyyyMMdd").format(loanModel.getFundraisingEndTime())
        );
        try {
            MerUpdateProjectResponseModel responseModel = paySyncClient.send(MerUpdateProjectMapper.class,
                    merUpdateProjectRequestModel,
                    MerUpdateProjectResponseModel.class);
            if (responseModel.isSuccess()) {
                loanMapper.updateStatus(loanId, loanStatus);
            }
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            payDataDto.setStatus(false);
            logger.error(e.getLocalizedMessage(), e);
        }
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> loanOut(long loanId) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        try {
            ProjectTransferResponseModel umPayReturn = doLoanOut(loanId);
            payDataDto.setStatus(umPayReturn.isSuccess());
            payDataDto.setCode(umPayReturn.getRetCode());
            payDataDto.setMessage(umPayReturn.getRetMsg());
        } catch (PayException e) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

    private ProjectTransferResponseModel doLoanOut(long loanId) throws PayException {
        // 预处理等待状态的投资记录，检查是否存在等待状态的投资记录
        proProcessWaitingInvest(loanId);

        // 查找借款人
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            throw new PayException("loan is not exists [" + loanId + "]");
        }
        if (LoanStatus.RECHECK != loan.getStatus()){
            throw new PayException("loan is not ready for recheck [" + loanId + "]");
        }
        String loanerPayUserId = accountMapper.findByLoginName(loan.getLoanerLoginName()).getPayUserId();


        // 查找所有投资成功的记录
        List<InvestModel> successInvestList = investMapper.findSuccessInvestsByLoanId(loanId);
        logger.debug("标的放款：查找到" + successInvestList.size() + "条成功的投资，标的ID:" + loanId);

        // 计算投资总金额
        long investAmountTotal = computeInvestAmountTotal(successInvestList);
        if (investAmountTotal <= 0) {
            throw new PayException("invest amount should great than 0");
        }

        logger.debug("标的放款：发起联动优势放款请求，标的ID:" + loanId + "，借款人:" + loanerPayUserId + "，放款金额:" + investAmountTotal);
        ProjectTransferResponseModel resp = doPayRequest(loanId, loanerPayUserId, investAmountTotal);

        if (resp.isSuccess()) {
            //TODO : 如果下面这些方法有任何一个出现异常，如何记录？
            logger.debug("标的放款：更新标的状态，标的ID:" + loanId);
            processLoanStatusForLoanOut(loan);

            logger.debug("标的放款：处理该标的的所有投资的账务信息，标的ID:" + loanId);
            processInvestForLoanOut(successInvestList);

            logger.debug("标的放款：把借款转给借款人账户，标的ID:" + loanId);
            processLoanAccountForLoanOut(loan, investAmountTotal);

            logger.debug("标的放款：生成还款计划，标的ID:" + loanId);
            repayGeneratorService.generateRepay(loanId);

            logger.debug("标的放款：处理推荐人奖励，标的ID:" + loanId);
            referrerRewardService.rewardReferrer(loan, successInvestList);

            logger.debug("标的放款：处理短信和邮件通知，标的ID:" + loanId);
            processNotifyForLoanOut(loanId);
        }
        return resp;
    }

    private void proProcessWaitingInvest(long loanId) throws PayException {
        // 获取联动优势投资订单的有效时间点（在此时间之前的waiting记录将被清理，如存在在此时间之后的waiting记录，则暂时不允许放款）
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -UmpayConstants.TIMEOUT_IN_SECOND_PROJECT_TRANSFER);
        Date validInvestTime = cal.getTime();

        // 检查是否存在未处理完成的投资记录
        int waitingInvestCount = investMapper.findWaitingInvestCountAfter(loanId, validInvestTime);
        if (waitingInvestCount > 0) {
            throw new PayException("exist waiting invest on loan[" + loanId + "]");
        }

        // 将已失效的投资记录状态置为失败
        investMapper.cleanWaitingInvestBefore(loanId, validInvestTime);
    }

    private ProjectTransferResponseModel doPayRequest(long loanId, String payUserId, long amount) throws PayException {
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newLoanOutRequest(
                String.valueOf(loanId), String.valueOf(loanId), payUserId, String.valueOf(amount));
        ProjectTransferResponseModel responseModel = paySyncClient.send(
                ProjectTransferMapper.class,
                requestModel,
                ProjectTransferResponseModel.class);
        return responseModel;
    }

    private long computeInvestAmountTotal(List<InvestModel> investList) {
        long amount = 0L;
        for (InvestModel investModel : investList) {
            amount += investModel.getAmount();
        }
        return amount;
    }

    private void processInvestForLoanOut(List<InvestModel> investList) {
        if (investList == null) {
            return;
        }
        for (InvestModel invest : investList) {
            try {
                userBillService.transferOutFreeze(invest.getLoginName(),
                        invest.getId(), invest.getAmount(), UserBillBusinessType.LOAN_SUCCESS);
            } catch (Exception e) {
                logger.error("transferOutFreeze Fail while loan out, invest [" + invest.getId() + "]", e);
            }
        }
    }

    // 把借款转给借款人账户
    private void processLoanAccountForLoanOut(LoanModel loan, long amount) {
        try {
            long orderId = loan.getId();
            userBillService.transferInBalance(loan.getLoanerLoginName(),
                    orderId, amount, UserBillBusinessType.LOAN_SUCCESS);
        } catch (Exception e) {
            logger.error("transferInBalance Fail while loan out, loan[" + loan.getId() + "]", e);
        }
    }

    private void processNotifyForLoanOut(long loanId) {
        List<InvestNotifyInfo> notifyInfos = investMapper.findSuccessInvestMobileEmailAndAmount(loanId);
        logger.debug(MessageFormat.format("标的: {0} 放款短信通知", loanId));
        notifyInvestorsLoanOutSuccessfulBySMS(notifyInfos);
        logger.debug(MessageFormat.format("标的: {0} 放款邮件通知", loanId));
        notifyInvestorsLoanOutSuccessfulByEmail(notifyInfos);
    }

    private void notifyInvestorsLoanOutSuccessfulBySMS(List<InvestNotifyInfo> notifyInfos) {
        for (InvestNotifyInfo notifyInfo : notifyInfos) {
            InvestSmsNotifyDto dto = new InvestSmsNotifyDto(notifyInfo);
            smsWrapperClient.sendInvestNotify(dto);
        }
    }

    private void notifyInvestorsLoanOutSuccessfulByEmail(List<InvestNotifyInfo> notifyInfos) {
        for (InvestNotifyInfo notifyInfo : notifyInfos) {
            Map<String, String> emailParameters = Maps.newHashMap(new ImmutableMap.Builder<String, String>()
                    .put("loanName", notifyInfo.getLoanName())
                    .put("money", AmountUtil.convertCentToString(notifyInfo.getAmount()))
                    .build());
            String userEmail = notifyInfo.getEmail();
            if (StringUtils.isNotEmpty(userEmail)) {
                sendCloudMailUtil.sendMailByLoanOut(userEmail, emailParameters);
            }
        }
    }

    private void processLoanStatusForLoanOut(LoanModel loan) {
        BaseDto<PayDataDto> dto = updateLoanStatus(loan.getId(), LoanStatus.REPAYING);
        if(dto.getData().getStatus()){
            LoanModel loan4Update = new LoanModel();
            loan4Update.setId(loan.getId());
            loan4Update.setRecheckTime(new Date());
            loanMapper.update(loan4Update);
        }else{
            logger.error("update loan status failed : "+dto.getData().getMessage());
        }
    }
}
