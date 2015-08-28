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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService{
    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);
    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private com.tuotiansudai.service.LoanService loanService;

    public BaseDto<PayDataDto> createLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
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
            if (responseModel.isSuccess()){
                loanService.updateLoan(loanDto);
            }
            dataDto.setStatus(responseModel.isSuccess());
            dataDto.setCode(responseModel.getRetCode());
            dataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            dataDto.setStatus(false);
            logger.error(e.getLocalizedMessage(),e);
        }
        baseDto.setData(dataDto);
        return baseDto;
    }
}
