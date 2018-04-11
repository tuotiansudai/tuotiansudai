package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
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
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.MobileEncryptor;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.WeChatClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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

    @Autowired
    private UserMapper userMapper;

    private final WeChatClient weChatClient = WeChatClient.getClient();

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    public static final String EVERYONE_HELP_WAIT_SEND_CASH = "EVERYONE_HELP_WAIT_SEND_CASH";

    private final List<Rates> rates = Lists.newArrayList(
            new Rates(2, 8, 0.002D),
            new Rates(8, 18, 0.005D),
            new Rates(18, 58, 0.006D),
            new Rates(58, 88, 0.007D),
            new Rates(88, 108, 0.008D),
            new Rates(108, Integer.MAX_VALUE, 0.01D)
    );

    public List<WeChatHelpModel> sendRewardRecord(){
        List<WeChatHelpModel> list = weChatHelpMapper.findByUserAndHelpType(null, null, WeChatHelpType.INVEST_HELP);
        List<WeChatHelpModel> sendCashList = list.stream().filter(WeChatHelpModel::isSend).collect(Collectors.toList());
        sendCashList.forEach(weChatHelpModel -> weChatHelpModel.setMobile(MobileEncryptor.encryptMiddleMobile(weChatHelpModel.getMobile())));
        return sendCashList;
    }

    public List<WeChatHelpModel> myInvestHelp(String loginName){
        List<WeChatHelpModel> list = weChatHelpMapper.findByUserAndHelpType(null, null, WeChatHelpType.INVEST_HELP);
        return list.stream().filter(i->i.getLoginName().equals(loginName)).collect(Collectors.toList());
    }

    public Map<String, Object> investHelpDetail(long id, String loginName){
        WeChatHelpModel weChatHelpModel = weChatHelpMapper.findById(id);
        if (weChatHelpModel != null && weChatHelpModel.getLoginName().equals(loginName)) {
            Map<String, Object> map = new HashMap<>();
            List<Long> myCashChain = new ArrayList<>();
            rates.forEach(rate -> myCashChain.add((long) (weChatHelpModel.getAnnualizedAmount() * rate.getRate())));
            map.put("myCashChain", myCashChain);
            Optional<Rates> optional = rates.stream().filter(rates -> rates.getMinNum() > weChatHelpModel.getHelpUserCount()).findFirst();
            if (optional.isPresent()){
                map.put("nextNode", optional.get().getMinNum() - weChatHelpModel.getHelpUserCount());
                map.put("nextAmount", (long) (weChatHelpModel.getAnnualizedAmount() * optional.get().getRate()));
            }
            map.put("helpFriends", weChatUserInfoMapper.findInfoByHelpId(weChatHelpModel.getId()));
            map.put("helpModel", weChatHelpModel);
            map.put("name", userMapper.findByLoginName(loginName).getUserName());
            return map;
        }
        return null;
    }

    @Transactional
    public long createEveryoneHelp(String loginName, String mobile, String openId){
        WeChatHelpModel weChatHelpModel = new WeChatHelpModel(WeChatHelpType.EVERYONE_HELP, new Date(), DateTime.now().plusDays(1).toDate());
        if (loginName != null) {
            weChatHelpModel.setLoginName(loginName);
            weChatHelpModel.setMobile(mobile);
        } else {
            WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(openId);
            if (weChatUserModel.isBound()) {
                weChatHelpModel.setLoginName(weChatUserModel.getLoginName());
                weChatHelpModel.setOpenId(weChatUserModel.getOpenid());
                weChatHelpModel.setMobile(userMapper.findByLoginName(weChatUserModel.getLoginName()).getMobile());
            } else {
                weChatHelpModel.setOpenId(openId);
            }
        }
        weChatHelpMapper.create(weChatHelpModel);
        redisWrapperClient.hset(EVERYONE_HELP_WAIT_SEND_CASH, String.valueOf(weChatHelpModel.getId()), DateTime.now().plusDays(1).toString("yyyy-MM-dd HH:mm:ss"));
        return weChatHelpModel.getId();
    }

    public WeChatHelpModel everyoneHelp(String loginName) {
        List<WeChatHelpModel> list = weChatHelpMapper.findByUserAndHelpType(loginName, null, WeChatHelpType.EVERYONE_HELP);
        return list.size() > 0 ? list.get(0) : null;
    }

    public Map<String, Object> everyoneHelpDetail(String loginName, String openId){
        List<WeChatHelpModel> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        if (loginName != null) {
            list = weChatHelpMapper.findByUserAndHelpType(loginName, null, WeChatHelpType.EVERYONE_HELP);
            UserModel userModel = userMapper.findByLoginName(loginName);
            map.put("name", userModel.getUserName() == null ? userModel.getMobile() : userModel.getUserName());
        } else {
            this.weChatUserInfo(openId);
            WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(openId);
            if (weChatUserModel.isBound()){
                list = weChatHelpMapper.findByUserAndHelpType(weChatUserModel.getLoginName(), null, WeChatHelpType.EVERYONE_HELP);
                UserModel userModel = userMapper.findByLoginName(weChatUserModel.getLoginName());
                map.put("name", userModel.getUserName() == null ? userModel.getMobile() : userModel.getUserName());
            }else{
                list = weChatHelpMapper.findByUserAndHelpType(null, openId, WeChatHelpType.EVERYONE_HELP);
                map.put("name", weChatUserInfoMapper.findByOpenId(openId).getNickName());
            }
        }
        if (list.size() > 0){
            map.put("model", list.get(0));
            map.put("helpFriends", weChatUserInfoMapper.findInfoByHelpId(list.get(0).getId()));
        }
        return map;
    }

    public Map<String, Object> weChatInvestHelpDetail(long id, String openId){
        this.weChatUserInfo(openId);
        WeChatHelpModel weChatHelpModel = weChatHelpMapper.findById(id);
        if (weChatHelpModel == null){
            return null;
        }
        Map<String, Object> map = this.investHelpDetail(id, weChatHelpModel.getLoginName());
        map.put("isHelp", weChatHelpInfoMapper.findByOpenId(openId, weChatHelpModel.getId()) != null);
        return map;
    }

    public Map<String, Object> weChatEveryoneHelpDetail(long id, String openId){
        this.weChatUserInfo(openId);
        WeChatHelpModel weChatHelpModel = weChatHelpMapper.findById(id);
        if (weChatHelpModel == null){
            return null;
        }

        String name;
        if (weChatHelpModel.getLoginName() != null){
            UserModel userModel = userMapper.findByLoginName(weChatHelpModel.getLoginName());
            name = userModel.getUserName() != null ? userModel.getUserName() : userModel.getMobile();
        }else{
            name = weChatUserInfoMapper.findByOpenId(openId).getNickName();
        }

        return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("model", weChatHelpModel)
                .put("name", name)
                .put("helpFriends", weChatUserInfoMapper.findInfoByHelpId(weChatHelpModel.getId()))
                .put("isHelp", weChatHelpInfoMapper.findByOpenId(openId, weChatHelpModel.getId()) != null)
                .build());
    }

    @Transactional
    public boolean clickHelp(long id, String openId){
        if (weChatHelpInfoMapper.findByOpenId(openId, id) != null) {
            return false;
        }
        if (weChatHelpInfoMapper.findHelpCountByOpenIdAndTime(openId, DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().toDate()) >= 5) {
            return false;
        }
        WeChatHelpModel weChatHelpModel = weChatHelpMapper.lockById(id);
        if (new Date().after(weChatHelpModel.getEndTime())){
            return false;
        }
        weChatHelpInfoMapper.create(new WeChatHelpInfoModel(openId, id));
        weChatHelpModel.setHelpUserCount(weChatHelpModel.getHelpUserCount() + 1);
        if (weChatHelpModel.getType() == WeChatHelpType.INVEST_HELP){
            Optional<Rates> optional = rates.stream().filter(rate -> rate.getMinNum() <= weChatHelpModel.getHelpUserCount() && rate.getMaxNum() > weChatHelpModel.getHelpUserCount()).findAny();
            optional.ifPresent(rates -> weChatHelpModel.setReward((long) (weChatHelpModel.getAnnualizedAmount() * rates.getRate())));
        }else if (weChatHelpModel.getReward() <= 1000){
            weChatHelpModel.setReward((weChatHelpModel.getHelpUserCount() * 20));
        }
        weChatHelpMapper.update(weChatHelpModel);
        return true;
    }

    private void weChatUserInfo(String openId){
        WeChatUserInfoModel weChatUserInfoModel = weChatUserInfoMapper.findByOpenId(openId);
        Map<String, Object> map = weChatClient.fetchWeChatUserInfo(openId);
        if (map != null){
            weChatUserInfoMapper.initCharset();
            if (weChatUserInfoModel == null){
                weChatUserInfoModel = new WeChatUserInfoModel(openId, (String) map.get("nickname"), (String) map.get("headimgurl"));
                weChatUserInfoMapper.create(weChatUserInfoModel);
            }else {
                weChatUserInfoModel.setNickName((String) map.get("nickname"));
                weChatUserInfoModel.setHeadImgUrl((String) map.get("headimgurl"));
                weChatUserInfoModel.setUpdatedTime(new Date());
                weChatUserInfoMapper.update(weChatUserInfoModel);
            }
        }else{
            if (weChatUserInfoModel == null){
                weChatUserInfoModel = new WeChatUserInfoModel(openId, null, null);
                weChatUserInfoMapper.create(weChatUserInfoModel);
            }
        }
    }

    class Rates{
        private int minNum;
        private int maxNum;
        private double rate;

        public Rates() {
        }

        Rates(int minNum, int maxNum, double rate) {
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

        public double getRate() {
            return rate;
        }
    }

    public boolean isOwnHelp(String loginName, String openId, long id){
        WeChatHelpModel weChatHelpModel = weChatHelpMapper.findById(id);
        return openId.equals(weChatHelpModel.getOpenId()) || (!Strings.isNullOrEmpty(loginName) && loginName.equals(weChatHelpModel.getLoginName()));
    }

}
