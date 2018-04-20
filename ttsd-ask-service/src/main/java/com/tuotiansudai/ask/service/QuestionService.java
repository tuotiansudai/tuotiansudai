package com.tuotiansudai.ask.service;

import com.google.common.base.Strings;
import com.tuotiansudai.ask.dto.QuestionDto;
import com.tuotiansudai.ask.dto.QuestionResultDataDto;
import com.tuotiansudai.ask.dto.QuestionWithCaptchaRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.ask.utils.SensitiveWordsFilter;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.rest.client.AskRestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import com.tuotiansudai.util.MobileEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private static final Logger logger = Logger.getLogger(QuestionService.class);

    @Autowired
    private CaptchaHelperService captchaHelperService;

    @Autowired
    private AskRestClient askRestClient;

    @Autowired
    private BaiDuMaWebMasterService baiDuMaWebMasterService;

    private static final String urlTemplate = "https://tuotiansudai.com/ask/question/%s";

    public QuestionResultDataDto createQuestion(QuestionWithCaptchaRequestDto questionRequestDto) {
        QuestionResultDataDto dataDto = new QuestionResultDataDto();
        if (!captchaHelperService.captchaVerify(questionRequestDto.getCaptcha())) {
            return dataDto;
        }
        dataDto.setCaptchaValid(true);

        String questionSensitive = SensitiveWordsFilter.matchSensitiveWords(questionRequestDto.getQuestion());
        if (questionSensitive != null) {
            dataDto.setSensitiveWord(questionSensitive);
            return dataDto;
        }
        dataDto.setQuestionSensitiveValid(true);

        String additionSensitive = SensitiveWordsFilter.matchSensitiveWords(questionRequestDto.getAddition());
        if (additionSensitive != null) {
            dataDto.setSensitiveWord(additionSensitive);
            return dataDto;
        }
        dataDto.setAdditionSensitiveValid(true);

        try {
            askRestClient.createQuestion(questionRequestDto);
            dataDto.setStatus(true);
        } catch (Exception e) {
            logger.error("create question failed", e);
        }
        return dataDto;
    }

    public void approve(List<Long> questionIds) {
        try {
            askRestClient.approveQuestion(questionIds);
            baiDuMaWebMasterService.sendBaiDuWebMaster(questionIds.stream().map(questionId -> String.format(urlTemplate,String.valueOf(questionId))).collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error("approve questions failed", e);
        }
    }

    public void reject(List<Long> questionIds) {
        try {
            askRestClient.rejectQuestion(questionIds);
        } catch (Exception e) {
            logger.error("reject questions failed", e);
        }
    }

    public QuestionDto getQuestion(String loginName, long questionId) {
        try {
            QuestionModel questionModel = askRestClient.getQuestion(questionId);
            return new QuestionDto(questionModel,
                    questionModel.getLoginName().equalsIgnoreCase(loginName) ? questionModel.getMobile() : MobileEncoder.encode(Strings.isNullOrEmpty(questionModel.getFakeMobile()) ? questionModel.getMobile() : questionModel.getFakeMobile()));
        } catch (RestException e) {
            logger.error("get question fail, ", e);
            return null;
        }
    }

    public BaseDto<BasePaginationDataDto<QuestionModel>> findAllQuestions(int index) {
        try {
            BaseDto<BasePaginationDataDto<QuestionModel>> questionModels = askRestClient.findAllQuestions(index);
            return questionModels;
        } catch (RestException e) {
            logger.error("get all questions fail, ", e);
            return null;
        }
    }

    public BaseDto<BasePaginationDataDto<QuestionModel>> findAllUnresolvedQuestions(int index) {
        try {
            BaseDto<BasePaginationDataDto<QuestionModel>> questionModels = askRestClient.findAllUnresolvedQuestions(index);
            return questionModels;
        } catch (RestException e) {
            logger.error("get all unresolved questions fail, ", e);
            return null;
        }
    }

    public BaseDto<BasePaginationDataDto<QuestionModel>> findAllHotQuestions(int index) {
        try {
            BaseDto<BasePaginationDataDto<QuestionModel>> questionModels = askRestClient.findAllHotQuestions(index);
            return questionModels;
        } catch (RestException e) {
            logger.error("get all hot questions fail, ", e);
            return null;
        }
    }

    public BaseDto<BasePaginationDataDto<QuestionModel>> findMyQuestions(int index) {
        try {
            BaseDto<BasePaginationDataDto<QuestionModel>> questionModels = askRestClient.findMyQuestions(index);
            return questionModels;
        } catch (RestException e) {
            logger.error("get my questions fail, ", e);
            return null;
        }
    }

    public BaseDto<BasePaginationDataDto<QuestionModel>> findByTag(Tag tag, int index) {
        try {
            BaseDto<BasePaginationDataDto<QuestionModel>> questionModels = askRestClient.findByTag(tag, index);
            return questionModels;
        } catch (RestException e) {
            logger.error("get questions by tag fail, ", e);
            return null;
        }
    }

    public BaseDto<BasePaginationDataDto<QuestionModel>> findQuestionsForConsole(String question, String mobile, QuestionStatus status, int index) {
        try {
            BaseDto<BasePaginationDataDto<QuestionModel>> questionModels = askRestClient.findQuestionsForConsole(question, mobile, status, index, 10);
            return questionModels;
        } catch (RestException e) {
            logger.error("find questions for console fail, ", e);
            return null;
        }
    }

    public Boolean isNewAnswerExists() {
        try {
            Boolean newAnswerExists = askRestClient.isNewAnswerExists();
            return newAnswerExists;
        } catch (RestException e) {
            logger.error("find is new answer exists fail, ", e);
            return null;
        }
    }

    public BaseDto<BasePaginationDataDto<QuestionModel>> getQuestionsByKeywords(String keywords, int index) {
        try {
            BaseDto<BasePaginationDataDto<QuestionModel>> questionsByKeywords = askRestClient.getQuestionsByKeywords(keywords, index);
            return questionsByKeywords;
        } catch (RestException e) {
            logger.error("get questions by keywords fail, ", e);
            return null;
        }
    }

    public QuestionModel findById(long id) {
        try {
            QuestionModel questionModel = askRestClient.getQuestion(id);
            return questionModel;
        } catch (RestException e) {
            logger.error("get question by id fail, ", e);
            return null;
        }
    }

    public BaseDto<BasePaginationDataDto<QuestionModel>> findEmbodyAllQuestions(int index) {
        try {
            BaseDto<BasePaginationDataDto<QuestionModel>> embodyAllQuestions = askRestClient.findEmbodyAllQuestions(index);
            return embodyAllQuestions;
        } catch (RestException e) {
            logger.error("get embody all questions fail, ", e);
            return null;
        }
    }

    public void updateEmbodyById(long questionId) {
        try {
            askRestClient.updateEmbody(questionId);
        } catch (Exception e) {
            logger.error("update embody by id failed", e);
        }
    }

}

