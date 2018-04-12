package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
import com.tuotiansudai.activity.repository.model.WeChatHelpType;
import com.tuotiansudai.activity.repository.model.WeChatHelpView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityConsoleInviteHelpService {

    @Autowired
    private WeChatHelpMapper weChatHelpMapper;


    public BasePaginationDataDto<WeChatHelpView> investRewardList(int index, int pageSize, String KeyWord, Long minInvest, Long maxInvest) {
        List<WeChatHelpView> weChatHelpViews = weChatHelpMapper.findByKeyWord(KeyWord, minInvest, maxInvest, WeChatHelpType.INVEST_HELP);
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


}
