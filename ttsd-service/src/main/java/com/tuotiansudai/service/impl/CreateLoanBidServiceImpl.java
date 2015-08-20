package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.mapper.TitleMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.TitleModel;
import com.tuotiansudai.service.CreateLoanBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private LoanTitleMapper loanTitleMapper;

    /**
     * @param titleModel
     * @function 创建标题
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTitle(TitleModel titleModel) {
        titleModel.setType("new");
        titleMapper.createTitle(titleModel);
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

    public List<TitleModel> findAllTitles(){
        return titleMapper.findAllTitles();
    }

    @Override
    public List<Map<String,String>> getLoanType() {
        List<Map<String,String>> loanTypes = new ArrayList<Map<String,String>>();
        for (LoanType loanType:LoanType.values()){
            Map<String,String> map = new HashMap<String,String>();
            map.put("loanType",loanType.name());
            map.put("name",loanType.getName());
            map.put("repayTimeUnit",loanType.getRepayTimeUnit());
            loanTypes.add(map);
        }
        return loanTypes;
    }

    @Override
    public List<Map<String,String>> getActivityType() {
        List<Map<String,String>> activityTypes = new ArrayList<Map<String,String>>();
        for (ActivityType activityType:ActivityType.values()){
            Map<String,String> map = new HashMap<String,String>();
            map.put(activityType.getActivityTypeCode(),activityType.getActivityTypeName());
            activityTypes.add(map);
        }
        return activityTypes;
    }
}
