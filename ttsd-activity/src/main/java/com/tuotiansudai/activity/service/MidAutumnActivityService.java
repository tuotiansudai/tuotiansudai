package com.tuotiansudai.activity.service;


import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.dto.MidAutumnFamilyDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MidAutumnActivityService {

    @Autowired
    private AutumnService autumnService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.autumn.startTime}\")}")
    private Date activityMinAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.autumn.endTime}\")}")
    private Date activityMinAutumnEndTime;

    public Map getMidAutumnHomeData(String loginName){
        Map<String,List<String>> allFamily = autumnService.getAllFamilyMap(activityMinAutumnStartTime, activityMinAutumnEndTime);
        List<String> myFamily = Lists.newArrayList();
        String myFamilyNum = "";

        if(!Strings.isNullOrEmpty(loginName)){
            for(String key : allFamily.keySet()){
                List<String> family = allFamily.get(key);
                for(String name : family){
                    if(loginName.equals(name)){
                        myFamily.addAll(family);
                        myFamilyNum = key;
                        break;
                    }
                }
            }
        }

        long totalInvestAmount = 0l;
        long todayInvestAmount = 0l;
        for (String name : myFamily) {
            totalInvestAmount += investMapper.sumInvestAmount(null, name, null, null, null, activityMinAutumnStartTime, activityMinAutumnEndTime, InvestStatus.SUCCESS, null);
            todayInvestAmount += investMapper.sumInvestAmount(null, name, null, null, null, DateTime.now().withTimeAtStartOfDay().toDate(), DateUtils.addMilliseconds(DateTime.now().plusDays(1).withTimeAtStartOfDay().toDate(), -1000), InvestStatus.SUCCESS, null);
        }

        Iterator<String> family = Iterators.transform(myFamily.iterator(), new Function<String, String>() {
            @Override
            public String apply(String input) {
                return randomUtils.encryptWebMiddleMobile(userMapper.findByLoginName(input).getMobile());
            }
        });

        String isOverdue = "false";
        if (DateTime.now().toDate().before(activityMinAutumnStartTime) || DateTime.now().toDate().after(activityMinAutumnEndTime)) {
            isOverdue = "true";
        }

        Map<String,Object> homeData = Maps.newConcurrentMap();
        homeData.put("myFamily",Lists.newArrayList(family));
        homeData.put("myFamilyNum",myFamilyNum);
        homeData.put("totalInvestAmount",AmountConverter.convertCentToString(totalInvestAmount));
        homeData.put("todayInvestAmount",AmountConverter.convertCentToString(todayInvestAmount));
        homeData.put("topThreeFamily",getTopThreeFamily(allFamily));
        homeData.put("isOverdue",isOverdue);

        return homeData;
    }

    private List<MidAutumnFamilyDto> getTopThreeFamily(Map<String,List<String>> allFamily){
        List<MidAutumnFamilyDto> allFamilyAmountList = Lists.newArrayList();
        for(String key : allFamily.keySet()){
            long totalFamilyInvestAmount = 0;
            List<String> family = allFamily.get(key);
            for(String name : family){
                totalFamilyInvestAmount += investMapper.sumInvestAmount(null, name, null, null, null, activityMinAutumnStartTime, activityMinAutumnEndTime, InvestStatus.SUCCESS, null);
            }
            if(totalFamilyInvestAmount == 0){
                continue;
            }
            MidAutumnFamilyDto midAutumnFamilyDto = new MidAutumnFamilyDto(key,AmountConverter.convertCentToString(totalFamilyInvestAmount),totalFamilyInvestAmount);
            allFamilyAmountList.add(midAutumnFamilyDto);
        }

        Collections.sort(allFamilyAmountList, new Comparator<MidAutumnFamilyDto>() {
            @Override
            public int compare(MidAutumnFamilyDto o1, MidAutumnFamilyDto o2) {
                return Long.compare(o2.getAmount(),o1.getAmount());
            }
        });

        return allFamilyAmountList.size() > 3 ? allFamilyAmountList.subList(0,3) : allFamilyAmountList;
    }

}
