package com.tuotiansudai.rest.ask.controller;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.rest.ask.service.QuestionService;
import com.tuotiansudai.rest.authenticate.RestUserInfo;
import com.tuotiansudai.rest.authenticate.UserInfoRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    @UserInfoRequired
    public ResponseEntity<QuestionModel> create(@RequestBody QuestionRequestDto questionPostDto) {
        QuestionModel questionModel = questionService.create(questionPostDto, RestUserInfo.getCurrentLoginName());
        return ResponseEntity.created(URI.create("/questions/" + questionModel.getId()))
                .body(questionModel);
    }
}
