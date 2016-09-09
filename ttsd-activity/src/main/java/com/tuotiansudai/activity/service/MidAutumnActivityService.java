package com.tuotiansudai.activity.service;


import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class MidAutumnActivityService {

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.min.autumn.startTime}\")}")
    private Date activityMinAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.min.autumn.endTime}\")}")
    private Date activityMinAutumnEndTime;

    public List getAllFamilyInvestAmount() {
        Map<String, List<String>> allFamily = getAllFamilyMap();
        List<MidAutumnFamilyDto> allFamilyAmountList = Lists.newArrayList();
        int count = 0;
        for (String key : allFamily.keySet()) {
            count++;
            if(count > 3){
                break;
            }
            List<String> family = allFamily.get(key);
            long investAmount = 0l;
            for (String name : family) {
                investAmount += investMapper.sumInvestAmount(null, name, null, null, null, activityMinAutumnStartTime, activityMinAutumnEndTime, InvestStatus.SUCCESS, null);
            }
            allFamilyAmountList.add(new MidAutumnFamilyDto(MessageFormat.format("{0}号家庭", count), AmountConverter.convertCentToString(investAmount)));
        }

        return allFamilyAmountList;
    }

    public Map getMyMinAutumnActivityFamily(String loginName) {
        Map<String, List<String>> allFamily = getAllFamilyMap();
        List<String> myFamily = getMyFamily(allFamily, loginName);

        if (CollectionUtils.isEmpty(myFamily) || myFamily.size() == 1) {
            return Maps.newConcurrentMap();
        }

        int group = 0;
        for (String key : allFamily.keySet()) {
            group++;
            if (key.equals(myFamily.get(0))) {
                break;
            }
        }

        Iterator<String> family = Iterators.transform(myFamily.iterator(), new Function<String, String>() {
            @Override
            public String apply(String input) {
                return randomUtils.encryptWebMiddleMobile(userMapper.findByLoginName(input).getMobile());
            }
        });

        long investAmount = 0l;
        for (String name : myFamily) {
            investAmount += investMapper.sumInvestAmount(null, name, null, null, null, activityMinAutumnStartTime, activityMinAutumnEndTime, InvestStatus.SUCCESS, null);
        }

        Map<String, Object> myFamilyMap = Maps.newConcurrentMap();
        myFamilyMap.put("number", String.valueOf(group));
        myFamilyMap.put("myFamily", Lists.newArrayList(family));
        myFamilyMap.put("myFamilyInvestAmount", AmountConverter.convertCentToString(investAmount));
        return myFamilyMap;
    }

    private Map getAllFamilyMap() {
        List<UserModel> userModels = userMapper.findUsersByRegisterTime(activityMinAutumnStartTime, activityMinAutumnEndTime);

        if (CollectionUtils.isEmpty(userModels)) {
            return Maps.newConcurrentMap();
        }

        Map<String, List<String>> allFamily = Maps.newConcurrentMap();
        String referrerLoginName;
        String loginName;
        for (UserModel userModel : userModels) {
            List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByLoginNameAndLevel(userModel.getLoginName(), 1);
            //有一级推荐时查询是否是团长，否则查询是否是团长推荐（无视推荐层级）
            if (CollectionUtils.isNotEmpty(referrerRelationModels)) {
                referrerLoginName = referrerRelationModels.get(0).getReferrerLoginName();
                loginName = userModel.getLoginName();
                if (allFamily.get(referrerLoginName) != null) {
                    allFamily.get(referrerLoginName).add(loginName);
                    continue;
                }

                for (String name : allFamily.keySet()) {
                    if (referrerRelationMapper.findByReferrerAndLoginName(name, loginName) != null) {
                        allFamily.get(name).add(loginName);
                        continue;
                    }
                }
            } else {
                //没有一级推荐就是团长
                allFamily.put(userModel.getLoginName(), Lists.newArrayList(userModel.getLoginName()));
            }
        }
        return allFamily;
    }

    private List getMyFamily(Map<String, List<String>> allFamily, final String loginName) {
        List<String> myFamily = null;
        for (String key : allFamily.keySet()) {
            Optional<String> family = Iterators.tryFind(allFamily.get(key).iterator(), new Predicate<String>() {
                @Override
                public boolean apply(String input) {
                    return input.equals(loginName);
                }
            });

            if (family.isPresent()) {
                myFamily = allFamily.get(key);
                break;
            }
        }
        return myFamily;
    }
}
