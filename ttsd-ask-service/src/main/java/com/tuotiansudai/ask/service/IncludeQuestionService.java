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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class IncludeQuestionService {

    private static final Logger logger = Logger.getLogger(IncludeQuestionService.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private IncludeQuestionMapper includeQuestionMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Value("${cms.server}")
    private String cmsServer;

    @Value("${ask.server}")
    private String askServer;

    private static final String NO_INCLUDE_QUESTIONS = "web:no_include_ask_questions";

    private static final String CMS_CATEGORY = "cms:no_include_questions";

    private static final String prefix = "/question/";

    private static final int timeout = 24 * 60 * 60 * 1000;

    private static final String CMS_CATEGORY_INFO = "/category-info?format=json";

    private static final String CMS_POSTS_INFO = "/posts-info?format=json&category=";

    private static final int CATEGORY_ORDER = 2;

    private static final int DETAIL_ORDER = 3;

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

    public void create(IncludeQuestionModel includeQuestionModel) {
        includeQuestionMapper.create(includeQuestionModel);
    }

    public List<SiteMapDataDto> getSiteMapData() {
        List<SiteMapDataDto> siteMapDataDtoList = this.getAskSiteMapData();
        siteMapDataDtoList.addAll(this.getCmsSiteMapData());
        return siteMapDataDtoList;
    }

    private List<SiteMapDataDto> getAskSiteMapData() {
        List<SiteMapDataDto> siteMapDataDtoList = Lists.newArrayList();
        if (redisWrapperClient.exists(NO_INCLUDE_QUESTIONS)) {
            //从redis中取值
            Map<byte[], byte[]> siteMapListHkey = redisWrapperClient.hgetAllSeri(NO_INCLUDE_QUESTIONS);
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
            List<QuestionModel> questionModelList = questionMapper.findApprovedNoInclude();
            for (QuestionModel questionModel : questionModelList) {
                SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
                siteMapDataDto.setName(questionModel.getQuestion());
                siteMapDataDto.setLinkUrl(askServer + prefix + questionModel.getId());
                siteMapDataDto.setSeq(DETAIL_ORDER);
                siteMapDataDtoList.add(siteMapDataDto);
                redisWrapperClient.hset(NO_INCLUDE_QUESTIONS, askServer + prefix + questionModel.getId(), questionModel.getQuestion(), timeout);
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
