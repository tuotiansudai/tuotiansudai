package com.tuotiansudai.ask.repository.aspect;

import com.tuotiansudai.ask.repository.mapper.AnswerMapper;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AspectAnswerMapper {

    @Autowired
    private AnswerMapper answerMapper;

    @Before(value = "(execution(* com.tuotiansudai.ask.repository.mapper.AnswerMapper.create(..)) || execution(* com.tuotiansudai.ask.repository.mapper.AnswerMapper.update(..)))")
    public void initCharset(){
        answerMapper.initCharset();
    }

}
