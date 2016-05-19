package com.tuotiansudai.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.ArticleStatus;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.dto.PayDataDto;
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
    private final static String articleCounterKey = "console:article:counter";

    private final static String likeCounterKey = "likeCounter";
    private final static String readCounterKey = "readCounter";

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
        LiCaiQuanArticleDto liCaiQuanArticleDto;
        if (redisWrapperClient.hexists(articleRedisKey, String.valueOf(articleId))) {
            liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
        } else {
            liCaiQuanArticleDto = new LiCaiQuanArticleDto();
            liCaiQuanArticleDto.setId(-1);
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
        String newCommmentsString = Joiner.on('\36').withKeyValueSeparator("\37").join(existedComments);
        redisWrapperClient.hset(articleCommentRedisKey, String.valueOf(articleId), newCommmentsString);
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
}
