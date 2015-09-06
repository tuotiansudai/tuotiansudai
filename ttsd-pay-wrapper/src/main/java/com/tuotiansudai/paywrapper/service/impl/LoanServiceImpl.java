package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
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
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

@Service
public class LoanServiceImpl implements LoanService {
    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;
    @Autowired
    private PaySyncClient paySyncClient;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IdGenerator idGenerator;

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
    public BaseDto<PayDataDto> loanOut(String loanId, String loginName, String amount) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        String loanerId = accountMapper.findByLoginName(loginName).getPayUserId();
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newLoanOutRequest(
                loanId, "01" + loanId, loanerId, amount);
        try {
            ProjectTransferResponseModel responseModel = paySyncClient.send(ProjectTransferMapper.class,
                    requestModel,
                    ProjectTransferResponseModel.class);
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
}
