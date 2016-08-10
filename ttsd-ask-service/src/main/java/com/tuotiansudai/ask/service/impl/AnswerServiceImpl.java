package com.tuotiansudai.ask.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.*;
import com.tuotiansudai.ask.repository.mapper.AnswerMapper;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.AnswerStatus;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.ask.utils.PaginationUtil;
import com.tuotiansudai.ask.utils.SensitiveWordsFilter;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private static final Logger logger = Logger.getLogger(AnswerServiceImpl.class);

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value(value = "${newAnswerAdoptedAlert}")
    private String newAnswerAdoptedAlertKey;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public boolean createAnswer(String loginName, AnswerRequestDto answerRequestDto) {
        long questionId = answerRequestDto.getQuestionId();
        QuestionModel questionModel = questionMapper.findById(questionId);
        if (questionModel == null) {
            return false;
        }

        AnswerModel answerModel = new AnswerModel(loginName,
                questionId,
                SensitiveWordsFilter.replace(answerRequestDto.getAnswer()));
        answerMapper.create(answerModel);
        return false;
    }

    @Override
    public void approve(String loginName, List<Long> answerIds) {
        for (long answerId : answerIds) {
            AnswerModel answerModel = answerMapper.findById(answerId);
            answerModel.setApprovedBy(loginName);
            answerModel.setApprovedTime(new Date());
            answerModel.setStatus(AnswerStatus.UNADOPTED);
            answerMapper.update(answerModel);
            QuestionModel questionModel = questionMapper.findById(answerModel.getQuestionId());
            questionModel.setAnswers(questionModel.getAnswers() + 1);
            questionMapper.update(questionModel);
        }
    }

    @Override
    public void reject(String loginName, List<Long> questionIds) {
        for (long questionId : questionIds) {
            AnswerModel answerModel = answerMapper.findById(questionId);
            answerModel.setRejectedBy(loginName);
            answerModel.setRejectedTime(new Date());
            answerModel.setStatus(AnswerStatus.REJECTED);
            answerMapper.update(answerModel);
        }
    }

    @Override
    public AnswerDto getBestAnswer(String loginName, long questionId) {
        AnswerModel bestAnswerModel = answerMapper.findBestAnswerByQuestionId(questionId);
        if (bestAnswerModel == null) {
            return null;
        }

        return new AnswerDto(bestAnswerModel,
                userMapper.findByLoginName(bestAnswerModel.getLoginName()).getMobile(),
                bestAnswerModel.getFavoredBy() != null && bestAnswerModel.getFavoredBy().contains(loginName),
                null);
    }

    @Override
    public List<AnswerDto> getAnswers(final String loginName, long questionId) {
        List<AnswerModel> answerModels = answerMapper.findByQuestionId(questionId);
        return Lists.transform(answerModels, new Function<AnswerModel, AnswerDto>() {
            @Override
            public AnswerDto apply(AnswerModel input) {
                return new AnswerDto(input,
                        userMapper.findByLoginName(input.getLoginName()).getMobile(),
                        input.getFavoredBy() != null && input.getFavoredBy().contains(loginName),
                        null);
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
        answerModel.setAdoptedTime(new Date());
        answerModel.setStatus(AnswerStatus.ADOPTED);
        answerMapper.update(answerModel);

        QuestionModel questionModel = questionMapper.findById(answerModel.getQuestionId());
        questionModel.setStatus(QuestionStatus.RESOLVED);
        questionMapper.update(questionModel);
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

        List<String> favoredBy = answerModel.getFavoredBy();
        if (favoredBy != null && favoredBy.contains(loginName)) {
            return false;
        }

        if (favoredBy == null) {
            answerModel.setFavoredBy(Lists.newArrayList(loginName));
        } else {
            answerModel.getFavoredBy().add(loginName);
        }
        answerMapper.update(answerModel);
        return true;
    }

    @Override
    public BaseDto<BasePaginationDataDto> findMyAnswers(String loginName, int index, int pageSize) {
        redisWrapperClient.hset(newAnswerAdoptedAlertKey, loginName, SIMPLE_DATE_FORMAT.format(new Date()));
        long count = answerMapper.countByLoginName(loginName);
        List<AnswerModel> myAnswers = answerMapper.findByLoginName(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);

        return generatePaginationData(loginName, index, pageSize, count, myAnswers);
    }

    @Override
    public BaseDto<BasePaginationDataDto> findAnswersForConsole(String question, String mobile, AnswerStatus status, int index, int pageSize) {
        UserModel userModel = userMapper.findByMobile(mobile);
        String loginName = userModel != null ? userModel.getLoginName() : null;
        long count = answerMapper.countAnswersForConsole(question, loginName, status);
        List<AnswerModel> answerModels = answerMapper.findAnswersForConsole(question, loginName, status, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(null, index, pageSize, count, answerModels);
    }



    @Override
    public boolean isNewAnswerAdoptedExists(String loginName) {
        Date now = new Date();
        boolean isKeyExists = redisWrapperClient.hexists(newAnswerAdoptedAlertKey, loginName);
        if (!isKeyExists) {
            redisWrapperClient.hset(newAnswerAdoptedAlertKey, loginName, SIMPLE_DATE_FORMAT.format(now));
            return false;
        }

        String value = redisWrapperClient.hget(newAnswerAdoptedAlertKey, loginName);
        final Date lastAlertTime;
        try {
            lastAlertTime = SIMPLE_DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage(), e);
            redisWrapperClient.hset(newAnswerAdoptedAlertKey, loginName, SIMPLE_DATE_FORMAT.format(now));
            return false;
        }

        List<AnswerModel> answerModels = answerMapper.findByLoginName(loginName, null, null);

        return Iterators.tryFind(answerModels.iterator(), new Predicate<AnswerModel>() {
            @Override
            public boolean apply(AnswerModel input) {
                return input.isBestAnswer() && input.getAdoptedTime().after(lastAlertTime);
            }
        }).isPresent();
    }

    private BaseDto<BasePaginationDataDto> generatePaginationData(final String loginName, int index, int pageSize, long count, List<AnswerModel> answers) {
        List<AnswerDto> items = Lists.transform(answers, new Function<AnswerModel, AnswerDto>() {
            @Override
            public AnswerDto apply(AnswerModel input) {
                QuestionModel questionModel = questionMapper.findById(input.getQuestionId());
                QuestionDto questionDto = new QuestionDto(questionModel, userMapper.findByLoginName(questionModel.getLoginName()).getMobile());
                return new AnswerDto(input,
                        userMapper.findByLoginName(input.getLoginName()).getMobile(),
                        input.getFavoredBy() != null && input.getFavoredBy().contains(loginName),
                        questionDto);
            }
        });

        BasePaginationDataDto<AnswerDto> data = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        data.setStatus(true);
        return new BaseDto<BasePaginationDataDto>(data);
    }
}
