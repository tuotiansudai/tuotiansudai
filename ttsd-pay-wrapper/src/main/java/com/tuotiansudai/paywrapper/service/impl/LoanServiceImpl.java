package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanTitleRelationModel;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;
    @Autowired
    private PaySyncClient paySyncClient;

    public BaseDto<PayDataDto> createLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        if (loanMapper.findById(loanDto.getId()) == null){
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        MerBindProjectRequestModel merBindProjectRequestModel = new MerBindProjectRequestModel(
                loanDto.getLoanerLoginName(),
                loanDto.getLoanAmount(),
                String.valueOf(loanDto.getId()),
                loanDto.getProjectName()
        );
        try {
            MerBindProjectResponseModel responseModel = paySyncClient.send(MerBindProjectMapper.class,
                    merBindProjectRequestModel,
                    MerBindProjectResponseModel.class);
            if (responseModel.isSuccess()) {
                LoanModel loanModel = new LoanModel(loanDto);
                loanModel.setStatus(loanDto.getStatus());
                loanMapper.update(loanModel);
                if (loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() > 0){
                    loanTitleRelationMapper.delete(loanDto.getId());
                }
                List<LoanTitleRelationModel> loanTitleRelationModels = loanDto.getLoanTitles();
                if (loanTitleRelationModels != null && loanTitleRelationModels.size() > 0){
                    loanTitleRelationMapper.create(loanTitleRelationModels);
                }
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
}
