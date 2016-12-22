package com.tuotiansudai.ask.service;

import com.tuotiansudai.ask.repository.mapper.IncludeQuestionMapper;
import com.tuotiansudai.ask.repository.model.IncludeQuestionModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncludeQuestionService {

    private static final Logger logger = Logger.getLogger(IncludeQuestionService.class);

    @Autowired
    private IncludeQuestionMapper includeQuestionMapper;

    public IncludeQuestionModel getIncludeQuestion(long id) {
        IncludeQuestionModel includeQuestionModel = includeQuestionMapper.findById(id);
        if (includeQuestionModel == null) {
            return null;
        }
        return includeQuestionModel;
    }

    public BaseDto<BasePaginationDataDto> findAllIncludeQuestions(int index, int pageSize) {
        long count = includeQuestionMapper.countAllIncludeQuestions();
        List<IncludeQuestionModel> allIncludeQuestions = includeQuestionMapper.findAllIncludeQuestions(PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        BasePaginationDataDto<IncludeQuestionModel> data = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, allIncludeQuestions);
        data.setStatus(true);
        return new BaseDto<>(data);
    }


}
