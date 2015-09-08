package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerUpdateProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerUpdateProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerUpdateProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class LoanServiceImpl implements LoanService {
    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;
    @Autowired
    private PaySyncClient paySyncClient;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private UserBillService userBillService;

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> createLoan(long loanId) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        LoanModel loanModel = loanMapper.findById(loanId);
        String loanerId = accountMapper.findByLoginName(loanModel.getLoanerLoginName()).getPayUserId();
        MerBindProjectRequestModel merBindProjectRequestModel = new MerBindProjectRequestModel(
                String.valueOf(loanModel.getId()),
                loanerId,
                String.valueOf(loanModel.getLoanAmount()),
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
        // 查找借款人
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            throw new PayException("loan is not exists [" + loanId + "]");
        }
        String loanerId = accountMapper.findByLoginName(loan.getLoanerLoginName()).getPayUserId();
        // 查找所有投资成功的记录
        List<InvestModel> successInvestList = investMapper.findSuccessInvests(loanId);
        // 计算投资总金额
        long investAmountTotal = computeInvestAmountTotal(successInvestList);
        if (investAmountTotal <= 0) {
            throw new PayException("amount should great than 0");
        }
        // 联动优势请求
        ProjectTransferResponseModel resp = doUmpayRequest(loanId, loanerId, investAmountTotal);

        if (resp.isSuccess()) {
            // 处理该标的的所有投资的账务信息
            processInvestForLoanOut(successInvestList);
            // 处理该标的帐务信息
            processLoanAccountForLoanOut(loan, investAmountTotal);
        }
        return resp;
    }

    private ProjectTransferResponseModel doUmpayRequest(long loanId, String umpayUserId, long amount) throws PayException {
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newLoanOutRequest(
                String.valueOf(loanId), String.valueOf(loanId), umpayUserId, String.valueOf(amount));
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
                continue;
            }
        }
    }

    private void processLoanAccountForLoanOut(LoanModel loan, long amount) {
        // 把借款转给借款人账户
        try {
            long orderId = loan.getId();
            userBillService.transferInBalance(loan.getLoanerLoginName(),
                    orderId, amount, UserBillBusinessType.LOAN_SUCCESS);
        } catch (Exception e) {
            logger.error("transferInBalance Fail while loan out, loan[" + loan.getId() + "]", e);
        }
    }

}
