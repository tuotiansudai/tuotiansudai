package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.HelpCenterSearchListResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.HelpCenterSearchRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppHelpCenterSearchService;
import com.tuotiansudai.enums.HelpCategory;
import com.tuotiansudai.repository.mapper.HelpCenterMapper;
import com.tuotiansudai.repository.model.HelpCenterModel;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MobileAppHelpCenterSearchResultServiceTest extends ServiceTestBase {

    @Autowired
    HelpCenterMapper helpCenterMapper;

    @Autowired
    MobileAppHelpCenterSearchService mobileAppHelpCenterSearchService;

    private HelpCenterModel createHelpCenterModel(String title, String content, HelpCategory helpCategory, boolean hot) {
        HelpCenterModel helpCenterModel = new HelpCenterModel(IdGenerator.generate(), title, content, helpCategory, hot);
        helpCenterMapper.create(helpCenterModel);
        return helpCenterModel;
    }

    private List<HelpCenterModel> prepareData(){
        List<HelpCenterModel> helpCenterModels = new ArrayList<>();
        HelpCenterModel helpCenterModel1 = createHelpCenterModel("testQuestion1", "testAnster1", HelpCategory.ACCOUNT_BANK_CARD, false);
        helpCenterModels.add(helpCenterModel1);
        HelpCenterModel helpCenterModel2 = createHelpCenterModel("testQuestion2", "testAnster1", HelpCategory.COUPON_REFERRER, true);
        helpCenterModels.add(helpCenterModel2);
        HelpCenterModel helpCenterModel3 = createHelpCenterModel("testQuestion3", "testAnster2", HelpCategory.KNOW_TTSD, true);
        helpCenterModels.add(helpCenterModel3);
        HelpCenterModel helpCenterModel4 = createHelpCenterModel("testQuestion4", "testAnster3", HelpCategory.POINT_MEMBERSHIP, false);
        helpCenterModels.add(helpCenterModel4);
        HelpCenterModel helpCenterModel5 = createHelpCenterModel("testQuestion5", "testAnster4", HelpCategory.RECHARGE_WITHDRAW, false);
        helpCenterModels.add(helpCenterModel5);
        return helpCenterModels;
    }

    @Test
    public void testGetAppHelpCenterSearchOnlyByHotResponseData() {
        prepareData();

        HelpCenterSearchRequestDto requestDto = new HelpCenterSearchRequestDto();
        requestDto.setHot("true");
        requestDto.setKeywords("Question");
        requestDto.setCategory("");

        BaseResponseDto<HelpCenterSearchListResponseDataDto> baseResponseDto = mobileAppHelpCenterSearchService.getHelpCenterSearchResult(requestDto);
        assertEquals(2, baseResponseDto.getData().getSearchList().size());
    }

    @Test
    public void testGetAppHelpCenterSearchOnlyByCategoryResponseData() {
        prepareData();
        HelpCenterSearchRequestDto requestDto = new HelpCenterSearchRequestDto();
        requestDto.setHot("false");
        requestDto.setKeywords("Question");
        requestDto.setCategory("know_ttsd");

        BaseResponseDto<HelpCenterSearchListResponseDataDto> baseResponseDto = mobileAppHelpCenterSearchService.getHelpCenterSearchResult(requestDto);
        assertEquals(1, baseResponseDto.getData().getSearchList().size());
    }

    @Test
    public void testGetAppHelpCenterSearchOnlyByKeywordsResponseData() {
        prepareData();
        HelpCenterSearchRequestDto requestDto = new HelpCenterSearchRequestDto();
        requestDto.setHot("false");
        requestDto.setKeywords("Question");
        requestDto.setCategory("");

        BaseResponseDto<HelpCenterSearchListResponseDataDto> baseResponseDto = mobileAppHelpCenterSearchService.getHelpCenterSearchResult(requestDto);
        assertEquals(5, baseResponseDto.getData().getSearchList().size());
    }
}
