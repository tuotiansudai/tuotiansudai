package com.tuotiansudai.console.activity.service;

import com.google.common.base.Joiner;
import com.tuotiansudai.activity.repository.dto.NotWorkDto;
import com.tuotiansudai.activity.repository.mapper.NotWorkMapper;
import com.tuotiansudai.activity.repository.model.NotWorkModel;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.UserRegisterInfo;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleNotWorkService {
    @Autowired
    NotWorkMapper notWorkMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    BankAccountMapper bankAccountMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.endTime}\")}")
    private Date activityEndTime;

    final private Map<Long, String> rewardMap = new HashMap<Long, String>() {{
        put(300000L, "20元红包");
        put(800000L, "30元话费");
        put(3000000L, "京东E卡");
        put(5000000L, "300元旅游基金(芒果卡)");
        put(10000000L, "索尼数码相机");
        put(20000000L, "联想YOGA 平板3代");
        put(30000000L, "CAN看尚42英寸液晶电视");
        put(52000000L, "锤子手机M1");
        put(80000000L, "浪琴手表康卡斯系列");
        put(120000000L, "Apple MacBook Air笔记本电脑");
    }};

    public BasePaginationDataDto<NotWorkDto> findNotWorkPagination(int index, int pageSize) {
        long count = notWorkMapper.findAllCount();
        List<NotWorkModel> notWorkModels = notWorkMapper.findPagination(PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        List<NotWorkDto> records = notWorkModels.stream().map(notWorkModel -> {
            NotWorkDto notWorkDto = new NotWorkDto(notWorkModel);
            List<String> rewardList = new ArrayList<>();
            rewardMap.forEach((k, v) -> {
                if (k <= notWorkModel.getInvestAmount()) {
                    rewardList.add(v);
                }
            });
            if (rewardList.size() > 0) {
                notWorkDto.setRewards(Joiner.on("、").join(rewardList));
            }
            List<UserRegisterInfo> users = userMapper.findAllUsersByRegisterTimeAndReferrer(activityStartTime, activityEndTime, notWorkModel.getLoginName());
            notWorkDto.setRecommendedRegisterAmount(String.valueOf(users.size()));

            int recommendIdentifyAmount = 0;
            for (UserRegisterInfo userModel : users) {
                BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(userModel.getLoginName());
                if (null != bankAccountModel && bankAccountModel.getCreatedTime().after(activityStartTime) && bankAccountModel.getCreatedTime().before(activityEndTime)) {
                    ++recommendIdentifyAmount;
                }
            }
            notWorkDto.setRecommendedIdentifyAmount(String.valueOf(recommendIdentifyAmount));

            return notWorkDto;
        }).collect(Collectors.toList());
        return new BasePaginationDataDto<>(index, pageSize, count, records);
    }
}
