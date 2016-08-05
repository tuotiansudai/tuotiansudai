package com.tuotiansudai.ask.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.*;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.ask.utils.PaginationUtil;
import com.tuotiansudai.repository.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseDto<BaseDataDto> createQuestion(String loginName, QuestionRequestDto questionRequestDto) {
        QuestionModel questionModel = new QuestionModel(loginName,
                questionRequestDto.getQuestion(),
                questionRequestDto.getAddition(),
                questionRequestDto.getTags());

        questionMapper.create(questionModel);

        return new BaseDto<>(new BaseDataDto(true, null));
    }

    @Override
    public BaseDto<BasePaginationDataDto> findAllQuestions(String loginName, int index, int pageSize) {
        long count = questionMapper.countAllQuestions(loginName);
        List<QuestionModel> allQuestions = questionMapper.findAllQuestions(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(index, pageSize, count, allQuestions);
    }

    @Override
    public BaseDto<BasePaginationDataDto> findAllUnresolvedQuestions(String loginName, int index, int pageSize) {
        long count = questionMapper.countAllUnresolvedQuestions(loginName);
        List<QuestionModel> allUnresolvedQuestions = questionMapper.findAllUnresolvedQuestions(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(index, pageSize, count, allUnresolvedQuestions);
    }

    @Override
    public BaseDto<BasePaginationDataDto> findAllHotQuestions(String loginName, int index, int pageSize) {
        long count = questionMapper.countAllQuestions(loginName);
        List<QuestionModel> allHotQuestions = questionMapper.findAllHotQuestions(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(index, pageSize, count, allHotQuestions);
    }

    private BaseDto<BasePaginationDataDto> generatePaginationData(int index, int pageSize, long count, List<QuestionModel> allQuestions) {
        List<QuestionPaginationItemDto> items = Lists.transform(allQuestions, new Function<QuestionModel, QuestionPaginationItemDto>() {
            @Override
            public QuestionPaginationItemDto apply(QuestionModel input) {
                return new QuestionPaginationItemDto(input, userMapper.findByLoginName(input.getLoginName()).getMobile());
            }
        });

        BasePaginationDataDto<QuestionPaginationItemDto> data = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        data.setStatus(true);
        return new BaseDto<BasePaginationDataDto>(data);
    }
}
