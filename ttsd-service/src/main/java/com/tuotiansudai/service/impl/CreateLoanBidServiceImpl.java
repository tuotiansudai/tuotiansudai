package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.TitleDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.TitleMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.TitleModel;
import com.tuotiansudai.service.CreateLoanBidService;
import com.tuotiansudai.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CreateLoanBidServiceImpl implements CreateLoanBidService{
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TitleMapper titleMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private IdGenerator idGenerator;
    /**
     * @param titleDto
     * @function 创建标题
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TitleModel createTitle(TitleDto titleDto) {
        TitleModel titleModel = new TitleModel();
        BigInteger id = new BigInteger(String.valueOf(idGenerator.generate()));
        titleModel.setId(id);
        titleModel.setTitle(titleDto.getTitle());
        titleModel.setType("new");
        titleMapper.createTitle(titleModel);
        return titleModel;
    }

    /**
     * @param loginName
     * @return
     * @function 获取成功注册过资金托管账户的用户登录名
     */
    @Override
    public List<String> getLoginNames(String loginName) {
        return accountMapper.findAllLoginNamesByLike("%"+loginName+"%");
    }

    public Map<String,List<TitleModel>> findAllTitles(){
        Map<String,List<TitleModel>> map = new HashMap<>();
        map.put("titles",titleMapper.findAllTitles());
        return map;
    }

    @Override
    public List<Map<String,String>> getLoanType() {
        List<Map<String,String>> loanTypes = new ArrayList<Map<String,String>>();
        for (LoanType loanType:LoanType.values()){
            Map<String,String> map = new HashMap<String,String>();
            map.put("loanTypeName",loanType.name());
            map.put("name",loanType.getName());
            map.put("repayTimeUnit",loanType.getRepayTimeUnit());
            map.put("repayTimePeriod",loanType.getRepayTimePeriod());
            loanTypes.add(map);
        }
        return loanTypes;
    }

    @Override
    public List<Map<String,String>> getActivityType() {
        List<Map<String,String>> activityTypes = new ArrayList<Map<String,String>>();
        for (ActivityType activityType:ActivityType.values()){
            Map<String,String> map = new HashMap<String,String>();
            map.put("activityTypeCode",activityType.getActivityTypeCode());
            map.put("activityTypeName",activityType.getActivityTypeName());
            activityTypes.add(map);
        }
        return activityTypes;
    }

    @Override
    public BaseDto<PayFormDataDto> createLoanBid(LoanDto loanDto) {
        return payWrapperClient.loan(loanDto);
    }
}
