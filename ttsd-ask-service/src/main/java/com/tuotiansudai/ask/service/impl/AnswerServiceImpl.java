package com.tuotiansudai.ask.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.*;
import com.tuotiansudai.ask.repository.mapper.AnswerMapper;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.ask.utils.PaginationUtil;
import com.tuotiansudai.repository.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public boolean createAnswer(String loginName, AnswerRequestDto answerRequestDto) {
        long questionId = answerRequestDto.getQuestionId();
        QuestionModel questionModel = questionMapper.findById(questionId);
        if (questionModel == null) {
            return false;
        }

        AnswerModel answerModel = new AnswerModel(loginName, questionId, answerRequestDto.getAnswer());
        answerMapper.create(answerModel);
        return false;
    }

    @Override
    public AnswerDto getBestAnswer(String loginName, long questionId) {
        AnswerModel bestAnswerModel = answerMapper.findBestAnswerByQuestionId(questionId);
        if (bestAnswerModel == null) {
            return null;
        }

        return new AnswerDto(bestAnswerModel,
                userMapper.findByLoginName(bestAnswerModel.getLoginName()).getMobile(),
                bestAnswerModel.getFavoredBy().contains(loginName));
    }

    @Override
    public List<AnswerDto> getAnswers(final String loginName, long questionId) {
        List<AnswerModel> answerModels = answerMapper.findByQuestionId(questionId);
        return Lists.transform(answerModels, new Function<AnswerModel, AnswerDto>() {
            @Override
            public AnswerDto apply(AnswerModel input) {
                return new AnswerDto(input,
                        userMapper.findByLoginName(input.getLoginName()).getMobile(),
                        input.getFavoredBy().contains(loginName));
            }
        });
    }

    @Override
    @Transactional
    public boolean makeBestAnswer(long answerId) {
        AnswerModel answerModel = answerMapper.lockById(answerId);
        if (answerModel == null) {
            return false;
        }
        AnswerModel bestAnswer = answerMapper.findBestAnswerByQuestionId(answerModel.getQuestionId());
        if (bestAnswer != null) {
            return false;
        }

        answerModel.setBestAnswer(true);
        answerMapper.update(answerModel);
        return true;
    }

    @Override
    @Transactional
    public boolean favor(String loginName, long answerId) {
        AnswerModel answerModel = answerMapper.lockById(answerId);
        if (answerModel == null) {
            return false;
        }

        if (userMapper.findByLoginName(loginName) == null) {
            return false;
        }

        if (answerModel.getFavoredBy().contains(loginName)) {
            return false;
        }

        answerModel.getFavoredBy().add(loginName);
        answerMapper.update(answerModel);
        return true;
    }

    @Override
    public BaseDto<BasePaginationDataDto> findMyAnswers(String loginName, int index, int pageSize) {
        long count = answerMapper.countByLoginName(loginName);
        List<AnswerModel> myAnswers = answerMapper.findByLoginName(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(index, pageSize, count, myAnswers);
    }

    private BaseDto<BasePaginationDataDto> generatePaginationData(int index, int pageSize, long count, List<AnswerModel> answers) {
        List<MyAnswerDto> items = Lists.transform(answers, new Function<AnswerModel, MyAnswerDto>() {
            @Override
            public MyAnswerDto apply(AnswerModel input) {
                QuestionModel questionModel = questionMapper.findById(input.getQuestionId());
                QuestionDto questionDto = new QuestionDto(questionModel, userMapper.findByLoginName(questionModel.getLoginName()).getMobile());
                return new MyAnswerDto(input, questionDto);
            }
        });

        BasePaginationDataDto<MyAnswerDto> data = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        data.setStatus(true);
        return new BaseDto<BasePaginationDataDto>(data);
    }
}
