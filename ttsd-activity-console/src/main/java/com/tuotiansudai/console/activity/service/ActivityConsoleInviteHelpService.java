package com.tuotiansudai.console.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpInfoMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatUserInfoMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final List<Rates> rates = Lists.newArrayList(
            new Rates(2, 8, "0.2%"),
            new Rates(8, 18, "0.5%"),
            new Rates(18, 58, "0.6%"),
            new Rates(58, 88, "0.7%"),
            new Rates(88, 108, "0.8%"),
            new Rates(108, Integer.MAX_VALUE, "1%")
    );


    public BasePaginationDataDto investRewardList(int index, int pageSize, String KeyWord, Long minInvest, Long maxInvest) {
        List<WeChatHelpView> weChatHelpViews = weChatHelpMapper.findByKeyWord(KeyWord, minInvest, maxInvest, WeChatHelpType.INVEST_HELP);
        weChatHelpViews.forEach(weChatHelpView -> weChatHelpView.setRate(getRate(weChatHelpView)));
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
        for (WeChatHelpInfoView weChatHelpInfoView: weChatHelpInfoViews) {
            WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(weChatHelpInfoView.getOpenId());
            if (weChatUserModel != null && weChatUserModel.isBound()) {
                UserModel userModel = userMapper.findByLoginName(weChatUserModel.getLoginName());
                weChatHelpInfoView.setUserName(userModel.getUserName());
                weChatHelpInfoView.setMobile(userModel.getMobile());
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
