package com.tuotiansudai.rest.ask.service;

import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.QuestionDto;
import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.dto.EmbodyQuestionDto;
import com.tuotiansudai.ask.repository.mapper.AnswerMapper;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.ask.utils.FakeMobileUtil;
import com.tuotiansudai.rest.ask.utils.SensitiveWordsFilter;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.MobileEncoder;
import com.tuotiansudai.util.PaginationUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String newAnswerAlertKey = "ask:new-answer-alert";

    private final QuestionMapper questionMapper;

    private final UserMapper userMapper;

    private final AnswerMapper answerMapper;

    private static final int PAGE_SIZE = 10;


    @Autowired
    public QuestionService(QuestionMapper questionMapper, UserMapper userMapper, AnswerMapper answerMapper) {
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
        this.answerMapper = answerMapper;
    }

    public QuestionModel create(QuestionRequestDto questionRequestDto, String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        String mobile = userModel.getMobile();
        QuestionModel questionModel = new QuestionModel(loginName, mobile,
                FakeMobileUtil.generateFakeMobile(mobile),
                SensitiveWordsFilter.replace(questionRequestDto.getQuestion()),
                SensitiveWordsFilter.replace(questionRequestDto.getAddition()),
                questionRequestDto.getTags());
        questionMapper.create(questionModel);
        return questionModel;
    }

    public void approve(String loginName, List<Long> questionIds) {
        for (long questionId : questionIds) {
            QuestionModel questionModel = questionMapper.findById(questionId);
            questionModel.setApprovedBy(loginName);
            questionModel.setApprovedTime(new Date());
            questionModel.setStatus(QuestionStatus.UNRESOLVED);
            questionMapper.update(questionModel);
        }

    }

    public void reject(String loginName, List<Long> questionIds) {
        for (long questionId : questionIds) {
            QuestionModel questionModel = questionMapper.findById(questionId);
            questionModel.setRejectedBy(loginName);
            questionModel.setRejectedTime(new Date());
            questionModel.setStatus(QuestionStatus.REJECTED);
            questionMapper.update(questionModel);
        }
    }

    public QuestionModel getQuestion(long questionId) {
        return questionMapper.findById(questionId);
    }

    public BaseDto<BasePaginationDataDto> findAllQuestions(String loginName, int index) {
        long count = questionMapper.countAllQuestions(loginName);
        List<QuestionModel> allQuestions = questionMapper.findAllQuestions(loginName, PaginationUtil.calculateOffset(index, PAGE_SIZE, count), PAGE_SIZE);
        return generatePaginationData(loginName, index, PAGE_SIZE, count, allQuestions, true);
    }

    public BaseDto<BasePaginationDataDto> findAllUnresolvedQuestions(String loginName, int index) {
        long count = questionMapper.countAllUnresolvedQuestions(loginName);
        List<QuestionModel> allUnresolvedQuestions = questionMapper.findAllUnresolvedQuestions(loginName, PaginationUtil.calculateOffset(index, PAGE_SIZE, count), PAGE_SIZE);
        return generatePaginationData(loginName, index, PAGE_SIZE, count, allUnresolvedQuestions, true);
    }

    public BaseDto<BasePaginationDataDto> findAllHotQuestions(String loginName, int index) {
        long count = questionMapper.countAllQuestions(loginName);
        List<QuestionModel> allHotQuestions = questionMapper.findAllHotQuestions(loginName, PaginationUtil.calculateOffset(index, PAGE_SIZE, count), PAGE_SIZE);
        return generatePaginationData(loginName, index, PAGE_SIZE, count, allHotQuestions, true);
    }

    public BaseDto<BasePaginationDataDto> findMyQuestions(String loginName, int index) {
        redisWrapperClient.hset(newAnswerAlertKey, loginName, SIMPLE_DATE_FORMAT.format(new Date()));
        long count = questionMapper.countByLoginName(loginName);
        List<QuestionModel> myQuestions = questionMapper.findByLoginName(loginName, PaginationUtil.calculateOffset(index, PAGE_SIZE, count), PAGE_SIZE);
        return generatePaginationData(loginName, index, PAGE_SIZE, count, myQuestions, false);
    }

    public BaseDto<BasePaginationDataDto> findByTag(String loginName, Tag tag, int index) {
        long count = questionMapper.countByTag(loginName, tag);
        List<QuestionModel> questions = questionMapper.findByTag(loginName, tag, PaginationUtil.calculateOffset(index, PAGE_SIZE, count), PAGE_SIZE);
        return generatePaginationData(loginName, index, PAGE_SIZE, count, questions, true);
    }

    public BaseDto<BasePaginationDataDto> findQuestionsForConsole(String question, String mobile, QuestionStatus status, int index, int pageSize) {
        long count = questionMapper.countQuestionsForConsole(question, mobile, status);
        List<QuestionModel> myQuestions = questionMapper.findQuestionsForConsole(question, mobile, status, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(null, index, pageSize, count, myQuestions, false);
    }

    public boolean isNewAnswerExists(String loginName) {
        Date now = new Date();
        boolean isKeyExists = redisWrapperClient.hexists(newAnswerAlertKey, loginName);
        if (!isKeyExists) {
            redisWrapperClient.hset(newAnswerAlertKey, loginName, SIMPLE_DATE_FORMAT.format(now));
            return false;
        }

        String value = redisWrapperClient.hget(newAnswerAlertKey, loginName);
        final Date lastAlertTime;
        try {
            lastAlertTime = SIMPLE_DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage(), e);
            redisWrapperClient.hset(newAnswerAlertKey, loginName, SIMPLE_DATE_FORMAT.format(now));
            return false;
        }

        List<QuestionModel> questions = questionMapper.findByLoginName(loginName, null, null);
        for (QuestionModel question : questions) {
            List<AnswerModel> answerModels = answerMapper.findByQuestionId(loginName, question.getId());
            if (Iterators.tryFind(answerModels.iterator(), input -> input.getCreatedTime().after(lastAlertTime)).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public BaseDto<BasePaginationDataDto> getQuestionsByKeywords(String keywords, String loginName, int index) {
        long count = questionMapper.countQuestionsByKeywords(keywords);
        List<QuestionModel> questionModels = questionMapper.findQuestionsByKeywords(keywords, PaginationUtil.calculateOffset(index, 10, count), 10);
        return generatePaginationData(loginName, index, 10, count, questionModels, true);
    }

    public BaseDto<BasePaginationDataDto> findEmbodyAllQuestions(int index) {
        long count = questionMapper.countEmbodyAllQuestions();
        List<QuestionModel> embodyAllQuestions = questionMapper.findEmbodyAllQuestions(PaginationUtil.calculateOffset(index, PAGE_SIZE, count), PAGE_SIZE);
        return generateEmbodyPaginationData(index, count, embodyAllQuestions);
    }


    private BaseDto<BasePaginationDataDto> generatePaginationData(final String loginName, int index, int pageSize, long count, List<QuestionModel> questionModels, final boolean isEncodeMobile) {
        List<QuestionDto> items = Lists.transform(questionModels, input -> new QuestionDto(input,
                isEncodeMobile && !input.getLoginName().equalsIgnoreCase(loginName) ?
                        (MobileEncoder.encode(Strings.isNullOrEmpty(input.getFakeMobile()) ? input.getMobile() : input.getFakeMobile())) : input.getMobile()));

        BasePaginationDataDto<QuestionDto> data = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        data.setStatus(true);
        return new BaseDto<>(data);
    }

    private BaseDto<BasePaginationDataDto> generateEmbodyPaginationData(int index, long count, List<QuestionModel> questionModels) {
        List<EmbodyQuestionDto> items = Lists.transform(questionModels, input -> new EmbodyQuestionDto(input));
        BasePaginationDataDto<EmbodyQuestionDto> data = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, PAGE_SIZE, count), PAGE_SIZE, count, items);
        data.setStatus(true);
        return new BaseDto<>(data);
    }

    public void updateEmbodyById(long questionId) {
        QuestionModel questionModel = questionMapper.findById(questionId);
        questionModel.setEmbody(true);
        questionMapper.update(questionModel);
    }


}
