package com.tuotiansudai.ask.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.*;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.ask.utils.PaginationUtil;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    public void approve(String loginName, List<Long> questionIds) {
        for (long questionId : questionIds) {
            QuestionModel questionModel = questionMapper.findById(questionId);
            questionModel.setApprovedBy(loginName);
            questionModel.setApprovedTime(new Date());
            questionModel.setStatus(QuestionStatus.UNRESOLVED);
            questionMapper.update(questionModel);
        }
    }

    @Override
    public void reject(String loginName, List<Long> questionIds) {
        for (long questionId : questionIds) {
            QuestionModel questionModel = questionMapper.findById(questionId);
            questionModel.setRejectedBy(loginName);
            questionModel.setRejectedTime(new Date());
            questionModel.setStatus(QuestionStatus.REJECTED);
            questionMapper.update(questionModel);
        }
    }

    @Override
    public QuestionDto getQuestion(long questionId) {
        QuestionModel questionModel = questionMapper.findById(questionId);
        if (questionModel == null) {
            return null;
        }

        return new QuestionDto(questionModel, userMapper.findByLoginName(questionModel.getLoginName()).getMobile());
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

    @Override
    public BaseDto<BasePaginationDataDto> findMyQuestions(String loginName, int index, int pageSize) {
        long count = questionMapper.countByLoginName(loginName);
        List<QuestionModel> myQuestions = questionMapper.findByLoginName(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(index, pageSize, count, myQuestions);
    }

    @Override
    public BaseDto<BasePaginationDataDto> findByTag(String loginName, Tag tag, int index, int pageSize) {
        long count = questionMapper.countByTag(loginName, tag);
        List<QuestionModel> myQuestions = questionMapper.findByTag(loginName, tag, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(index, pageSize, count, myQuestions);
    }

    @Override
    public BaseDto<BasePaginationDataDto> findQuestionsForConsole(String question, String mobile, QuestionStatus status, int index, int pageSize) {
        UserModel userModel = userMapper.findByMobile(mobile);
        String loginName = userModel != null ? userModel.getLoginName() : null;
        long count = questionMapper.countQuestionsForConsole(question, loginName, status);
        List<QuestionModel> myQuestions = questionMapper.findQuestionsForConsole(question, loginName, status, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(index, pageSize, count, myQuestions);
    }

    private BaseDto<BasePaginationDataDto> generatePaginationData(int index, int pageSize, long count, List<QuestionModel> questionModels) {
        List<QuestionDto> items = Lists.transform(questionModels, new Function<QuestionModel, QuestionDto>() {
            @Override
            public QuestionDto apply(QuestionModel input) {
                return new QuestionDto(input, userMapper.findByLoginName(input.getLoginName()).getMobile());
            }
        });

        BasePaginationDataDto<QuestionDto> data = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        data.setStatus(true);
        return new BaseDto<BasePaginationDataDto>(data);
    }
}
