package com.tuotiansudai.rest.client;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "askRestClient", url = "http://localhost:8080")
public interface AskRestClient {

    @RequestMapping(value = "/questions", method = RequestMethod.POST)
    QuestionModel create(@RequestBody QuestionRequestDto questionPostDto);

    @RequestMapping(value = "/questions", method = RequestMethod.POST)
    QuestionModel create(@RequestBody QuestionRequestDto questionPostDto, @RequestHeader("userId") String userId);

    @RequestMapping(value = "/questions", method = RequestMethod.POST)
    QuestionModel create(@RequestBody QuestionRequestDto questionPostDto, @RequestHeader("userId") String userId, @RequestHeader("requestId") String requestId);
}
