package com.tuotiansudai.rest.client;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import feign.RequestLine;

@RestClient(url = "${ask.rest.server}")
public interface AskRestClient {

    @RequestLine("POST /questions")
    QuestionModel createQuestion(QuestionRequestDto questionPostDto) throws RestException;
}
