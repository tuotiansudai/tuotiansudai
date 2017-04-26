package com.tuotiansudai.ask.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.SiteMapDataDto;
import com.tuotiansudai.util.RedisWrapperClient;
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

@Service
public class EmbodyQuestionService {

    private static final Logger logger = Logger.getLogger(EmbodyQuestionService.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @Value("${ask.server}")
    private String askServer;

    private static final String NO_EMBODY_QUESTIONS = "web:no_embody_ask_questions";

    private static final String PREFIX = "/question/";

    private static final int timeout = 24 * 60 * 60;

    private static final int DETAIL_ORDER = 3;

    private static final String ASK_COUNT_SEPARATOR = "\1";

    private static final String ASK_CONTENT_SEPARATOR = "\0";

    public List<SiteMapDataDto> getAskSiteMapData(Tag tag) {
        List<SiteMapDataDto> askSiteMapDataDtoList = Lists.newArrayList();
        if (redisWrapperClient.hexists(NO_EMBODY_QUESTIONS, String.valueOf(tag))) {
            //从redis中取值
            try {
                String AskQuestionValue = redisWrapperClient.hget(NO_EMBODY_QUESTIONS, String.valueOf(tag));
                String[] questionArrValue = AskQuestionValue.split(ASK_COUNT_SEPARATOR);
                for (int i = 0; i < questionArrValue.length; i++) {
                    SiteMapDataDto askSiteMapDataDto = new SiteMapDataDto();
                    askSiteMapDataDto.setName(questionArrValue[i].substring(questionArrValue[i].indexOf(ASK_CONTENT_SEPARATOR) + 1, questionArrValue[i].length()));
                    askSiteMapDataDto.setLinkUrl(questionArrValue[i].substring(0, questionArrValue[i].indexOf(ASK_CONTENT_SEPARATOR)));
                    askSiteMapDataDto.setSeq(DETAIL_ORDER);
                    askSiteMapDataDtoList.add(askSiteMapDataDto);
                }
            } catch (Exception e) {
                logger.error("read ask sitemap from redis error " + e);
            }
        } else {
            //从数据库中取值,并放入redis
            List<QuestionModel> questionModelList = questionMapper.findApprovedNotEmbodyQuestions(tag);
            StringBuilder stringBuilder = new StringBuilder();
            for (QuestionModel questionModel : questionModelList) {
                SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                siteMapDataDto.setName(questionModel.getQuestion());
                siteMapDataDto.setLinkUrl(askServer + PREFIX + questionModel.getId());
                siteMapDataDto.setSeq(DETAIL_ORDER);
                stringBuilder.append(askServer).append(PREFIX).append(questionModel.getId()).append(ASK_CONTENT_SEPARATOR).append(questionModel.getQuestion()).append(ASK_COUNT_SEPARATOR);
                askSiteMapDataDtoList.add(siteMapDataDto);
            }
            if (!Strings.isNullOrEmpty(stringBuilder.toString())) {
                String value = Strings.isNullOrEmpty(stringBuilder.toString()) ? "" : stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
                redisWrapperClient.hset(NO_EMBODY_QUESTIONS, String.valueOf(tag), value, timeout);
            }
        }
        return askSiteMapDataDtoList;
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
