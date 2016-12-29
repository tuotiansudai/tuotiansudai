package com.tuotiansudai.ask.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.CmsCategoryDto;
import com.tuotiansudai.ask.repository.mapper.QuestionMapper;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.SiteMapDataDto;
import com.tuotiansudai.util.JsonConverter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
public class EmbodyQuestionService {

    private static final Logger logger = Logger.getLogger(EmbodyQuestionService.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @Value("${cms.server}")
    private String cmsServer;

    @Value("${ask.server}")
    private String askServer;

    private static final String NO_EMBODY_QUESTIONS = "web:no_embody_ask_questions";

    private static final String CMS_CATEGORY = "cms:no_embody_questions";

    private static final String PREFIX = "/question/";

    private static final int timeout = 24 * 60 * 60;

    private static final String CMS_CATEGORY_INFO = "/category-info?format=json";

    private static final String CMS_POSTS_INFO = "/posts-info?format=json&category=";

    private static final int CATEGORY_ORDER = 2;

    private static final int DETAIL_ORDER = 3;


    public List<SiteMapDataDto> getSiteMapData() {
        List<SiteMapDataDto> siteMapDataDtoList = this.getAskSiteMapData();
        siteMapDataDtoList.addAll(this.getCmsSiteMapData());
        return siteMapDataDtoList;
    }

    private List<SiteMapDataDto> getAskSiteMapData() {
        List<SiteMapDataDto> siteMapDataDtoList = Lists.newArrayList();
        if (redisWrapperClient.exists(NO_EMBODY_QUESTIONS)) {
            //从redis中取值
            Map<byte[], byte[]> siteMapListHkey = redisWrapperClient.hgetAllSeri(NO_EMBODY_QUESTIONS);
            for (byte[] key : siteMapListHkey.keySet()) {
                try {
                    SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                    siteMapDataDto.setName(new String(siteMapListHkey.get(key), "UTF-8"));
                    siteMapDataDto.setLinkUrl(new String(key, "UTF-8"));
                    siteMapDataDto.setSeq(DETAIL_ORDER);
                    siteMapDataDtoList.add(siteMapDataDto);
                } catch (UnsupportedEncodingException e) {
                    logger.error("read redis error " + e);
                }
            }
        } else {
            //从数据库中取值,并放入redis
            List<QuestionModel> questionModelList = questionMapper.findApprovedNotEmbodyQuestions();
            for (QuestionModel questionModel : questionModelList) {
                SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                siteMapDataDto.setName(questionModel.getQuestion());
                siteMapDataDto.setLinkUrl(askServer + PREFIX + questionModel.getId());
                siteMapDataDto.setSeq(DETAIL_ORDER);
                siteMapDataDtoList.add(siteMapDataDto);
                redisWrapperClient.hset(NO_EMBODY_QUESTIONS, askServer + PREFIX + questionModel.getId(), questionModel.getQuestion(), timeout);
            }
        }
        return siteMapDataDtoList;
    }

    public List<SiteMapDataDto> getCmsSiteMapCategory() {
        List<SiteMapDataDto> siteMapCategoryList = new LinkedList<>();
        String cmsCategoryJsonString = loadJSON(cmsServer + CMS_CATEGORY_INFO);
        if (cmsCategoryJsonString == null || "".equals(cmsCategoryJsonString)) {
            return siteMapCategoryList;
        }

        try {
            JsonConverter.readValue(cmsCategoryJsonString, CmsCategoryDto.class);

        } catch (IOException e) {
            logger.error("jsonConverter parse is error " + e);
        }
        JSONArray categoryJsonArray = JSONArray.fromObject(cmsCategoryJsonString);
        Iterator categoryIt = categoryJsonArray.iterator();
        //找出根目录
        while (categoryIt.hasNext()) {
            SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
            JSONObject obj = (JSONObject) categoryIt.next();
            if (obj.getString("parent") == "null" || "".equals(obj.getString("parent"))) {
                siteMapDataDto.setName(obj.getString("name") + "|" + obj.getString("slug"));
                siteMapDataDto.setLinkUrl(cmsServer + "/" + obj.getString("slug"));
                siteMapCategoryList.add(siteMapDataDto);
            }
        }
        return this.getCmsSiteMapAllCategory(siteMapCategoryList, cmsCategoryJsonString);

    }

    private List<SiteMapDataDto> getCmsSiteMapAllCategory(List<SiteMapDataDto> siteMapCategoryList, String cmsCategoryJsonString) {
        List<SiteMapDataDto> siteMapCategoryAllList = new LinkedList<>();
        for (SiteMapDataDto siteMapDataDto : siteMapCategoryList) {
            SiteMapDataDto siteMapDataDtoOneLevel = new SiteMapDataDto();
            siteMapDataDtoOneLevel.setName(siteMapDataDto.getName().substring(0, siteMapDataDto.getName().indexOf("|")));
            siteMapDataDtoOneLevel.setLinkUrl(siteMapDataDto.getLinkUrl());
            siteMapDataDtoOneLevel.setSeq(1);
            siteMapCategoryAllList.add(siteMapDataDtoOneLevel);

            JSONArray categoryJsonArray = JSONArray.fromObject(cmsCategoryJsonString);
            Iterator categoryIt = categoryJsonArray.iterator();
            while (categoryIt.hasNext()) {
                JSONObject obj = (JSONObject) categoryIt.next();
                if (obj.getString("parent").equals(siteMapDataDto.getName().substring(siteMapDataDto.getName().indexOf("|") + 1, siteMapDataDto.getName().length()))) {
                    SiteMapDataDto siteMapDataDtoSecondLevel = new SiteMapDataDto();
                    siteMapDataDtoSecondLevel.setName(obj.getString("name"));
                    siteMapDataDtoSecondLevel.setLinkUrl(cmsServer + "/" + obj.getString("parent") + "/" + obj.getString("slug"));
                    siteMapDataDtoSecondLevel.setSeq(2);
                    siteMapCategoryAllList.add(siteMapDataDtoSecondLevel);
                }
            }
        }
        return siteMapCategoryAllList;
    }

    private List<SiteMapDataDto> getCmsSiteMapData() {
        List<SiteMapDataDto> siteMapDataDtoList = Lists.newArrayList();
        List<SiteMapDataDto> siteMapPostsDataDtoList = Lists.newArrayList();
        if (redisWrapperClient.exists(CMS_CATEGORY)) {
            //从redis中取值
            Map<byte[], byte[]> siteMapListHkey = redisWrapperClient.hgetAllSeri(CMS_CATEGORY);
            for (byte[] key : siteMapListHkey.keySet()) {
                try {
                    SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                    String name = new String(siteMapListHkey.get(key), "UTF-8");
                    siteMapDataDto.setName(name.substring(0, name.indexOf("||")));
                    siteMapDataDto.setSeq(Integer.parseInt(name.substring(name.indexOf("||") + 2, name.length())));
                    siteMapDataDto.setLinkUrl(new String(key, "UTF-8"));
                    siteMapDataDtoList.add(siteMapDataDto);
                } catch (UnsupportedEncodingException e) {
                    logger.error("read redis error " + e);
                }
            }
        } else {
            String cmsCategoryJsonString = loadJSON(cmsServer + CMS_CATEGORY_INFO);
            if (cmsCategoryJsonString == null || "".equals(cmsCategoryJsonString)) {
                return siteMapDataDtoList;
            }
            JSONArray categoryJsonArray = JSONArray.fromObject(cmsCategoryJsonString);
            Iterator categoryIt = categoryJsonArray.iterator();
            while (categoryIt.hasNext()) {
                SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                JSONObject obj = (JSONObject) categoryIt.next();
                String cmsPostsJsonString = loadJSON(cmsServer + CMS_POSTS_INFO + obj.getString("slug"));
                if (!"".equals(cmsPostsJsonString)) {
                    JSONArray postsJsonArray = JSONArray.fromObject(cmsPostsJsonString);
                    Iterator postsIt = postsJsonArray.iterator();
                    while (postsIt.hasNext()) {
                        SiteMapDataDto siteMapPostsDataDto = new SiteMapDataDto();
                        JSONObject postsObj = (JSONObject) postsIt.next();
                        siteMapPostsDataDto.setName(postsObj.getString("title"));
                        siteMapPostsDataDto.setLinkUrl(postsObj.getString("url"));
                        siteMapPostsDataDtoList.add(siteMapPostsDataDto);
                        siteMapPostsDataDto.setSeq(DETAIL_ORDER);
                        //因为网站读取从redis的值时需要排序,所以添加时value用 (title + "||" +排序) 的格式,以便前台读取时可以排序,详细文章用3,栏目分类用2
                        redisWrapperClient.hset(CMS_CATEGORY, postsObj.getString("url"), postsObj.getString("title") + "||" + DETAIL_ORDER, timeout);
                    }
                }

                if (obj.getString("name") != null) {
                    siteMapDataDto.setName(obj.getString("name"));
                }
                String linkUrl = cmsServer + (obj.getString("parent") != null ? "/" + obj.getString("parent") : "") + "/" + obj.getString("slug");
                siteMapDataDto.setLinkUrl(linkUrl);
                siteMapDataDto.setSeq(CATEGORY_ORDER);
                siteMapDataDtoList.add(siteMapDataDto);
                redisWrapperClient.hset(CMS_CATEGORY, linkUrl, obj.getString("name") + "||" + CATEGORY_ORDER, timeout);
            }
            siteMapDataDtoList.addAll(siteMapPostsDataDtoList);
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
                if (questionModel != null && !questionModel.isEmbody()) {
                    questionService.updateEmbodyById(Long.parseLong(questionId));
                    listSuccess.add(strVal);
                }
                else{
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
        baseDataDto.setMessage("批量导入成功! 其中 " + listSuccess.size() + " 条导入成功, "+listFailed.size()+" 条导入失败");
        //清除缓存
        if(listSuccess.size() > 0){
            redisWrapperClient.del(NO_EMBODY_QUESTIONS);
        }
        return baseDataDto;
    }

    private String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            logger.error("access cms url not exist, please check cms url + " + e);
        } catch (IOException e) {
            logger.error("access cms url not exist, please check cms url + " + e);
        }
        return json.toString();
    }


}
