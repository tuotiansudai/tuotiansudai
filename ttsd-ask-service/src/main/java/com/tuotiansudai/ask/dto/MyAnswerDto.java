package com.tuotiansudai.ask.dto;

import com.tuotiansudai.ask.repository.model.AnswerModel;

public class MyAnswerDto extends AnswerDto {

    private QuestionDto question;

    public MyAnswerDto(AnswerModel answerModel, QuestionDto questionDto) {
        super(answerModel, null, false);
        this.question = questionDto;
    }

    public QuestionDto getQuestion() {
        return question;
    }
}
