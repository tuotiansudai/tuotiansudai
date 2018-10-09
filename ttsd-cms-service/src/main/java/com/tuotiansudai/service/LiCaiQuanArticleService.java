package com.tuotiansudai.service;

public interface LiCaiQuanArticleService {

    long getLikeCount(long articleId);

    long getReadCount(long articleId);

    void updateLikeCount(long articleId);

    void updateReadCount(long articleId);
}
