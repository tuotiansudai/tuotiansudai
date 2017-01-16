package com.tuotiansudai.rest.client;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.rest.client.exceptions.RestException;
import feign.RequestLine;

public interface AskRestClient {

    @RequestLine("POST /questions")
    QuestionModel createQuestion(QuestionRequestDto questionPostDto) throws RestException;
}
