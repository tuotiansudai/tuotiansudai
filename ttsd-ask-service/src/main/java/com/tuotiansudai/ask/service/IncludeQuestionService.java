package com.tuotiansudai.ask.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.mapper.IncludeQuestionMapper;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.IncludeQuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.SiteMapDataDto;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Service
public class IncludeQuestionService {

    private static final Logger logger = Logger.getLogger(IncludeQuestionService.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String NO_INCLUDE_QUESTIONS = "web:no_include_ask_questions";

    private static final String prefix = "http://ask.tuotiansudai.com/question/";

    private static final int timeout = 24 * 60 * 60 * 1000;

    @Autowired
    private IncludeQuestionMapper includeQuestionMapper;

    @Autowired
    private QuestionMapper questionMapper;

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

    public IncludeQuestionModel getIncludeQuestionByQuestionId(long questionId) {
        IncludeQuestionModel includeQuestionModel = includeQuestionMapper.findByQuestionId(questionId);
        if (includeQuestionModel == null) {
            return null;
        }
        return includeQuestionModel;
    }

    public void create(IncludeQuestionModel includeQuestionModel){
        includeQuestionMapper.create(includeQuestionModel);
    }

    public List<SiteMapDataDto> getSiteMapData(){
        List<SiteMapDataDto> siteMapDataDtoList = Lists.newArrayList();
        if(redisWrapperClient.exists(NO_INCLUDE_QUESTIONS)){
            //从redis中去值
            Map<byte[], byte[]> siteMapListHkey = redisWrapperClient.hgetAllSeri(NO_INCLUDE_QUESTIONS);
            for (byte[] key : siteMapListHkey.keySet()) {
                try {
                    SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                    siteMapDataDto.setName(new String(siteMapListHkey.get(key), "UTF-8"));
                    siteMapDataDto.setLinkUrl(new String(key, "UTF-8"));
                    siteMapDataDtoList.add(siteMapDataDto);
                } catch (UnsupportedEncodingException e) {
                    logger.error("read redis error " + e);
                }
            }
        }
        else{
            //从数据库中取值,并放入redis
            List<QuestionModel> questionModelList = questionMapper.findApprovedNoInclude();
            for(QuestionModel questionModel : questionModelList){
                SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                siteMapDataDto.setName(questionModel.getQuestion());
                siteMapDataDto.setLinkUrl(prefix + questionModel.getId());
                siteMapDataDtoList.add(siteMapDataDto);
                redisWrapperClient.hset(NO_INCLUDE_QUESTIONS, prefix + questionModel.getId(), questionModel.getQuestion(), timeout);
            }
        }
        return siteMapDataDtoList;
    }

}
