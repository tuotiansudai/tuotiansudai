package com.tuotiansudai.ask.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.SiteMapDataDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmbodyQuestionService {

    private static final Logger logger = Logger.getLogger(EmbodyQuestionService.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @Value("${ask.server}")
    private String askServer;

    private static final String NO_EMBODY_QUESTIONS = "web:no_embody_ask_questions";

    private static final String CMS_CATEGORY = "cms:no_embody_questions";

    private static final String PREFIX = "/question/";

    private static final int timeout = 24 * 60 * 60;

    private static final int DETAIL_ORDER = 3;

    public List<SiteMapDataDto> getAskSiteMapData(Tag tag) {
        List<SiteMapDataDto> siteMapDataDtoList = Lists.newArrayList();
        if (redisWrapperClient.hexists(NO_EMBODY_QUESTIONS, String.valueOf(tag))) {
            //从redis中取值
           String value = redisWrapperClient.hget(NO_EMBODY_QUESTIONS, String.valueOf(tag));
            String[] arrValue = value.split("--");
            for (int i =0;i < arrValue.length;i++) {
                try {
                    SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                    siteMapDataDto.setName(arrValue[i].substring(arrValue[i].indexOf("|") + 1, arrValue[i].length()));
                    siteMapDataDto.setLinkUrl(arrValue[i].substring(0, arrValue[i].indexOf("|")));
                    siteMapDataDto.setSeq(DETAIL_ORDER);
                    siteMapDataDtoList.add(siteMapDataDto);
                } catch (Exception e) {
                    logger.error("read redis error " + e);
                }
            }
        } else {
            //从数据库中取值,并放入redis
            List<QuestionModel> questionModelList = questionMapper.findApprovedNotEmbodyQuestions(tag);
            String value = "";
            for (QuestionModel questionModel : questionModelList) {
                SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                siteMapDataDto.setName(questionModel.getQuestion());
                siteMapDataDto.setLinkUrl(askServer + PREFIX + questionModel.getId());
                siteMapDataDto.setSeq(DETAIL_ORDER);
                value += askServer + PREFIX + questionModel.getId()+ "|" + questionModel.getQuestion() + "--";
                siteMapDataDtoList.add(siteMapDataDto);
                redisWrapperClient.hset(NO_EMBODY_QUESTIONS, String.valueOf(tag), value, timeout);
            }
        }
        return siteMapDataDtoList;
    }

    public BaseDataDto createImportEmbodyQuestion(HttpServletRequest httpServletRequest) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        InputStream inputStream = null;
        BaseDataDto baseDataDto = new BaseDataDto();
        List<String> listSuccess = new ArrayList<>();
        List<String> listFailed = new ArrayList<>();
        if (!multipartFile.getOriginalFilename().endsWith(".csv")) {
            return new BaseDataDto(false, "上传失败!文件必须是csv格式");
        }
        try {
            inputStream = multipartFile.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String strVal;
            while (null != (strVal = bufferedReader.readLine())) {
                String questionId = strVal.substring(strVal.lastIndexOf("/") + 1);
                if (!strVal.startsWith(askServer + PREFIX)) {
                    listFailed.add(strVal);
                    continue;
                }
                QuestionModel questionModel = questionService.findById(Long.parseLong(questionId));
                if (questionModel != null && !questionModel.isEmbody() && (questionModel.getStatus() == QuestionStatus.RESOLVED || questionModel.getStatus() == QuestionStatus.UNRESOLVED)) {
                    questionService.updateEmbodyById(Long.parseLong(questionId));
                    listSuccess.add(strVal);
                } else {
                    listFailed.add(strVal);
                }
            }
        } catch (IOException e) {
            return new BaseDataDto(false, "上传失败!文件内容错误");
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
        baseDataDto.setStatus(true);
        baseDataDto.setMessage("批量导入完成! 其中 " + listSuccess.size() + " 条导入成功, " + listFailed.size() + " 条导入失败");
        //清除缓存
        if (listSuccess.size() > 0) {
            redisWrapperClient.del(NO_EMBODY_QUESTIONS);
        }
        return baseDataDto;
    }
}
