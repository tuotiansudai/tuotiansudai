package com.tuotiansudai.rest.ask.controller;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.rest.ask.service.QuestionService;
import com.tuotiansudai.rest.authenticate.RestUserInfo;
import com.tuotiansudai.rest.authenticate.UserInfoRequired;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    @UserInfoRequired
    public ResponseEntity<QuestionModel> create(@Valid @RequestBody QuestionRequestDto questionPostDto) {
        QuestionModel questionModel = questionService.create(questionPostDto, RestUserInfo.getCurrentLoginName());
        return ResponseEntity.created(URI.create("/question/" + questionModel.getId()))
                .body(questionModel);
    }

    @RequestMapping(value = "/approve", method = RequestMethod.PUT)
    @UserInfoRequired
    public ResponseEntity approve(@RequestBody List<Long> questionIds) {
        questionService.approve(RestUserInfo.getCurrentLoginName(), questionIds);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/reject", method = RequestMethod.PUT)
    @UserInfoRequired
    public ResponseEntity reject(@RequestBody List<Long> questionIds) {
        questionService.reject(RestUserInfo.getCurrentLoginName(), questionIds);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/{questionId:^\\d+$}", method = RequestMethod.GET)
    public ResponseEntity<QuestionModel> getQuestion(@PathVariable long questionId) {
        QuestionModel questionModel = questionService.getQuestion(questionId);
        if (questionModel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(questionModel);
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<BaseDto<BasePaginationDataDto>> findAllQuestions(@Param(value = "index") int index) {
        BaseDto<BasePaginationDataDto> questionModels = questionService.findAllQuestions(RestUserInfo.getCurrentLoginName(), index);
        return ResponseEntity.ok(questionModels);
    }

    @RequestMapping(value = "/unresolved", method = RequestMethod.GET)
    public ResponseEntity<BaseDto<BasePaginationDataDto>> findAllUnresolvedQuestions(@Param(value = "index") int index) {
        BaseDto<BasePaginationDataDto> questionModels = questionService.findAllUnresolvedQuestions(RestUserInfo.getCurrentLoginName(), index);
        return ResponseEntity.ok(questionModels);
    }

    @RequestMapping(value = "/hot", method = RequestMethod.GET)
    public ResponseEntity<BaseDto<BasePaginationDataDto>> findAllHotQuestions(@Param(value = "index") int index) {
        BaseDto<BasePaginationDataDto> questionModels = questionService.findAllHotQuestions(RestUserInfo.getCurrentLoginName(), index);
        return ResponseEntity.ok(questionModels);
    }

    @RequestMapping(value = "/mine", method = RequestMethod.GET)
    @UserInfoRequired
    public ResponseEntity<BaseDto<BasePaginationDataDto>> findMyQuestions(@Param(value = "index") int index) {
        BaseDto<BasePaginationDataDto> questionModels = questionService.findMyQuestions(RestUserInfo.getCurrentLoginName(), index);
        return ResponseEntity.ok(questionModels);
    }

    @RequestMapping(value = "/tag/{tag:^[A-Z0-9_]+$}", method = RequestMethod.GET)
    public ResponseEntity<BaseDto<BasePaginationDataDto>> findByTag(@PathVariable Tag tag, @Param(value = "index") int index) {
        BaseDto<BasePaginationDataDto> questionModels = questionService.findByTag(RestUserInfo.getCurrentLoginName(), tag, index);
        return ResponseEntity.ok(questionModels);
    }

    @RequestMapping(value = "/console", method = RequestMethod.GET)
    @UserInfoRequired
    public ResponseEntity<BaseDto<BasePaginationDataDto>> findQuestionsForConsole(@Param(value = "question") String question,
                                                                                  @Param(value = "mobile") String mobile,
                                                                                  @Param(value = "status") QuestionStatus status,
                                                                                  @Param(value = "index") int index,
                                                                                  @Param(value = "pageSize") int pageSize) {
        BaseDto<BasePaginationDataDto> questionModels = questionService.findQuestionsForConsole(question, mobile, status, index, pageSize);
        return ResponseEntity.ok(questionModels);
    }

    @RequestMapping(value = "/isNewAnswerExists", method = RequestMethod.GET)
    @UserInfoRequired
    public ResponseEntity<Boolean> isNewAnswerExists() {
        boolean newAnswerExists = questionService.isNewAnswerExists(RestUserInfo.getCurrentLoginName());
        return ResponseEntity.ok(newAnswerExists);
    }

    @RequestMapping(value = "/byKeywords", method = RequestMethod.GET)
    public ResponseEntity<BaseDto<BasePaginationDataDto>> getQuestionsByKeywords(@Param(value = "keywords") String keywords, @Param(value = "index") int index) {
        BaseDto<BasePaginationDataDto> questionsByKeywords = questionService.getQuestionsByKeywords(keywords, RestUserInfo.getCurrentLoginName(), index);
        return ResponseEntity.ok(questionsByKeywords);
    }

    @RequestMapping(value = "/embodyAll", method = RequestMethod.GET)
    public ResponseEntity<BaseDto<BasePaginationDataDto>> findEmbodyAllQuestions(@Param(value = "index") int index) {
        BaseDto<BasePaginationDataDto> questionsByKeywords = questionService.findEmbodyAllQuestions(index);
        return ResponseEntity.ok(questionsByKeywords);
    }

    @RequestMapping(value = "/updateEmbody/{questionId:^\\d+$}", method = RequestMethod.PUT)
    public ResponseEntity findEmbodyAllQuestions(@PathVariable long questionId) {
        questionService.updateEmbodyById(questionId);
        return ResponseEntity.accepted().build();
    }

}
