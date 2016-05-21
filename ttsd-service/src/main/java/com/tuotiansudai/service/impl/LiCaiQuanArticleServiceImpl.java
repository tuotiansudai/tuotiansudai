package com.tuotiansudai.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.SerializeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
public class LiCaiQuanArticleServiceImpl implements LiCaiQuanArticleService {
    static Logger logger = Logger.getLogger(LiCaiQuanArticleServiceImpl.class);

    private final static String articleRedisKey = "console:article:key";
    private final static String articleCommentRedisKey = "console:article:comment";
    private final static String articleCounterKey = "console:article:counter";

    private final static String likeCounterKey = "likeCounter";
    private final static String readCounterKey = "readCounter";

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseDto<PayDataDto> retrace(long articleId) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setData(payDataDto);
        if (!redisWrapperClient.hexistsSeri(articleRedisKey, String.valueOf(articleId))) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(MessageFormat.format("id:{0}不存在,请核实!", articleId));
            return baseDto;
        }
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
        if (liCaiQuanArticleDto != null && liCaiQuanArticleDto.getArticleStatus() == ArticleStatus.APPROVING) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(MessageFormat.format("id:{0}正在审核!", articleId));
            return baseDto;
        }
        liCaiQuanArticleDto.setArticleStatus(ArticleStatus.RETRACED);
        liCaiQuanArticleDto.setCreateTime(new Date());
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(articleId), liCaiQuanArticleDto);
        return baseDto;
    }

    @Override
    public void createAndEditArticle(LiCaiQuanArticleDto liCaiQuanArticleDto,String creator) {
        if(liCaiQuanArticleDto.getArticleId() == null ){
            long articleId = idGenerator.generate();
            liCaiQuanArticleDto.setArticleId(articleId);
            liCaiQuanArticleDto.setCreateTime(new Date());
        }else{
            liCaiQuanArticleDto.setUpdateTime(new Date());
        }
        liCaiQuanArticleDto.setCreator(creator);
        liCaiQuanArticleDto.setArticleStatus(ArticleStatus.TO_APPROVE);
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()), liCaiQuanArticleDto);
    }

    @Override
    public LiCaiQuanArticleDto obtainEditArticleDto(long articleId) {
        if(redisWrapperClient.hexistsSeri(articleRedisKey,String.valueOf(articleId))){
            return (LiCaiQuanArticleDto)redisWrapperClient.hgetSeri(articleRedisKey,String.valueOf(articleId));
        }else{
            LicaiquanArticleModel licaiquanArticleModel = licaiquanArticleMapper.findArticleById(articleId);
            if(licaiquanArticleModel != null){
                return new LiCaiQuanArticleDto(licaiquanArticleModel);
            }
        }

        return null;

    }

    @Override
    public ArticlePaginationDataDto findLiCaiQuanArticleDto(String title, ArticleSectionType articleSectionType,
                                                            int pageSize,
                                                            int index){
        int count = 0;
        List<LiCaiQuanArticleDto> list;
        List<LiCaiQuanArticleDto> articleDtoList = findRedisArticleDto(title,articleSectionType);
        if(CollectionUtils.isNotEmpty(articleDtoList)) count += articleDtoList.size();

        List<LicaiquanArticleModel> articleListItemModelList = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime(title,articleSectionType,1,10000);
        if(CollectionUtils.isNotEmpty(articleListItemModelList)){
            count += articleListItemModelList.size();
            for(LicaiquanArticleModel model : articleListItemModelList){
                articleDtoList.add(new LiCaiQuanArticleDto(model));
            }
        }
        Collections.sort(articleDtoList, new Comparator<LiCaiQuanArticleDto>(){
            @Override
            public int compare(LiCaiQuanArticleDto o1, LiCaiQuanArticleDto o2) {
                return o2.getCreateTime().after(o1.getCreateTime()) ? 1 : -1;
            }
        });

        int indexCount = (index - 1) * pageSize;
        int totalPages = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
        index = index > totalPages ? totalPages : index;
        int toIndex = (indexCount + pageSize) > articleDtoList.size()?articleDtoList.size():indexCount + pageSize;
        list = updateArticleDtoTitle(articleDtoList.subList(indexCount,toIndex));
        ArticlePaginationDataDto dto = new ArticlePaginationDataDto(index,pageSize,count,list);
        return dto;
    }

    private List<LiCaiQuanArticleDto> findRedisArticleDto(String title, ArticleSectionType articleSectionType){
        List<LiCaiQuanArticleDto> articleDtoList = new ArrayList<>();
        Map<byte[],byte[]> articleDtoListHkey = redisWrapperClient.hgetAllSeri(articleRedisKey);
        for (byte[] key : articleDtoListHkey.keySet()) {
            LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto)SerializeUtil.deserialize(articleDtoListHkey.get(key));
            if(StringUtils.isNotEmpty(title) && liCaiQuanArticleDto.getTitle().indexOf(title) == -1){
                continue;
            }
            if(articleSectionType != null && !liCaiQuanArticleDto.getSection().equals(articleSectionType)){
                continue;
            }
            articleDtoList.add(liCaiQuanArticleDto);
        }
        return articleDtoList;
    }

    private List<LiCaiQuanArticleDto> updateArticleDtoTitle(List<LiCaiQuanArticleDto> list){
        List<LiCaiQuanArticleDto> redisList = new ArrayList<>();
        List<LiCaiQuanArticleDto> databaseList = new ArrayList<>();
        for(LiCaiQuanArticleDto dto : list){
            if(dto.getArticleStatus().equals(ArticleStatus.PUBLISH)){
                databaseList.add(dto);
            }else{
                redisList.add(dto);
            }
        }

        if(CollectionUtils.isNotEmpty(redisList) && CollectionUtils.isNotEmpty(databaseList)){
            for(LiCaiQuanArticleDto database : databaseList){
                for(LiCaiQuanArticleDto redis : redisList){
                    if(redis.getTitle().equals(database.getTitle())){
                        database.setTitle(database.getTitle() + "(原文)");
                        break;
                    }
                }
            }
        }
        return list;
    }

    public LiCaiQuanArticleDto getArticleContent(long articleId) {
        LiCaiQuanArticleDto liCaiQuanArticleDto;
        if (redisWrapperClient.hexists(articleRedisKey, String.valueOf(articleId))) {
            liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
        } else {
            liCaiQuanArticleDto = new LiCaiQuanArticleDto();
            liCaiQuanArticleDto.setArticleId(-1l);
            liCaiQuanArticleDto.setTitle("");
            liCaiQuanArticleDto.setCreateTime(new Date());
            liCaiQuanArticleDto.setContent("");
            liCaiQuanArticleDto.setAuthor("");
        }
        return liCaiQuanArticleDto;
    }

    @Override
    public void rejectArticle(long articleId, String comment) {
        String timeStamp = new Date().toString();
        Map<String, String> existedComments = getAllComments(articleId);
        existedComments.put(timeStamp, comment);
        String newCommentsString = Joiner.on('\36').withKeyValueSeparator("\37").join(existedComments);
        redisWrapperClient.hset(articleCommentRedisKey, String.valueOf(articleId), newCommentsString);
    }

    @Override
    public Map<String, String> getAllComments(long articleId) {
        String existedCommentsString = redisWrapperClient.hget(articleCommentRedisKey, String.valueOf(articleId));
        Map<String, String> existedComments = new HashMap<>();
        if (null != existedCommentsString) {
            Map<String, String> unmodifiedMap = Splitter.on('\36').withKeyValueSeparator('\37').split(existedCommentsString);
            for (Map.Entry<String, String> entry : unmodifiedMap.entrySet()) {
                existedComments.put(entry.getKey(), entry.getValue());
            }
        }
        return existedComments;
    }

    @Override
    public Map<String, Integer> getLikeAndReadCount(long articleId) {
        String articleCounterString = redisWrapperClient.hget(articleCounterKey, String.valueOf(articleId));
        Map<String, Integer> existedCounter = new HashMap<>();
        if (null != articleCounterString) {
            Map<String, String> unmodifiedMap = Splitter.on(',').withKeyValueSeparator(':').split(articleCounterString);
            for (Map.Entry<String, String> entry : unmodifiedMap.entrySet()) {
                existedCounter.put(entry.getKey(), Integer.parseInt(entry.getValue()));
            }
        } else {
            existedCounter.put(likeCounterKey, 0);
            existedCounter.put(readCounterKey, 0);
        }
        return existedCounter;
    }

    private void updateRedisArticleCounter(long articleId, Map<String, Integer> map) {
        String counterString = Joiner.on(",").withKeyValueSeparator(":").join(map);
        redisWrapperClient.hset(articleCounterKey, String.valueOf(articleId), counterString);
    }

    @Override
    public void updateLikeCount(long articleId) {
        Map<String, Integer> existedCounter = getLikeAndReadCount(articleId);
        existedCounter.put(likeCounterKey, existedCounter.get(likeCounterKey) + 1);
        updateRedisArticleCounter(articleId, existedCounter);
    }

    @Override
    public void updateReadCount(long articleId) {
        Map<String, Integer> existedCounter = getLikeAndReadCount(articleId);
        existedCounter.put(readCounterKey, existedCounter.get(readCounterKey) + 1);
        updateRedisArticleCounter(articleId, existedCounter);
    }

    private List<LiCaiQuanArticleDto> setLikeAndReadCount(List<LiCaiQuanArticleDto> list){
        for(LiCaiQuanArticleDto dto : list){
            Map<String, Integer> map = getLikeAndReadCount(dto.getArticleId());
            dto.setLikeCount(map.get(likeCounterKey));
            dto.setReadCount(map.get(readCounterKey));
        }
        return list;
    }

    @Override
    public void checkPassAndCreateArticle(long articleId,String checkName){
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
        if(liCaiQuanArticleDto != null){
            redisWrapperClient.hdel(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
            LiCaiQuanArticleDto liCaiQuanArticl1eDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
            liCaiQuanArticleDto.setChecker(checkName);
            if(liCaiQuanArticleDto.getUpdateTime() == null){
                liCaiQuanArticleDto.setUpdateTime(new Date());
            }
            LicaiquanArticleModel model = new LicaiquanArticleModel(liCaiQuanArticleDto);
            if(this.licaiquanArticleMapper.findArticleById(model.getId()) != null){
                this.licaiquanArticleMapper.updateArticle(model);
            }else{
                this.licaiquanArticleMapper.createArticle(model);
            }
        }
    }

    @Override
    public void deleteArticle(long articleId) {
        this.licaiquanArticleMapper.deleteArticle(articleId);
    }

    @Override
    public void changeArticleStatus(long articleId,ArticleStatus articleStatus) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey,
                String.valueOf(articleId));
        liCaiQuanArticleDto.setArticleStatus(articleStatus);
    }
}
