package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.NationalMidAutumnMapper;
import com.tuotiansudai.activity.repository.model.ActivityInvestRewardView;
import com.tuotiansudai.activity.repository.model.NationalMidAutumnView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityConsoleNationalMidAutumnService {

    @Autowired
    private NationalMidAutumnMapper nationalMidAutumnMapper;

    public BasePaginationDataDto<ActivityInvestRewardView> list(int index, int pageSize) {
        List<NationalMidAutumnView> nationalMidAutumnViews = nationalMidAutumnMapper.findAll();
        int count = nationalMidAutumnViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, nationalMidAutumnViews.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }

}
