package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
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
import com.tuotiansudai.utils.DateCompare;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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
        BigDecimal minInvestAmount = new BigDecimal(loanDto.getMinInvestAmount());
        BigDecimal maxInvestAmount = new BigDecimal(loanDto.getMaxInvestAmount());
        if (maxInvestAmount.compareTo(minInvestAmount) < 0){
            baseDto.setSuccess(false);
            dataDto.setStatus(false);
            return baseDto;
        }
        Integer result = DateCompare.compareDateStr(loanDto.getFundraisingStartTime(),
                DateCompare.YEAR_MONTH_DAY_HOUR_MINUTE,
                loanDto.getFundraisingEndTime(),
                DateCompare.YEAR_MONTH_DAY_HOUR_MINUTE);
        if (result == null || result == 1){
            baseDto.setSuccess(false);
            dataDto.setStatus(false);
            return baseDto;
        }
        String loanUserId = getLoginName(loanDto.getLoanLoginName());
        if (loanUserId == null) {
            baseDto.setSuccess(false);
            dataDto.setStatus(false);
            return baseDto;
        }
        String loanAgentId = getLoginName(loanDto.getAgentLoginName());
        if (loanAgentId == null){
            baseDto.setSuccess(false);
            dataDto.setStatus(false);
            return baseDto;
        }
        String projectId = String.valueOf(idGenerator.generate());/****标的号****/
        MerBindProjectRequestModel requestModel = new MerBindProjectRequestModel(
                loanUserId,
                loanDto.getLoanAmount(),
                projectId,
                loanDto.getProjectName());
        try {
            MerBindProjectResponseModel responseModel = paySyncClient.send(MerBindProjectMapper.class,
                    requestModel,
                    MerBindProjectResponseModel.class);
            if (responseModel.isSuccess()) {
                loanDto.setId(projectId);
                loanMapper.createLoan(new LoanModel(loanDto));
                List<LoanTitleModel> loanTitleModelList = loanDto.getLoanTitles();
                for (LoanTitleModel loanTitleModel : loanDto.getLoanTitles()){
                    loanTitleModel.setId(new BigInteger(String.valueOf(idGenerator.generate())));
                    loanTitleModel.setLoanId(new BigInteger(projectId));
                }
                loanTitleMapper.createLoanTitle(loanTitleModelList);
                dataDto.setStatus(responseModel.isSuccess());
                dataDto.setCode(responseModel.getRetCode());
                dataDto.setMessage(responseModel.getRetMsg());
            }else {
                baseDto.setSuccess(false);
                dataDto.setStatus(false);
            }
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
            dataDto.setStatus(false);
        } catch (ParseException e){
            logger.error("date format is not yyyy-MM-dd");
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
            dataDto.setStatus(false);
        }
        baseDto.setData(dataDto);
        return baseDto;
    }

    public String getLoginName(String loginName){
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        String loanUserId = null;
        if (accountModel != null) {
            loanUserId = accountModel.getPayUserId();
        }
        return loanUserId;
    }
}
