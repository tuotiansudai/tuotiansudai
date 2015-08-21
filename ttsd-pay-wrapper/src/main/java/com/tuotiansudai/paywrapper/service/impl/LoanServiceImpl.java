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
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    IdGenerator idGenerator;
    /**
     * @param loanDto
     * @return
     * @function 创建标的
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto createLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto();
        PayDataDto dataDto = new PayDataDto();
        String loanUserId = getLoginName(loanDto);
        if (loanUserId == null) {
            dataDto.setStatus(false);
            return baseDto;
        }
        String loanAgentId = getLoginName(loanDto);
        if (loanAgentId != null){
            dataDto.setStatus(false);
            return baseDto;
        }
        MerBindProjectRequestModel requestModel = new MerBindProjectRequestModel();
        requestModel.setLoanUserId(loanUserId);
        requestModel.setProjectAmount(loanDto.getLoanAmount());
        String projectId = String.valueOf(idGenerator.generate());/****标的号****/
        requestModel.setProjectId(projectId);
        requestModel.setProjectName(loanDto.getProjectName());
        try {
            MerBindProjectResponseModel responseModel = paySyncClient.send(MerBindProjectMapper.class,
                    requestModel,
                    MerBindProjectResponseModel.class);
            if (responseModel.isSuccess()) {
                LoanModel loanModel = null;
                try {
                    loanModel = new LoanModel(loanDto);
                } catch (ParseException e) {
                    logger.error("date format is not yyyy-MM-dd");
                    logger.error(e.getLocalizedMessage(),e);
                }
                loanModel.setId(projectId);
                loanMapper.createLoan(loanModel);
                List<LoanTitleModel> loanTitleModelList = new ArrayList<LoanTitleModel>();
                for (LoanTitleModel loanTitleModel : loanDto.getLoanTitles()){
                    loanTitleModel.setLoanId(new BigInteger(projectId));
                    loanTitleModelList.add(loanTitleModel);
                }
                loanTitleMapper.createLoanTitle(loanTitleModelList);
                dataDto.setStatus(responseModel.isSuccess());
                dataDto.setCode(responseModel.getRetCode());
                dataDto.setMessage(responseModel.getRetMsg());
            }
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
            dataDto.setStatus(false);
        }
        return baseDto;
    }

    public String getLoginName(LoanDto loanDto){
        AccountModel accountModel = accountMapper.findByLoginName(loanDto.getLoanLoginName());
        String loanUserId = null;
        if (accountModel != null) {
            loanUserId = accountModel.getPayUserId();
        }
        return loanUserId;
    }
}
