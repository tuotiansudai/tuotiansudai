package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TitleMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.TitleModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

public class LoanServiceImpl implements LoanService {
    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TitleMapper titleMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

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
        AccountModel accountModel = accountMapper.findByLoginName(loanDto.getLoanLoginName());
        String loanUserId = null;
        if (accountModel != null) {
            loanUserId = accountModel.getPayUserId();
        } else {
            dataDto.setStatus(false);
            return baseDto;
        }
        MerBindProjectRequestModel requestModel = new MerBindProjectRequestModel();
        requestModel.setLoanUserId(loanDto.getProjectAmount());
        requestModel.setProjectAmount(loanUserId);
        String projectId = String.valueOf(System.currentTimeMillis());/****标的号****/

        requestModel.setProjectId(projectId);
        requestModel.setProjectName(loanDto.getProjectName());
        MerBindProjectResponseModel responseModel = null;
        try {
            responseModel = paySyncClient.send(MerBindProjectMapper.class, requestModel, MerBindProjectResponseModel.class);
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
                loanTitleMapper.createLoanTitle(loanDto.getLoanTitles());
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

    /**
     * @param titleModel
     * @function 创建标题
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTitle(TitleModel titleModel) {
        titleMapper.createTitle(titleModel);
    }

    /**
     * @param loginName
     * @return
     * @function 获取成功注册过资金托管账户的用户登录名
     */
    @Override
    public List<String> getLoginNames(String loginName) {
        return accountMapper.findAllLoginNamesByLike(loginName);
    }

}
