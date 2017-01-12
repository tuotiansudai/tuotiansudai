package com.tuotiansudai.rest.client;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.rest.client.exceptions.RestException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "askRestClient", url = "${ask.rest.server}")
public interface AskRestClient {

    @RequestMapping(value = "/questions", method = RequestMethod.POST)
    QuestionModel createQuestion(@RequestBody QuestionRequestDto questionPostDto) throws RestException;
}
