package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.BaseResponse;
import com.tuotiansudai.activity.repository.mapper.*;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThirdAnniversaryActivityService {

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.third.anniversary.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.third.anniversary.draw.endTime}\")}")
    private Date activityDrawEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.third.anniversary.endTime}\")}")
    private Date activityEndTime;

    private final Map<Integer, String> FOOTBALL_TEAMS = Maps.newHashMap(ImmutableMap.<Integer, String>builder()
            .put(1, "agenting")
            .put(2, "aiji")
            .put(3, "aodaliya")
            .put(4, "banama")
            .put(5, "baxi")
            .put(6, "bilishi")
            .put(7, "bilu")
            .put(8, "bingdao")
            .put(9, "bolan")
            .put(10, "danmai")
            .put(11, "deguo")
            .put(12, "eluosi")
            .put(13, "faguo")
            .put(14, "gelunbiya")
            .put(15, "gesidani")
            .put(16, "hanguo")
            .put(17, "keluodiya")
            .put(18, "moluoge")
            .put(19, "moxige")
            .put(20, "niriliya")
            .put(21, "putaoya")
            .put(22, "riben")
            .put(23, "ruidian")
            .put(24, "ruishi")
            .put(25, "saierweiya")
            .put(26, "saineijiaer")
            .put(27, "shatealabo")
            .put(28, "tunisi")
            .put(29, "wulagui")
            .put(30, "xibanya")
            .put(31, "yilang")
            .put(32, "yinggelan")
            .build());

    private final Map<Integer, Double> rates = Maps.newHashMap(ImmutableMap.<Integer, Double>builder()
            .put(0, 0D)
            .put(1, 0.001D)
            .put(2, 0.002D)
            .put(3, 0.005D)
            .build());

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String THIRD_ANNIVERSARY_TOP_FOUR_TEAM = "THIRD_ANNIVERSARY_TOP_FOUR_TEAM";

    private final String THIRD_ANNIVERSARY_TOP_FOUR_TEAM_CHINESE = "THIRD_ANNIVERSARY_TOP_FOUR_TEAM_CHINESE";

    private final String THIRD_ANNIVERSARY_EACH_EVERY_DAY_DRAW = "THIRD_ANNIVERSARY_EACH_EVERY_DAY_DRAW:{0}";

    private final String THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE = "THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE";

    private final String THIRD_ANNIVERSARY_WAIT_SEND_REWARD = "THIRD_ANNIVERSARY_WAIT_SEND_REWARD";

    @Autowired
    private ThirdAnniversaryDrawMapper thirdAnniversaryDrawMapper;

    @Autowired
    private ActivityInvestAnnualizedMapper activityInvestAnnualizedMapper;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ThirdAnniversaryHelpMapper thirdAnniversaryHelpMapper;

    @Autowired
    private ThirdAnniversaryHelpInfoMapper thirdAnniversaryHelpInfoMapper;

    @Autowired
    private UserMapper userMapper;

    private final long EACH_INVEST_AMOUNT_50000 = 50000L;

    private final int lifeSecond = 180 * 24 * 60 * 60;

    public Map<String, Object> selectResult(String loginName) {

        List<ActivityInvestAnnualizedView> annualizedViews = activityInvestAnnualizedMapper.findByActivityAndMobile(ActivityInvestAnnualized.THIRD_ANNIVERSARY_ACTIVITY, null);
        Map<String, String> supportMaps = redisWrapperClient.hgetAll(THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE);
        List<String> redSupportLoginName = supportMaps.entrySet().stream().filter(entry -> entry.getValue().equals("RED")).map(Map.Entry::getKey).collect(Collectors.toList());
        List<String> blueSupportLoginName = supportMaps.entrySet().stream().filter(entry -> entry.getValue().equals("BLUE")).map(Map.Entry::getKey).collect(Collectors.toList());

        long redSupportAmount = annualizedViews.stream().filter(view -> redSupportLoginName.contains(view.getLoginName())).mapToLong(ActivityInvestAnnualizedView::getSumAnnualizedAmount).sum();
        long blueSupportAmount = annualizedViews.stream().filter(view -> blueSupportLoginName.contains(view.getLoginName())).mapToLong(ActivityInvestAnnualizedView::getSumAnnualizedAmount).sum();

        if (Strings.isNullOrEmpty(loginName)) {
            return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                    .put("redAmount", AmountConverter.convertCentToString(redSupportAmount))
                    .put("blueAmount", AmountConverter.convertCentToString(blueSupportAmount))
                    .put("redCount", redSupportLoginName.size())
                    .put("blueCount", blueSupportLoginName.size())
                    .build());
        }

        ActivityInvestAnnualizedModel model = activityInvestAnnualizedMapper.findByActivityAndLoginName(ActivityInvestAnnualized.THIRD_ANNIVERSARY_ACTIVITY, loginName);
        boolean isSelect = redisWrapperClient.hexists(THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE, loginName);
        String currentRate = "0";
        String currentAward = "0";
        String selectResult = "";
        if (isSelect) {
            boolean isSelectRed = redisWrapperClient.hget(THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE, loginName).equals("RED");
            long myAmount = model == null ? 0 : model.getSumAnnualizedAmount();
            if (redSupportAmount > blueSupportAmount) {
                currentRate = isSelectRed ? "0.8%" : "0.5%";
                currentAward = AmountConverter.convertCentToString((long) (myAmount * (isSelectRed ? 0.008 : 0.005)));
            } else if (redSupportAmount < blueSupportAmount) {
                currentRate = isSelectRed ? "0.5%" : "0.8%";
                currentAward = AmountConverter.convertCentToString((long) (myAmount * (isSelectRed ? 0.005 : 0.008)));
            } else {
                currentRate = "0.5%";
                currentAward = AmountConverter.convertCentToString((long) (myAmount * 0.005));
            }
            selectResult = isSelectRed ? "RED" : "BLUE";
        }

        return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("redAmount", AmountConverter.convertCentToString(redSupportAmount))
                .put("blueAmount", AmountConverter.convertCentToString(blueSupportAmount))
                .put("redCount", redSupportLoginName.size())
                .put("blueCount", blueSupportLoginName.size())
                .put("isSelect", isSelect)
                .put("selectResult", selectResult)
                .put("myAmount", model == null ? "0" : AmountConverter.convertCentToString(model.getSumAnnualizedAmount()))
                .put("currentRate", currentRate)
                .put("currentAward", currentAward)
                .build());
    }

    public Map<String, Object> getTopFourTeam(String loginName) {
        Map<String, Object> map = new HashMap<>();
        if (!redisWrapperClient.exists(THIRD_ANNIVERSARY_TOP_FOUR_TEAM)) {
            map.put("isSuccess", false);
            return map;
        }
        List<String> teams = Arrays.asList(redisWrapperClient.get(THIRD_ANNIVERSARY_TOP_FOUR_TEAM).split(","));
        List<String> collectSuccessLoginNames = thirdAnniversaryDrawMapper.findLoginNameByCollectTopFour(teams);
        map.put("topFour", redisWrapperClient.get(THIRD_ANNIVERSARY_TOP_FOUR_TEAM_CHINESE));
        map.put("collectSuccess", collectSuccessLoginNames.size());
        map.put("isSuccess", !Strings.isNullOrEmpty(loginName) && collectSuccessLoginNames.stream().anyMatch(name -> name.equals(loginName)));
        return map;
    }

    public BaseResponse<List<ThirdAnniversaryDrawModel>> getTeamLogos(String loginName) {
        return new BaseResponse<List<ThirdAnniversaryDrawModel>>(thirdAnniversaryDrawMapper.findByLoginName(loginName));
    }

    public BaseResponse draw(String loginName) {

        if (StringUtils.isEmpty(loginName)) {
            return new BaseResponse("请登陆后再来开球吧！");
        }

        if (!duringDrawActivities()) {
            return new BaseResponse("不在活动时间范围内");
        }

        if (accountMapper.findByLoginName(loginName) == null) {
            return new BaseResponse("请实名认证后再来开球吧！");
        }

        int count = this.unUserDrawCount(loginName);

        if (count <= 0) {
            return new BaseResponse("您暂无开球机会，赢取机会后再来开球吧!");
        }

        List<ThirdAnniversaryDrawModel> models = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int random = (int) (Math.random() * 32 + 1);
            models.add(new ThirdAnniversaryDrawModel(loginName, FOOTBALL_TEAMS.get(random)));
        }

        redisWrapperClient.hset(MessageFormat.format(THIRD_ANNIVERSARY_EACH_EVERY_DAY_DRAW, loginName), DateTime.now().toString("yyyy-MM-dd"), "success", lifeSecond);
        thirdAnniversaryDrawMapper.create(models);

        return new BaseResponse<List<ThirdAnniversaryDrawModel>>(models.stream().collect(Collectors.groupingBy(ThirdAnniversaryDrawModel::getTeamName, Collectors.summingInt(ThirdAnniversaryDrawModel::getTeamCount)))
                .entrySet().stream().map(entry -> new ThirdAnniversaryDrawModel(loginName, entry.getKey(), entry.getValue())).collect(Collectors.toList()));

    }

    public int unUserDrawCount(String loginName){
        if (Strings.isNullOrEmpty(loginName)){
            return 0;
        }
        int usedDrawCount = thirdAnniversaryDrawMapper.countDrawByLoginName(loginName);

        ActivityInvestAnnualizedModel activityInvestAnnualizedModel = activityInvestAnnualizedMapper.findByActivityAndLoginName(ActivityInvestAnnualized.THIRD_ANNIVERSARY_ACTIVITY, loginName);
        int investDrawCount = activityInvestAnnualizedModel == null ? 0 : (int) (activityInvestAnnualizedModel.getSumInvestAmount() / EACH_INVEST_AMOUNT_50000);

        Map<String, String> eachEveryDayDraws = redisWrapperClient.hgetAll(MessageFormat.format(THIRD_ANNIVERSARY_EACH_EVERY_DAY_DRAW, loginName));
        int usedInvestDrawCount = usedDrawCount - eachEveryDayDraws.size();

        int isTodayDraw = eachEveryDayDraws.entrySet().stream().filter(entry -> entry.getKey().equals(DateTime.now().toString("yyyy-MM-dd"))).count() > 0 ? 0 : 1;
        return investDrawCount - usedInvestDrawCount + isTodayDraw;
    }

    public BaseResponse selectRedOrBlue(String loginName, boolean isRed) {
        if (!duringActivities()){
            return new BaseResponse("不在活动时间范围内");
        }
        if (Strings.isNullOrEmpty(loginName) || redisWrapperClient.hexists(THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE, loginName)) {
            return new BaseResponse("支持失败");
        }
        redisWrapperClient.hset(THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE, loginName, isRed ? "RED" : "BLUE");
        return new BaseResponse<Map<String, Object>>(this.selectResult(loginName));
    }

    public Map<String, Object> invite(String loginName) {
        ThirdAnniversaryHelpModel model = thirdAnniversaryHelpMapper.findByLoginName(loginName);
        if (model == null) {
            List<ActivityInvestModel> investModels = activityInvestMapper.findAllByActivityLoginNameAndTime(loginName, ActivityCategory.THIRD_ANNIVERSARY.name(), activityStartTime, new Date());
            long sumAnnualizedAmount = investModels.stream().mapToLong(ActivityInvestModel::getAnnualizedAmount).sum();
            return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                    .put("annualizedAmount", AmountConverter.convertCentToString(sumAnnualizedAmount))
                    .put("reward", "0.00")
                    .put("endTime", "")
                    .build());
        }
        List<ActivityInvestModel> investModels = activityInvestMapper.findAllByActivityLoginNameAndTime(loginName, ActivityCategory.THIRD_ANNIVERSARY.name(), activityStartTime, model.getEndTime());
        long sumAnnualizedAmount = investModels.stream().mapToLong(ActivityInvestModel::getAnnualizedAmount).sum();
        List<ThirdAnniversaryHelpInfoModel> helpInfoModels = thirdAnniversaryHelpInfoMapper.findByHelpId(model.getId());
        return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("annualizedAmount", AmountConverter.convertCentToString(sumAnnualizedAmount))
                .put("reward", AmountConverter.convertCentToString((long) (sumAnnualizedAmount * rates.get(helpInfoModels.size()))))
                .put("endTime", new DateTime(model.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"))
                .put("helpFriend", helpInfoModels)
                .build());
    }

    public void shareInvite(String loginName) {
        ThirdAnniversaryHelpModel model = thirdAnniversaryHelpMapper.findByLoginName(loginName);
        if (model != null) {
            return;
        }
        UserModel userModel = userMapper.findByLoginName(loginName);
//        ThirdAnniversaryHelpModel thirdAnniversaryHelpModel = new ThirdAnniversaryHelpModel(loginName, userModel.getMobile(), userModel.getUserName(), new Date(), DateTime.now().plusDays(3).toDate());
//        thirdAnniversaryHelpMapper.create(thirdAnniversaryHelpModel);
//        redisWrapperClient.hset(THIRD_ANNIVERSARY_WAIT_SEND_REWARD, String.valueOf(thirdAnniversaryHelpModel.getId()), DateTime.now().plusDays(3).toString("yyyy-MM-dd HH:mm:ss"));
        ThirdAnniversaryHelpModel thirdAnniversaryHelpModel = new ThirdAnniversaryHelpModel(loginName, userModel.getMobile(), userModel.getUserName(), new Date(), DateTime.now().plusMinutes(30).toDate());
        thirdAnniversaryHelpMapper.create(thirdAnniversaryHelpModel);
        redisWrapperClient.hset(THIRD_ANNIVERSARY_WAIT_SEND_REWARD, String.valueOf(thirdAnniversaryHelpModel.getId()), DateTime.now().plusMinutes(30).toString("yyyy-MM-dd HH:mm:ss"));
    }

    public Map<String, Object> sharePage(String loginName, String originator) {
        ThirdAnniversaryHelpModel helpModel = thirdAnniversaryHelpMapper.findByLoginName(originator);
        if (helpModel == null) {
            return null;
        }

        long sumAnnualizedAmount = activityInvestMapper.findAllByActivityLoginNameAndTime(originator, ActivityCategory.THIRD_ANNIVERSARY.name(), activityStartTime, helpModel.getEndTime()).stream().mapToLong(ActivityInvestModel::getAnnualizedAmount).sum();
        List<ThirdAnniversaryHelpInfoModel> helpInfoModels = thirdAnniversaryHelpInfoMapper.findByHelpId(helpModel.getId());
        boolean isHelp = !Strings.isNullOrEmpty(loginName) && helpInfoModels.stream().anyMatch(model -> model.getLoginName().equals(loginName));
        return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("endTime", new DateTime(helpModel.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"))
                .put("isHelp", isHelp)
                .put("helpFriend", helpInfoModels)
                .put("reward", AmountConverter.convertCentToString((long) (sumAnnualizedAmount * (isHelp ? rates.get(helpInfoModels.size()) : 0.005D))))
                .build());
    }

    public void openRedEnvelope(String loginName, String mobile, String originator) {
        ThirdAnniversaryHelpModel helpModel = thirdAnniversaryHelpMapper.findByLoginName(originator);
        List<ThirdAnniversaryHelpInfoModel> helpInfoModels = thirdAnniversaryHelpInfoMapper.findByHelpId(helpModel.getId());
        if (new Date().after(helpModel.getEndTime())){
            return;
        }
        if (helpInfoModels.size() >= 3) {
            return;
        }
        if (helpInfoModels.stream().anyMatch(model -> model.getLoginName().equals(loginName))) {
            return;
        }
        thirdAnniversaryHelpInfoMapper.create(new ThirdAnniversaryHelpInfoModel(helpModel.getId(), loginName, mobile, userMapper.findByLoginName(loginName).getUserName()));
    }

    private boolean duringDrawActivities() {
        return activityStartTime.before(new Date()) && activityDrawEndTime.after(new Date());
    }

    private boolean duringActivities() {
        return activityStartTime.before(new Date()) && activityEndTime.after(new Date());
    }

    public boolean isActivityRegister(String loginName){
        return "thirdAnniversary".equals(userMapper.findByLoginName(loginName).getChannel());
    }

    public boolean isAccount(String loginName) {
        return accountMapper.findByLoginName(loginName) != null;
    }
}
