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

import java.util.*;
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

    private final Map<Integer, Double> rates = Maps.newHashMap(ImmutableMap.<Integer, Double>builder()
            .put(2, 0.002D)
            .put(8, 0.005D)
            .put(18, 0.006D)
            .put(58, 0.007D)
            .put(88, 0.008D)
            .put(108, 0.01D)
            .build());

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
            Map<String, Object> map = new HashMap<>();
            List<Long> myCashChain = new ArrayList<>();
            rates.values().forEach(rate -> myCashChain.add((long) (weChatHelpModel.getAnnualizedAmount() * rate)));
            map.put("myCashChain", myCashChain);
            Optional<Integer> optional = rates.keySet().stream().filter(i -> i > weChatHelpModel.getHelpUserCount()).findFirst();
            if (optional.isPresent()){
                map.put("nextNode", optional.get() - weChatHelpModel.getHelpUserCount());
                map.put("nextAmount", (long) (weChatHelpModel.getAnnualizedAmount() * rates.get(optional.get())));
            }
            map.put("helpFriends", weChatUserInfoMapper.findInfoByHelpId(weChatHelpModel.getId()));
            map.put("helpModel", weChatHelpModel);
            return map;
        }
        return null;
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
}
