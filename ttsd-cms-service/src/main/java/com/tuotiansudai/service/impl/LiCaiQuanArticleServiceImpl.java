package com.tuotiansudai.service.impl;

import com.tuotiansudai.service.LiCaiQuanArticleService;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LiCaiQuanArticleServiceImpl implements LiCaiQuanArticleService {

    private final static Logger logger = Logger.getLogger(LiCaiQuanArticleServiceImpl.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String articleLikeCounterKey = "console:article:likeCounter";

    private final static String articleReadCounterKey = "console:article:readCounter";

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
}
