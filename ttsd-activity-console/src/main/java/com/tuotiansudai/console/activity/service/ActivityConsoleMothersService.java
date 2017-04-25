package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.model.MothersDayView;
import com.tuotiansudai.activity.repository.model.WomanDayRecordView;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ActivityConsoleMothersService {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mothers.day.startTime}\")}")
    private Date activityStartTimeStr;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mothers.day.endTime}\")}")
    private Date activityEndTimeStr;



    public void list(){

    }

    private List<MothersDayView> setInvestRecord(String loginName) {
        List<MothersDayView> list = Lists.newArrayList();
        List<InvestModel> investModels = investMapper.findSuccessInvestByInvestTime(loginName, activityStartTimeStr, activityEndTimeStr);
        Map<String, Long> investAmountMaps = Maps.newConcurrentMap();
        for (InvestModel investModel : investModels) {
            if (investModel.getLoanId() == 1 || investModel.getTransferInvestId() != null)
                continue;

            if (investAmountMaps.get(investModel.getLoginName()) == null) {
                investAmountMaps.put(investModel.getLoginName(), investModel.getAmount());
                continue;
            }
            investAmountMaps.put(investModel.getLoginName(), investAmountMaps.get(investModel.getLoginName()) + investModel.getAmount());
        }

        investAmountMaps.forEach((k, v) -> {
            UserModel userModel = userMapper.findByLoginName(k);
            list.add(new MothersDayView(k, userModel.getUserName(), userModel.getMobile(), AmountConverter.convertCentToString(v), ));
        });

        return list;
    }

    
}
