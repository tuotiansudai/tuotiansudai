package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.SuperScholarRewardMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
import com.tuotiansudai.activity.repository.model.SuperScholarRewardModel;
import com.tuotiansudai.activity.repository.model.SuperScholarRewardView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleSuperScholarService {

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Autowired
    private SuperScholarRewardMapper superScholarRewardMapper;

    public BasePaginationDataDto<SuperScholarRewardView> list(String keyWord, int index, int pageSize) {
        List<ActivityInvestModel> activityInvestModels = activityInvestMapper.findByUserNameOrMobile(keyWord, ActivityCategory.SUPER_SCHOLAR_ACTIVITY.name());
        List<SuperScholarRewardView> list = activityInvestModels.stream()
                .filter(model -> superScholarRewardMapper.findByLoginNameAndAnswerTime(model.getLoginName(), model.getCreatedTime()) != null)
                .map(model -> {
                    SuperScholarRewardModel superScholarRewardModel = superScholarRewardMapper.findByLoginNameAndAnswerTime(model.getLoginName(), model.getCreatedTime());
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
        int count = list.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * pageSize;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        return new BasePaginationDataDto(index, pageSize, count, list.subList(startIndex, endIndex));
    }
}
