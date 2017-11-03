package com.tuotiansudai.rest.ask;

import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.QuestionDto;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.rest.ask.service.QuestionService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional("askTransactionManager")
public class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    public void shouldEncodeMobileIsOk() {
        String loginName = "caiBuDao";
        String mobile = "15500001111";
        String question = "testQuestion";
        QuestionModel questionModel = getQuestionModel(loginName, mobile, question);
        questionModel.setStatus(QuestionStatus.UNRESOLVED);
        questionMapper.create(questionModel);

        BaseDto<BasePaginationDataDto> basePaginationDataDtoBaseDto = questionService.getQuestionsByKeywords("testQuestion", loginName, 1);

        List<QuestionDto> questionDtoList = basePaginationDataDtoBaseDto.getData().getRecords();
        assertTrue(CollectionUtils.isNotEmpty(questionDtoList));
        assertTrue(questionDtoList.get(0).getMobile().equals(mobile));

        basePaginationDataDtoBaseDto = questionService.getQuestionsByKeywords("testQuestion", "test1", 1);
        questionDtoList = basePaginationDataDtoBaseDto.getData().getRecords();
        assertTrue(!questionDtoList.get(0).getMobile().equals(mobile));
    }

    private QuestionModel getQuestionModel(String loginName, String mobile, String question) {
        return new QuestionModel(loginName, mobile, "fakeMobile", question, "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
    }
}
