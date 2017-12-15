package com.tuotiansudai.ask.service;

import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.AnswerDto;
import com.tuotiansudai.ask.dto.AnswerRequestDto;
import com.tuotiansudai.ask.dto.AnswerResultDataDto;
import com.tuotiansudai.ask.dto.QuestionDto;
import com.tuotiansudai.ask.repository.mapper.AnswerMapper;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.AnswerStatus;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.utils.FakeMobileUtil;
import com.tuotiansudai.ask.utils.SensitiveWordsFilter;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.MobileEncoder;
import com.tuotiansudai.util.PaginationUtil;
import com.tuotiansudai.util.RedisWrapperClient;
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
public class AnswerService {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final Logger logger = Logger.getLogger(AnswerService.class);

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String newAnswerAdoptedAlertKey = "ask:new-answer-adopted-alert";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CaptchaHelperService captchaHelperService;

    public AnswerResultDataDto createAnswer(String loginName, AnswerRequestDto answerRequestDto) {
        AnswerResultDataDto answerResultDataDto = new AnswerResultDataDto();

        long questionId = answerRequestDto.getQuestionId();
        QuestionModel questionModel = questionMapper.findById(questionId);
        if (questionModel == null) {
            return answerResultDataDto;
        }

        if (!captchaHelperService.captchaVerify(answerRequestDto.getCaptcha())) {
            return answerResultDataDto;
        }
        answerResultDataDto.setCaptchaValid(true);

        if (SensitiveWordsFilter.match(answerRequestDto.getAnswer())) {
            return answerResultDataDto;
        }
        answerResultDataDto.setAnswerSensitiveValid(true);

        UserModel userModel = userMapper.findByLoginName(loginName);
        AnswerModel answerModel = new AnswerModel(loginName,
                userModel.getMobile(),
                FakeMobileUtil.generateFakeMobile(userModel.getMobile()),
                questionId,
                SensitiveWordsFilter.replace(answerRequestDto.getAnswer()));
        answerMapper.create(answerModel);

        answerResultDataDto.setStatus(true);

        return answerResultDataDto;
    }

    public void approve(String loginName, List<Long> answerIds) {
        for (long answerId : answerIds) {
            AnswerModel answerModel = answerMapper.findById(answerId);
            answerModel.setApprovedBy(loginName);
            answerModel.setApprovedTime(new Date());
            answerModel.setStatus(AnswerStatus.UNADOPTED);
            answerMapper.update(answerModel);
            QuestionModel questionModel = questionMapper.findById(answerModel.getQuestionId());
            questionModel.setAnswers(questionModel.getAnswers() + 1);
            questionModel.setLastAnsweredTime(new Date());
            questionMapper.update(questionModel);
        }
    }

    public void reject(String loginName, List<Long> questionIds) {
        for (long questionId : questionIds) {
            AnswerModel answerModel = answerMapper.findById(questionId);
            answerModel.setRejectedBy(loginName);
            answerModel.setRejectedTime(new Date());
            answerModel.setStatus(AnswerStatus.REJECTED);
            answerMapper.update(answerModel);
        }
    }

    public AnswerDto getBestAnswer(String loginName, long questionId) {
        AnswerModel bestAnswerModel = answerMapper.findBestAnswerByQuestionId(questionId);
        if (bestAnswerModel == null) {
            return null;
        }

        return new AnswerDto(bestAnswerModel,
                bestAnswerModel.getLoginName().equalsIgnoreCase(loginName) ? bestAnswerModel.getMobile() : MobileEncoder.encode(Strings.isNullOrEmpty(bestAnswerModel.getFakeMobile()) ? bestAnswerModel.getMobile() : bestAnswerModel.getFakeMobile()),
                bestAnswerModel.getFavoredBy() != null && bestAnswerModel.getFavoredBy().contains(loginName),
                null);
    }

    public BaseDto<BasePaginationDataDto> getNotBestAnswers(final String loginName, long questionId, int index, int pageSize) {
        long count = answerMapper.countNotBestByQuestionId(loginName, questionId);
        List<AnswerModel> notBestAnswerModels = answerMapper.findNotBestByQuestionId(loginName, questionId, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(loginName, index, pageSize, count, notBestAnswerModels, true);
    }

    @Transactional
    public boolean makeBestAnswer(long answerId) {
        AnswerModel answerModel = answerMapper.lockById(answerId);
        if (answerModel == null) {
            return false;
        }

        QuestionModel questionModel = questionMapper.lockById(answerModel.getQuestionId());

        AnswerModel bestAnswer = answerMapper.findBestAnswerByQuestionId(answerModel.getQuestionId());
        if (bestAnswer != null) {
            return false;
        }

        answerModel.setBestAnswer(true);
        answerModel.setAdoptedTime(new Date());
        answerModel.setStatus(AnswerStatus.ADOPTED);
        answerMapper.update(answerModel);

        questionModel.setStatus(QuestionStatus.RESOLVED);
        questionMapper.update(questionModel);
        return true;
    }

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

    public BaseDto<BasePaginationDataDto> findMyAnswers(String loginName, int index, int pageSize) {
        redisWrapperClient.hset(newAnswerAdoptedAlertKey, loginName, SIMPLE_DATE_FORMAT.format(new Date()));
        long count = answerMapper.countByLoginName(loginName);
        List<AnswerModel> myAnswers = answerMapper.findByLoginName(loginName, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);

        return generatePaginationData(loginName, index, pageSize, count, myAnswers, false);
    }

    public BaseDto<BasePaginationDataDto> findAnswersForConsole(String question, String mobile, AnswerStatus status, int index, int pageSize) {
        long count = answerMapper.countAnswersForConsole(question, mobile, status);
        List<AnswerModel> answerModels = answerMapper.findAnswersForConsole(question, mobile, status, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return generatePaginationData(null, index, pageSize, count, answerModels, false);
    }

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

        return Iterators.tryFind(answerModels.iterator(), input -> input.isBestAnswer() && input.getAdoptedTime().after(lastAlertTime)).isPresent();
    }

    private BaseDto<BasePaginationDataDto> generatePaginationData(final String loginName, int index, int pageSize, long count, List<AnswerModel> answers, final boolean isEncodeMobile) {
        List<AnswerDto> items = Lists.transform(answers, input -> {
            QuestionModel questionModel = questionMapper.findById(input.getQuestionId());
            QuestionDto questionDto = new QuestionDto(questionModel,
                    isEncodeMobile && !questionModel.getLoginName().equalsIgnoreCase(loginName) ? (MobileEncoder.encode(Strings.isNullOrEmpty(questionModel.getFakeMobile()) ? questionModel.getMobile() : questionModel.getFakeMobile())) : questionModel.getMobile());
            return new AnswerDto(input,
                    isEncodeMobile ? MobileEncoder.encode(Strings.isNullOrEmpty(input.getFakeMobile()) ? input.getMobile() : input.getFakeMobile()) : input.getMobile(),
                    input.getFavoredBy() != null && input.getFavoredBy().contains(loginName),
                    questionDto);
        });

        BasePaginationDataDto<AnswerDto> data = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        data.setStatus(true);
        return new BaseDto<>(data);
    }
}
