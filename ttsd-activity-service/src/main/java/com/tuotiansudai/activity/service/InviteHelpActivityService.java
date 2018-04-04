package com.tuotiansudai.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpInfoMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatUserInfoMapper;
import com.tuotiansudai.activity.repository.model.WeChatHelpInfoModel;
import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
import com.tuotiansudai.activity.repository.model.WeChatHelpType;
import com.tuotiansudai.activity.repository.model.WeChatUserInfoModel;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.util.WeChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InviteHelpActivityService {

    @Autowired
    private WeChatUserInfoMapper weChatUserInfoMapper;

    @Autowired
    private WeChatHelpMapper weChatHelpMapper;

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Autowired
    private WeChatHelpInfoMapper weChatHelpInfoMapper;

    private final WeChatClient weChatClient = WeChatClient.getClient();

    private final List<Double> rates = Lists.newArrayList(0.002D, 0.005D, 0.006D, 0.007D, 0.008D, 0.01D);
    private final List<Integer> number = Lists.newArrayList(2, 8, 18, 58, 88, 108);

//    private final List<RewardRate> rewardRates = Lists.newArrayList(
//            new RewardRate(2, 8, 0.002),
//            new RewardRate(8, 18, 0.005),
//            new RewardRate(18, 58, 0.006),
//            new RewardRate(58, 88, 0.007),
//            new RewardRate(88, 108, 0.008),
//            new RewardRate(108, Integer.MAX_VALUE, 0.01));

    public Map<String, Object> investHelp(String loginName){
        List<WeChatHelpModel> list = weChatHelpMapper.findAllByType(WeChatHelpType.INVEST_HELP);
        List<WeChatHelpModel> sendCashList = list.stream().filter(WeChatHelpModel::isSend).collect(Collectors.toList());
        List<WeChatHelpModel> myList = list.stream().filter(i->i.getLoginName().equals(loginName)).collect(Collectors.toList());
        return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("sendCashList", sendCashList)
                .put("myList", myList)
                .build());
    }

    public Map<String, Object> investHelpDetail(long id, String loginName){
        WeChatHelpModel weChatHelpModel = weChatHelpMapper.findById(id);
        if (weChatHelpModel != null && weChatHelpModel.getLoginName().equals(loginName)) {
            List<WeChatUserInfoModel> list = weChatHelpInfoMapper.findInfoByHelpId(weChatHelpModel.getId());
            List<Long> myCashChain = new ArrayList<>();
            rates.forEach(rate -> myCashChain.add((long) (weChatHelpModel.getAnnualizedAmount() * rate)));



        }
        return null;
    }

    public int getNextChainNode(int userCount){
        
    }


    public void createWeChatUserInfo(String openId){
        Map<String, Object> map = weChatClient.fetchWeChatUserInfo(openId);
        if (map == null){
            return;
        }
        weChatUserInfoMapper.initCharset();
        WeChatUserInfoModel weChatUserInfoModel = weChatUserInfoMapper.findByOpenId(openId);
        if (weChatUserInfoModel == null){
            weChatUserInfoModel = new WeChatUserInfoModel(openId, (String) map.get("nickname"), (String) map.get("headimgurl"));
            weChatUserInfoMapper.create(weChatUserInfoModel);
        }else {
            weChatUserInfoModel.setNickName((String) map.get("nickname"));
            weChatUserInfoModel.setHeadImgUrl((String) map.get("headimgurl"));
            weChatUserInfoModel.setUpdatedTime(new Date());
            weChatUserInfoMapper.update(weChatUserInfoModel);
        }
    }


    class RewardRate{
        private int minNum;
        private int maxNum;
        private double rate;

        public RewardRate() {
        }

        public RewardRate(int minNum, int maxNum, double rate) {
            this.minNum = minNum;
            this.maxNum = maxNum;
            this.rate = rate;
        }

        public int getMinNum() {
            return minNum;
        }

        public int getMaxNum() {
            return maxNum;
        }

        public double getRate() {
            return rate;
        }
    }
}
