package com.tuotiansudai.console.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.*;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ActivityConsoleInviteHelpService {

    @Autowired
    private WeChatHelpMapper weChatHelpMapper;

    @Autowired
    private WeChatHelpInfoMapper weChatHelpInfoMapper;

    @Autowired
    private WeChatUserInfoMapper weChatUserInfoMapper;

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.third.anniversary.startTime}\")}")
    private Date activityStartTime;

    private final List<Rates> rates = Lists.newArrayList(
            new Rates(2, 8, "0.2%"),
            new Rates(8, 18, "0.5%"),
            new Rates(18, 58, "0.6%"),
            new Rates(58, 88, "0.7%"),
            new Rates(88, 108, "0.8%"),
            new Rates(108, Integer.MAX_VALUE, "1%")
    );

    private final Map<Integer, Double> thirdAnniversaryRated = Maps.newHashMap(ImmutableMap.<Integer, Double>builder()
            .put(0, 0D)
            .put(1, 0.001D)
            .put(2, 0.002D)
            .put(3, 0.005D)
            .build());


    public BasePaginationDataDto investRewardList(int index, int pageSize, String KeyWord, Long minInvest, Long maxInvest, WeChatHelpType weChatHelpType) {
        List<WeChatHelpView> weChatHelpViews = weChatHelpMapper.findByKeyWord(KeyWord, minInvest, maxInvest, WeChatHelpType.INVEST_HELP);
        weChatHelpViews.forEach(weChatHelpView -> weChatHelpView.setRate(getRate(weChatHelpView)));
        if (weChatHelpType == WeChatHelpType.THIRD_ANNIVERSARY_HELP){
            weChatHelpViews = weChatHelpMapper.findByKeyWord(KeyWord, minInvest, maxInvest, WeChatHelpType.THIRD_ANNIVERSARY_HELP);
            weChatHelpViews.forEach(view->{
                List<ActivityInvestModel> investModels = activityInvestMapper.findAllByActivityLoginNameAndTime(view.getLoginName(), ActivityCategory.THIRD_ANNIVERSARY.name(), activityStartTime, view.getEndTime());
                List<WeChatHelpInfoModel> helpInfoModels = weChatHelpInfoMapper.findByHelpId(view.getId());
                long annualizedAmount = investModels.stream().mapToLong(ActivityInvestModel::getAnnualizedAmount).sum();
                long investAmount = investModels.stream().mapToLong(ActivityInvestModel::getInvestAmount).sum();
                view.setInvestAmount(investAmount);
                view.setAnnualizedAmount(annualizedAmount);
                int helpUserCount = helpInfoModels.size();
                view.setHelpUserCount(helpUserCount);
                view.setReward((long) (annualizedAmount * thirdAnniversaryRated.get(helpUserCount)));
                view.setRate(helpUserCount == 0 ? "0" : String.format("%.1f", thirdAnniversaryRated.get(helpUserCount) * 100));
            });
        }
        int count = weChatHelpViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        return new BasePaginationDataDto(index, pageSize, count, weChatHelpViews.subList(startIndex, endIndex));

    }

    public BasePaginationDataDto shareRewardList(int index, int pageSize, String KeyWord) {
        List<WeChatHelpView> weChatHelpViews = weChatHelpMapper.findByKeyWord(KeyWord, null, null, WeChatHelpType.EVERYONE_HELP);
        int count = weChatHelpViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        return new BasePaginationDataDto(index, pageSize, count, weChatHelpViews.subList(startIndex, endIndex));
    }

    public WeChatHelpModel findById(long id){
        return weChatHelpMapper.findById(id);
    }


    public BasePaginationDataDto investRewardDetail(int index, int pageSize, long id, String nickName, WeChatHelpUserStatus status) {
        weChatUserInfoMapper.initCharset();
        List<WeChatHelpInfoView> weChatHelpInfoViews = weChatHelpInfoMapper.findByNickName(id, nickName, status);
        boolean isThirdAnniversary = weChatHelpMapper.findById(id).getType() == WeChatHelpType.THIRD_ANNIVERSARY_HELP;
        if (!isThirdAnniversary){
            for (WeChatHelpInfoView weChatHelpInfoView: weChatHelpInfoViews) {
                if (Strings.isNullOrEmpty(weChatHelpInfoView.getMobile())){
                    WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(weChatHelpInfoView.getOpenId());
                    if (weChatUserModel != null && weChatUserModel.isBound()) {
                        UserModel userModel = userMapper.findByLoginName(weChatUserModel.getLoginName());
                        weChatHelpInfoView.setUserName(userModel.getUserName());
                        weChatHelpInfoView.setMobile(userModel.getMobile());
                    }
                }
            }
        }

        int count = weChatHelpInfoViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        return new BasePaginationDataDto(index, pageSize, count, weChatHelpInfoViews.subList(startIndex, endIndex));
    }

    public WeChatHelpModel getHelpModel(long id){
        return weChatHelpMapper.findById(id);
    }

    public String getRate(WeChatHelpView weChatHelpView){
        Optional<Rates> optional = rates.stream().filter(rate -> rate.getMinNum() <= weChatHelpView.getHelpUserCount() && rate.getMaxNum() > weChatHelpView.getHelpUserCount()).findAny();
        return optional.map(Rates::getRate).orElse("");
    }

    class Rates{
        private int minNum;
        private int maxNum;
        private String rate;

        public Rates() {
        }

        Rates(int minNum, int maxNum, String rate) {
            this.minNum = minNum;
            this.maxNum = maxNum;
            this.rate = rate;
        }

        int getMinNum() {
            return minNum;
        }

        int getMaxNum() {
            return maxNum;
        }

        public String getRate() {
            return rate;
        }
    }


}
