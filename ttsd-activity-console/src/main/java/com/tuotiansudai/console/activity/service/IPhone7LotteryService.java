package com.tuotiansudai.console.activity.service;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.mapper.IPhone7LotteryConfigMapper;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryStatView;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryWinnerView;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigStatus;
import com.tuotiansudai.console.activity.dto.IPhone7InvestLotteryStatDto;
import com.tuotiansudai.console.activity.dto.IPhone7InvestLotteryWinnerDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IPhone7LotteryService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private IPhone7InvestLotteryMapper mapper;

    @Autowired
    private IPhone7LotteryConfigMapper configMapper;

    public BaseDto<BasePaginationDataDto> listStat(String mobile, int pageIndex, int pageSize) {
        String loginName = null;
        if (!Strings.isNullOrEmpty(mobile)) {
            UserModel userModel = userMapper.findByMobile(mobile);
            if (userModel == null) {
                BasePaginationDataDto<IPhone7InvestLotteryStatDto> paginationDataDto = new BasePaginationDataDto<>(
                        pageIndex, pageSize, 0, new ArrayList<>());
                return new BaseDto<>(paginationDataDto);
            }
            loginName = userModel.getLoginName();
        }

        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 0 ? 10 : pageSize;

        int rowIndex = (pageIndex - 1) * pageSize;

        List<IPhone7InvestLotteryStatView> records = mapper.statInvest(loginName, rowIndex, pageSize);
        int userCount = records.size();
        if (Strings.isNullOrEmpty(mobile)) {
            userCount = mapper.statUserCount();
        }
        List<IPhone7InvestLotteryStatDto> dtoList = records.stream().map(r -> {
            UserModel userModel = userMapper.findByLoginName(r.getLoginName());
            AccountModel accountModel = accountMapper.findByLoginName(r.getLoginName());
            return new IPhone7InvestLotteryStatDto(r, userModel.getMobile(), accountModel.getUserName());
        }).collect(Collectors.toList());

        BasePaginationDataDto<IPhone7InvestLotteryStatDto> paginationDataDto = new BasePaginationDataDto<>(
                pageIndex, pageSize, userCount, dtoList);
        return new BaseDto<>(paginationDataDto);
    }

    @Transactional(transactionManager = "transactionManager")
    public void approveConfig(long id, String loginName) {
        IPhone7LotteryConfigModel configModel = configMapper.findById(id);
        if (configModel != null) {
            List<IPhone7LotteryConfigModel> configModels = configMapper.findByInvestAmount(configModel.getInvestAmount());
            if (configModels.stream().anyMatch(c -> c.getStatus() == IPhone7LotteryConfigStatus.EFFECTIVE)) {
                throw new RuntimeException("此阶段已开奖完毕，不能进行修改中奖号码");
            }
            configMapper.removeApprovedConfig(configModel.getInvestAmount());
            configMapper.approve(id, loginName, new Date());
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public void refuseConfig(long id, String loginName) {
        IPhone7LotteryConfigModel configModel = configMapper.findById(id);
        if (configModel != null) {
            configMapper.refuse(id, loginName, new Date());
        }
    }

    public List<IPhone7InvestLotteryWinnerDto> listWinner() {
        List<IPhone7InvestLotteryWinnerView> winnerViews = mapper.listWinner();
        return winnerViews.stream().map(w -> {
            UserModel userModel = userMapper.findByLoginName(w.getLoginName());
            AccountModel accountModel = accountMapper.findByLoginName(w.getLoginName());
            return new IPhone7InvestLotteryWinnerDto(w, userModel.getMobile(), accountModel.getUserName());
        }).collect(Collectors.toList());
    }
}
