package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.model.WomanDayRecordView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ActivityWomanDayService {


    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.woman.day.startTime}\")}")
    private Date activityWomanDayStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.woman.day.endTime}\")}")
    private Date activityWomanDayEndTime;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    //每投资1000奖励花瓣
    private final long EACH_INVEST_AMOUNT_10000 = 100000L;

    public BasePaginationDataDto<WomanDayRecordView> getWomanDayPrizeRecord(int index, int pageSize, String loginName) {
        Map<String, WomanDayRecordView> womanDayAllRecordMap = setReferrerRecord(setInvestRecord(getSignRecord(loginName), loginName), loginName);
        List<WomanDayRecordView> womanDayRecordViews = Lists.newArrayList(womanDayAllRecordMap.values());
        for (WomanDayRecordView womanDayRecordView : womanDayRecordViews) {
            womanDayRecordView.setReferrerLeaves(womanDayRecordView.getReferrerLeaves() > 50 ? 50 : womanDayRecordView.getReferrerLeaves());
            womanDayRecordView.setTotalLeaves(womanDayRecordView.getInvestLeaves() + womanDayRecordView.getReferrerLeaves() + womanDayRecordView.getSignLeaves());
            womanDayRecordView.setPrize(getPrize(womanDayRecordView.getTotalLeaves()));
            UserModel userModel = userMapper.findByLoginNameOrMobile(womanDayRecordView.getMobile());
            womanDayRecordView.setName(userModel.getUserName());
            womanDayRecordView.setMobile(userModel.getMobile());
        }

        Collections.sort(womanDayRecordViews, (o1, o2) -> o1.getTotalLeaves() > o2.getTotalLeaves() ? 1 : 0);
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, womanDayRecordViews.size(), womanDayRecordViews);
        return basePaginationDataDto;
    }

    private Map<String, WomanDayRecordView> getSignRecord(String loginName) {
        Map<String, WomanDayRecordView> womanDayAllRecordMap = Maps.newConcurrentMap();
        pointBillMapper.findPointBillPagination(loginName, null, 0,
                Integer.MAX_VALUE, activityWomanDayStartTime, activityWomanDayEndTime,
                Lists.newArrayList(PointBusinessType.SIGN_IN)).
                stream().forEach(userBillModel -> this.putParam(womanDayAllRecordMap, userBillModel.getLoginName(), RewardType.SIGN_REWARD, 1));
        return womanDayAllRecordMap;
    }

    private Map<String, WomanDayRecordView> setInvestRecord(Map<String, WomanDayRecordView> womanDayAllRecordMap, String loginName) {
        List<InvestModel> investModels = investMapper.findSuccessInvestByInvestTime(loginName, activityWomanDayStartTime, activityWomanDayEndTime);
        Map<String, Long> investAmountMaps = Maps.newConcurrentMap();
        for(InvestModel investModel : investModels){
            if(investModel.getLoanId() == 1)
                continue;

            if(investAmountMaps.get(investModel.getLoginName()) == null){
                investAmountMaps.put(investModel.getLoginName(), investModel.getAmount());
                continue;
            }
            investAmountMaps.put(investModel.getLoginName(), investAmountMaps.get(investModel.getLoginName()) + investModel.getAmount());
        }

        investAmountMaps.forEach((k, v) -> {
            if(v / EACH_INVEST_AMOUNT_10000 > 0){
                this.putParam(womanDayAllRecordMap, k, RewardType.INVEST_REWARD , (int) (v / EACH_INVEST_AMOUNT_10000));
            }
        });
        return womanDayAllRecordMap;
    }

    private Map<String, WomanDayRecordView> setReferrerRecord(Map<String, WomanDayRecordView> womanDayAllRecordMap, String loginName) {
        List<UserModel> referrerUsers = userMapper.findUsersByRegisterTimeOrReferrer(activityWomanDayStartTime, activityWomanDayEndTime, loginName);
        referrerUsers.stream().filter(userModel -> investMapper.sumSuccessActivityInvestAmount(userModel.getLoginName(),null, activityWomanDayStartTime, activityWomanDayEndTime) >= 5000)
                .forEach(userModel -> this.putParam(womanDayAllRecordMap, userModel.getReferrer(), RewardType.REFERRER_REWARD, 1));
        return womanDayAllRecordMap;
    }

    private Map<String, WomanDayRecordView> putParam(Map<String, WomanDayRecordView> womanDayAllRecordMap, String loginName, RewardType rewardType, int rewardTime) {
        if (womanDayAllRecordMap.get(loginName) == null) {
            womanDayAllRecordMap.put(loginName, generateRecordView(new WomanDayRecordView(loginName), rewardType, rewardTime));
        } else {
            womanDayAllRecordMap.put(loginName, generateRecordView(womanDayAllRecordMap.get(loginName), rewardType, rewardTime));
        }
        return womanDayAllRecordMap;
    }

    private WomanDayRecordView generateRecordView(WomanDayRecordView womanDayRecordView, RewardType rewardType, int rewardTime) {
        switch (rewardType) {
            case SIGN_REWARD:
                womanDayRecordView.setSignLeaves(womanDayRecordView.getSignLeaves() + rewardTime);
                break;
            case INVEST_REWARD:
                womanDayRecordView.setInvestLeaves(womanDayRecordView.getInvestLeaves() + rewardTime);
                break;
            case REFERRER_REWARD:
                womanDayRecordView.setReferrerLeaves(womanDayRecordView.getReferrerLeaves() + rewardTime);
                break;
        }
        return womanDayRecordView;
    }

    private String getPrize(int totalLeaves) {
        if (9 <= totalLeaves && totalLeaves < 38) {
            return "礼盒一";
        }

        if (38 <= totalLeaves && totalLeaves < 78) {
            return "礼盒二";
        }

        if (78 <= totalLeaves && totalLeaves < 138) {
            return "礼盒三";
        }

        if (138 <= totalLeaves && totalLeaves < 238) {
            return "礼盒四";
        }

        if (238 <= totalLeaves && totalLeaves < 338) {
            return "礼盒五";
        }

        if (338 <= totalLeaves && totalLeaves < 438) {
            return "礼盒六";
        }

        if (438 <= totalLeaves && totalLeaves < 638) {
            return "礼盒七";
        }

        if (638 <= totalLeaves) {
            return "礼盒八";
        }
        return "";
    }

    enum RewardType {
        SIGN_REWARD(),
        INVEST_REWARD(),
        REFERRER_REWARD();

        RewardType() {
        }
    }
}










