package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LiCaiQuanArticleServiceImpl implements LiCaiQuanArticleService {
    static Logger logger = Logger.getLogger(LiCaiQuanArticleServiceImpl.class);

    private final static String articleRedisKey = "console:article:key";
    private final static String articleCommentRedisKey = "console:article:comment";
    private final static String articleLikeCounterKey = "console:article:likeCounter";
    private final static String articleReadCounterKey = "console:article:readCounter";

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private IdGenerator idGenerator;

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
    public void createArticle(LiCaiQuanArticleDto liCaiQuanArticleDto) {
        long articleId = idGenerator.generate();
        liCaiQuanArticleDto.setId(articleId);
        liCaiQuanArticleDto.setArticleStatus(ArticleStatus.TO_APPROVE);
        liCaiQuanArticleDto.setCreateTime(new Date());
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(articleId), liCaiQuanArticleDto);
    }

    @Override
    public LiCaiQuanArticleDto getArticleContent(long articleId) {
        if (redisWrapperClient.hexists(articleRedisKey, String.valueOf(articleId))) {
            return (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
        } else {
            return null;
        }
    }

    private void storeComment(long articleId, String comment) {
        String timeStamp = new Date().toString();
        Map<String, String> existedComments = getAllComments(articleId);
        existedComments.put(timeStamp, comment);
        redisWrapperClient.hsetSeri(articleCommentRedisKey, String.valueOf(articleId), existedComments);
    }

    @Override
    public void rejectArticle(long articleId, String comment) {
        changeArticleStatus(articleId, ArticleStatus.TO_APPROVE);
        storeComment(articleId, comment);
    }

    @Override
    public Map<String, String> getAllComments(long articleId) {
        Map<String, String> existedComments = new HashMap<>();
        if (redisWrapperClient.hexists(articleCommentRedisKey, String.valueOf(articleId))) {
            existedComments = (Map<String, String>) redisWrapperClient.hgetSeri(articleCommentRedisKey, String.valueOf(articleId));
        }
        return existedComments;
    }

    @Override
    public BaseDto<BaseDataDto> checkArticleOnStatus(long articleId) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        if (getArticleContent(articleId).getArticleStatus().equals(ArticleStatus.APPROVING)) {
            BaseDataDto baseDataDto = new BaseDataDto();
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("文章正在审核中!");
            baseDto.setData(baseDataDto);
        } else {
            changeArticleStatus(articleId, ArticleStatus.APPROVING);
            BaseDataDto baseDataDto = new BaseDataDto();
            baseDataDto.setStatus(true);
            baseDto.setData(baseDataDto);
        }
        return baseDto;
    }


    @Override
    public long getLikeCount(long articleId) {
        String likeCountString = redisWrapperClient.hget(articleLikeCounterKey, String.valueOf(articleId));
        if (null == likeCountString) {
            return 0L;
        }
        return Long.valueOf(likeCountString);
    }

    @Override
    public long getReadCount(long articleId) {
        String readCountString = redisWrapperClient.hget(articleReadCounterKey, String.valueOf(articleId));
        if (null == readCountString) {
            return 0L;
        }
        return Long.valueOf(readCountString);
    }

    @Override
    public void updateLikeCount(long articleId) {
        redisWrapperClient.hincrby(articleLikeCounterKey, String.valueOf(articleId), 1);
    }

    @Override
    public void updateReadCount(long articleId) {
        redisWrapperClient.hincrby(articleReadCounterKey, String.valueOf(articleId), 1);
    }

    private void changeArticleStatus(long articleId, ArticleStatus articleStatus) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey,
                String.valueOf(articleId));
        liCaiQuanArticleDto.setArticleStatus(articleStatus);
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(articleId), liCaiQuanArticleDto);
    }
}
