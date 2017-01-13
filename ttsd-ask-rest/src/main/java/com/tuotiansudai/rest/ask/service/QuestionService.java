package com.tuotiansudai.rest.ask.service;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.ask.utils.FakeMobileUtil;
import com.tuotiansudai.rest.ask.utils.SensitiveWordsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionMapper questionMapper;

    private final UserMapper userMapper;

    @Autowired
    public QuestionService(QuestionMapper questionMapper, UserMapper userMapper) {
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
    }

    public QuestionModel create(QuestionRequestDto questionRequestDto, String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        String mobile = userModel.getMobile();
        QuestionModel questionModel = new QuestionModel(loginName, mobile,
                FakeMobileUtil.generateFakeMobile(mobile),
                SensitiveWordsFilter.replace(questionRequestDto.getQuestion()),
                SensitiveWordsFilter.replace(questionRequestDto.getAddition()),
                questionRequestDto.getTags());
        questionMapper.create(questionModel);
        return questionModel;
    }
}
