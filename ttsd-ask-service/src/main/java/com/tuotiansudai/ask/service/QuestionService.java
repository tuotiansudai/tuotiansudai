package com.tuotiansudai.ask.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.*;
import com.tuotiansudai.ask.repository.mapper.AnswerMapper;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.ask.utils.MobileEncoder;
import com.tuotiansudai.ask.utils.PaginationUtil;
import com.tuotiansudai.ask.utils.SensitiveWordsFilter;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class QuestionService {

    private static final Logger logger = Logger.getLogger(QuestionService.class);

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value(value = "${newAnswerAlert}")
    private String newAnswerAlertKey;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private CaptchaHelperService captchaHelperService;

    public QuestionResultDataDto createQuestion(String loginName, QuestionRequestDto questionRequestDto) {
        QuestionResultDataDto dataDto = new QuestionResultDataDto();
        if (!captchaHelperService.captchaVerify(questionRequestDto.getCaptcha())) {
            return dataDto;
        }
        dataDto.setCaptchaValid(true);

        if (SensitiveWordsFilter.match(questionRequestDto.getQuestion())) {
            return dataDto;
        }
        dataDto.setQuestionSensitiveValid(true);

        if (SensitiveWordsFilter.match(questionRequestDto.getAddition())) {
            return dataDto;
        }
        dataDto.setAdditionSensitiveValid(true);

        QuestionModel questionModel = new QuestionModel(loginName,
                SensitiveWordsFilter.replace(questionRequestDto.getQuestion()),
                SensitiveWordsFilter.replace(questionRequestDto.getAddition()),
                questionRequestDto.getTags());

        questionMapper.create(questionModel);

        dataDto.setStatus(true);

        return dataDto;
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

    public QuestionDto getQuestion(String loginName, long questionId) {
        QuestionModel questionModel = questionMapper.findById(questionId);
        if (questionModel == null) {
            return null;
        }

        String mobile = userMapper.findByLoginName(questionModel.getLoginName()).getMobile();
        return new QuestionDto(questionModel,
                questionModel.getLoginName().equalsIgnoreCase(loginName) ? mobile : MobileEncoder.encode(mobile));
    }

    public BaseDto<BasePaginationDataDto> findAllQuestions(String loginName, int index, int pageSize) {
        long count = questionMapper.countAllQuestions(loginName);
        List<QuestionModel> allQuestions = questionMapper.findAllQuestions(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(loginName, index, pageSize, count, allQuestions, true);
    }

    public BaseDto<BasePaginationDataDto> findAllUnresolvedQuestions(String loginName, int index, int pageSize) {
        long count = questionMapper.countAllUnresolvedQuestions(loginName);
        List<QuestionModel> allUnresolvedQuestions = questionMapper.findAllUnresolvedQuestions(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(loginName, index, pageSize, count, allUnresolvedQuestions, true);
    }

    public BaseDto<BasePaginationDataDto> findAllHotQuestions(String loginName, int index, int pageSize) {
        long count = questionMapper.countAllQuestions(loginName);
        List<QuestionModel> allHotQuestions = questionMapper.findAllHotQuestions(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(loginName, index, pageSize, count, allHotQuestions, true);
    }

    public BaseDto<BasePaginationDataDto> findMyQuestions(String loginName, int index, int pageSize) {
        redisWrapperClient.hset(newAnswerAlertKey, loginName, SIMPLE_DATE_FORMAT.format(new Date()));
        long count = questionMapper.countByLoginName(loginName);
        List<QuestionModel> myQuestions = questionMapper.findByLoginName(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(loginName, index, pageSize, count, myQuestions, false);
    }

    public BaseDto<BasePaginationDataDto> findByTag(String loginName, Tag tag, int index, int pageSize) {
        long count = questionMapper.countByTag(loginName, tag);
        List<QuestionModel> questions = questionMapper.findByTag(loginName, tag, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(loginName, index, pageSize, count, questions, false);
    }

    public BaseDto<BasePaginationDataDto> findQuestionsForConsole(String question, String mobile, QuestionStatus status, int index, int pageSize) {
        UserModel userModel = userMapper.findByMobile(mobile);
        String loginName = userModel != null ? userModel.getLoginName() : null;
        long count = questionMapper.countQuestionsForConsole(question, loginName, status);
        List<QuestionModel> myQuestions = questionMapper.findQuestionsForConsole(question, loginName, status, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
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
            if (Iterators.tryFind(answerModels.iterator(), new Predicate<AnswerModel>() {
                @Override
                public boolean apply(AnswerModel input) {
                    return input.getCreatedTime().after(lastAlertTime);
                }
            }).isPresent()) {
                return true;
            }
        }

        return false;
    }

    private BaseDto<BasePaginationDataDto> generatePaginationData(final String loginName, int index, int pageSize, long count, List<QuestionModel> questionModels, final boolean isEncodeMobile) {
        List<QuestionDto> items = Lists.transform(questionModels, new Function<QuestionModel, QuestionDto>() {
            @Override
            public QuestionDto apply(QuestionModel input) {
                String mobile = userMapper.findByLoginName(input.getLoginName()).getMobile();
                return new QuestionDto(input,
                        isEncodeMobile && !input.getLoginName().equalsIgnoreCase(loginName) ? MobileEncoder.encode(mobile) : mobile);
            }
        });

        BasePaginationDataDto<QuestionDto> data = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        data.setStatus(true);
        return new BaseDto<BasePaginationDataDto>(data);
    }
}
