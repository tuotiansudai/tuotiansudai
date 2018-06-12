package com.tuotiansudai.console.activity.service;

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
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleThirdAnniversaryService {

    @Autowired
    private ThirdAnniversaryHelpMapper thirdAnniversaryHelpMapper;

    @Autowired
    private ThirdAnniversaryHelpInfoMapper thirdAnniversaryHelpInfoMapper;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.third.anniversary.startTime}\")}")
    private Date activityStartTime;

    private final Map<Integer, Double> rates = Maps.newHashMap(ImmutableMap.<Integer, Double>builder()
            .put(0, 0D)
            .put(1, 0.001D)
            .put(2, 0.002D)
            .put(3, 0.005D)
            .build());

    public BasePaginationDataDto list(int index, int pageSize) {
        List<ThirdAnniversaryHelpModel> models = thirdAnniversaryHelpMapper.findAll();
        List<ThirdAnniversaryHelpView> views = models.stream().map(model->{
            List<ActivityInvestModel> investModels = activityInvestMapper.findAllByActivityLoginNameAndTime(model.getLoginName(), ActivityCategory.THIRD_ANNIVERSARY.name(), activityStartTime, model.getEndTime());
            long investAmount = investModels.stream().mapToLong(ActivityInvestModel::getInvestAmount).sum();
            long annualizedAmount = investModels.stream().mapToLong(ActivityInvestModel::getAnnualizedAmount).sum();
            List<ThirdAnniversaryHelpInfoModel> helpInfoModels = thirdAnniversaryHelpInfoMapper.findByHelpId(model.getId());
            int count = helpInfoModels.size();
            String friends = helpInfoModels.stream().map(infoModel->infoModel.getMobile()).collect(Collectors.joining(","));
            return new ThirdAnniversaryHelpView(model, AmountConverter.convertCentToString(investAmount), AmountConverter.convertCentToString(annualizedAmount), count, count == 0 ? "0": String.format("%.1f", rates.get(count) * 100), AmountConverter.convertCentToString((long) (annualizedAmount * rates.get(count))), friends);
        }).collect(Collectors.toList());

        int count = views.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        return new BasePaginationDataDto(index, pageSize, count, views.subList(startIndex, endIndex));
    }
}
