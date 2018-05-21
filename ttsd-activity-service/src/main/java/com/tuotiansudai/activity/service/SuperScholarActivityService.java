package com.tuotiansudai.activity.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.BaseResponse;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.SuperScholarRewardMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
import com.tuotiansudai.activity.repository.model.SuperScholarRewardModel;
import com.tuotiansudai.activity.repository.model.SuperScholarRewardView;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SuperScholarActivityService {

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private SuperScholarRewardMapper superScholarRewardMapper;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Autowired
    private UserMapper userMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.endTime}\")}")
    private Date activityEndTime;

    private static final String QUESTIONS = "questions.json";

    public List<SuperScholarRewardView> activityHome(String loginName) {
//        List<ActivityInvestModel> models = activityInvestMapper.findAllByActivityLoginNameAndTime(loginName, ActivityCategory.SUPER_SCHOLAR_ACTIVITY.name(), activityStartTime, DateTime.now().withTimeAtStartOfDay().minusMillis(1).toDate());
        List<ActivityInvestModel> models = activityInvestMapper.findAllByActivityLoginNameAndTime(loginName, ActivityCategory.SUPER_SCHOLAR_ACTIVITY.name(), activityStartTime, DateTime.now().toDate());
        return models.stream()
                .filter(model -> superScholarRewardMapper.findByLoginNameAndAnswerTime(loginName, model.getCreatedTime()) != null)
                .map(model -> {
                    SuperScholarRewardModel superScholarRewardModel = superScholarRewardMapper.findByLoginNameAndAnswerTime(loginName, model.getCreatedTime());
                    double rewardRate = superScholarRewardModel.getRewardRate();
                    return new SuperScholarRewardView(
                            model.getUserName(),
                            model.getMobile(),
                            AmountConverter.convertCentToString(model.getInvestAmount()),
                            AmountConverter.convertCentToString(model.getAnnualizedAmount()),
                            String.format("%.1f", rewardRate * 100) + "%",
                            AmountConverter.convertCentToString((long) (model.getAnnualizedAmount() * rewardRate)),
                            model.getCreatedTime());

                }).collect(Collectors.toList());
    }

    public boolean doQuestion(String loginName) {
        return superScholarRewardMapper.findByLoginNameAndAnswerTime(loginName, new Date()) != null;
    }

    @Transactional
    public BaseResponse getQuestions(String loginName) throws IOException {
        SuperScholarRewardModel superScholarRewardModel = superScholarRewardMapper.findByLoginNameAndCreatedTime(loginName, new Date());
        InputStream inputStream = SuperScholarActivityService.class.getClassLoader().getResourceAsStream(QUESTIONS);
        Map<String, Object> map = new ObjectMapper().readValue(inputStream, new TypeReference<HashMap<String, Object>>() {
        });
        HashSet<String> randomQuestion = new HashSet<>();
        randomSet(randomQuestion);
        List<String> questionIndex = new ArrayList<>(randomQuestion);
        List<String> answers = new ArrayList<>();
        List<Map<String, Object>> questions = questionIndex.stream()
                .map(index -> {
                    HashMap<String, Object> values = (HashMap<String, Object>) map.get(index);
                    answers.add((String) values.get("answer"));
                    HashMap<String, String> options = (HashMap<String, String>) values.get("options");
                    return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                            .put("question", values.get("question"))
                            .put("options", options.entrySet().stream().map(entry -> (entry.getKey() + "、" + entry.getValue())).collect(Collectors.toList()))
                            .build());
                })
                .collect(Collectors.toList());

        if (superScholarRewardModel == null) {
            superScholarRewardMapper.create(new SuperScholarRewardModel(loginName, String.join(",", questionIndex), String.join(",", answers)));
        } else {
            superScholarRewardModel.setQuestionIndex(String.join(",", questionIndex));
            superScholarRewardModel.setQuestionAnswer(String.join(",", answers));
            superScholarRewardMapper.update(superScholarRewardModel);
        }
        BaseResponse baseResponse = new BaseResponse(true);
        baseResponse.setData(questions);
        return baseResponse;
    }

    public boolean submitAnswer(String loginName, String answer) {
        SuperScholarRewardModel superScholarRewardModel = superScholarRewardMapper.findByLoginNameAndCreatedTime(loginName, new Date());
        List<String> userAnswer = Arrays.asList(answer.split(","));
        if (superScholarRewardModel == null || userAnswer.size() < 5) {
            return false;
        }

        int userRight = 0;
        List<String> rightAnswer = Arrays.asList(superScholarRewardModel.getQuestionAnswer().split(","));
        for (int i = 0; i < rightAnswer.size(); i++) {
            userRight = userRight + (userAnswer.get(i).toUpperCase().equals(rightAnswer.get(i).toUpperCase()) ? 1 : 0);
        }

        long couponId = getCouponId();
        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":" + couponId);

        superScholarRewardModel.setUserAnswer(answer);
        superScholarRewardModel.setUserRight(userRight);
        superScholarRewardModel.setAnswerTime(new Date());
        superScholarRewardModel.setCouponId(couponId);
        superScholarRewardMapper.update(superScholarRewardModel);
        return true;
    }

    public Map<String, Object> viewResult(String loginName) {
        SuperScholarRewardModel superScholarRewardModel = superScholarRewardMapper.findByLoginNameAndAnswerTime(loginName, new Date());
        if (superScholarRewardModel == null){
            return null;
        }
        return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("rate", String.format("%.1f", superScholarRewardModel.getRewardRate() * 100) + "%")
                .put("questionAnswer", Lists.newArrayList(superScholarRewardModel.getQuestionAnswer().split(",")))
                .put("userAnswer", Lists.newArrayList(superScholarRewardModel.getUserAnswer().split(",")))
                .put("coupon", superScholarRewardModel.getCouponId())
                .build());
    }

    public boolean shareSuccess(String loginName) {
        SuperScholarRewardModel superScholarRewardModel = superScholarRewardMapper.findByLoginNameAndAnswerTime(loginName, new Date());
        if (superScholarRewardModel == null){
            return false;
        }
        superScholarRewardModel.setShareHome(true);
        superScholarRewardMapper.update(superScholarRewardModel);
        return true;
    }

    public boolean mobileExists(String mobile) {
        return userMapper.findByMobile(mobile) != null;
    }

    public String getReferrerInfo(String mobile) {
        UserModel referrer = userMapper.findByMobile(mobile);
        if (!StringUtils.isEmpty(referrer.getUserName())) {
            return StringUtils.leftPad(StringUtils.right(referrer.getUserName(), 1), referrer.getUserName().length(), "*");
        }
        return referrer.getMobile().substring(0, 3) + "****" + referrer.getMobile().substring(7);
    }

    private void randomSet(HashSet<String> set) {
        if (set.size() == 5) {
            return;
        }
        set.add(String.valueOf((int) (Math.random() * 60) + 1));

        if (set.size() < 5) {
            randomSet(set);
        }
    }

    public long getCouponId() {
        int random = (int) (Math.random() * 100);
        if (random >= 0 && random < 30) {
            return 495L;
        }
        return 496L;
    }

    public boolean duringActivities() {
        return activityStartTime.before(new Date()) && activityEndTime.after(new Date());
    }
}
