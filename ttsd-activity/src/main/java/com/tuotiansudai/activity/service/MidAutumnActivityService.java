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
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
public class MidAutumnActivityService {

    @Autowired
    private AutumnService autumnService;

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

//    public List getAllFamilyInvestAmount() {
//        Map<String, List<String>> allFamily = getAllFamilyMap();
//        List<MidAutumnFamilyDto> allFamilyAmountList = Lists.newArrayList();
//        int count = 0;
//        for (String key : allFamily.keySet()) {
//            count++;
//            if(count > 3){
//                break;
//            }
//            List<String> family = allFamily.get(key);
//            long investAmount = 0l;
//            for (String name : family) {
//                investAmount += investMapper.sumInvestAmount(null, name, null, null, null, activityMinAutumnStartTime, activityMinAutumnEndTime, InvestStatus.SUCCESS, null);
//            }
//            allFamilyAmountList.add(new MidAutumnFamilyDto(MessageFormat.format("{0}号家庭", count), AmountConverter.convertCentToString(investAmount)));
//        }
//
//        return allFamilyAmountList;
//    }
//
//    public Map getMyMinAutumnActivityFamily(String loginName) {
//        Map<String, List<String>> allFamily = getAllFamilyMap();
//        List<String> myFamily = getMyFamily(allFamily, loginName);
//
//        if (CollectionUtils.isEmpty(myFamily) || myFamily.size() == 1) {
//            return Maps.newConcurrentMap();
//        }
//
//        int group = 0;
//        for (String key : allFamily.keySet()) {
//            group++;
//            if (key.equals(myFamily.get(0))) {
//                break;
//            }
//        }
//
//        Iterator<String> family = Iterators.transform(myFamily.iterator(), new Function<String, String>() {
//            @Override
//            public String apply(String input) {
//                return randomUtils.encryptWebMiddleMobile(userMapper.findByLoginName(input).getMobile());
//            }
//        });
//
//        long investAmount = 0l;
//        for (String name : myFamily) {
//            investAmount += investMapper.sumInvestAmount(null, name, null, null, null, activityMinAutumnStartTime, activityMinAutumnEndTime, InvestStatus.SUCCESS, null);
//        }
//
//        Map<String, Object> myFamilyMap = Maps.newConcurrentMap();
//        myFamilyMap.put("number", String.valueOf(group));
//        myFamilyMap.put("myFamily", Lists.newArrayList(family));
//        myFamilyMap.put("myFamilyInvestAmount", AmountConverter.convertCentToString(investAmount));
//        return myFamilyMap;
//    }
//
//    private Map getAllFamilyMap() {
//        List<UserModel> userModels = userMapper.findUsersByRegisterTime(activityMinAutumnStartTime, activityMinAutumnEndTime);
//
//        if (CollectionUtils.isEmpty(userModels)) {
//            return Maps.newConcurrentMap();
//        }
//
//        Map<String, List<String>> allFamily = Maps.newConcurrentMap();
//        String referrerLoginName;
//        String loginName;
//        for (UserModel userModel : userModels) {
//            List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByLoginNameAndLevel(userModel.getLoginName(), 1);
//            //有一级推荐时查询是否是团长，否则查询是否是团长推荐（无视推荐层级）
//            if (CollectionUtils.isNotEmpty(referrerRelationModels)) {
//                referrerLoginName = referrerRelationModels.get(0).getReferrerLoginName();
//                loginName = userModel.getLoginName();
//                if (allFamily.get(referrerLoginName) != null) {
//                    allFamily.get(referrerLoginName).add(loginName);
//                    continue;
//                }
//
//                for (String name : allFamily.keySet()) {
//                    if (referrerRelationMapper.findByReferrerAndLoginName(name, loginName) != null) {
//                        allFamily.get(name).add(loginName);
//                        continue;
//                    }
//                }
//            } else {
//                //没有一级推荐就是团长
//                allFamily.put(userModel.getLoginName(), Lists.newArrayList(userModel.getLoginName()));
//            }
//        }
//        return allFamily;
//    }
//
//    private List getMyFamily(Map<String, List<String>> allFamily, final String loginName) {
//        List<String> myFamily = null;
//        for (String key : allFamily.keySet()) {
//            Optional<String> family = Iterators.tryFind(allFamily.get(key).iterator(), new Predicate<String>() {
//                @Override
//                public boolean apply(String input) {
//                    return input.equals(loginName);
//                }
//            });
//
//            if (family.isPresent()) {
//                myFamily = allFamily.get(key);
//                break;
//            }
//        }
//        return myFamily;
//    }

    public Map getMidAutumnHomeData(String loginName){
        Map<String,List<String>> allFamily = autumnService.getAllFamilyMap(activityMinAutumnStartTime,activityMinAutumnEndTime);
        List<String> myFamily = Lists.newArrayList();
        String myFamilyNum = "";
        if(allFamily.get(loginName) != null){
            myFamily.addAll(allFamily.get(loginName));
        }else{
            for(String key : allFamily.keySet()){
                List<String> family = allFamily.get(key);
                for(String name : family){
                    if(loginName.equals(name.split("|")[0])){
                        myFamily.addAll(family);
                        myFamilyNum = name.split("|")[1];
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

        Map<String,Object> homeData = Maps.newConcurrentMap();
        homeData.put("myFamily",Lists.newArrayList(family));
        homeData.put("myFamilyNum",myFamilyNum);
        homeData.put("totalInvestAmount",AmountConverter.convertCentToString(totalInvestAmount));
        homeData.put("todayInvestAmount",AmountConverter.convertCentToString(todayInvestAmount));
        homeData.put("topThreeFamily",getTopThreeFamily(allFamily));

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
            MidAutumnFamilyDto midAutumnFamilyDto = new MidAutumnFamilyDto(key.split("|")[1],AmountConverter.convertCentToString(totalFamilyInvestAmount),totalFamilyInvestAmount);
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


    public static void main(String args[]){
        Map<String,String> map = new LinkedHashMap();
        map.put("1","1");
        map.put("2","1");
        map.put("3","1");
        map.put("4","1");
        map.put("5","1");
        map.put("6","1");
        map.put("7","1");
        map.put("8","1");
        map.put("9","1");
        for(String key : map.keySet()){
            System.out.println(key);
        }
    }
}
