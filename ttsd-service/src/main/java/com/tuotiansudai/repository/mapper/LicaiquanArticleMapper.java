package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LicaiquanArticleContentModel;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huoxuanbo on 16/5/13.
 */

@Repository
public interface LicaiquanArticleMapper {
    void createArticle(LicaiquanArticleModel licaiquanArticleModel);

    void deleteArticle(@Param("id")long id);

    void updateArticleContent(LicaiquanArticleModel licaiquanArticleModel);

    void updateLikeCount(@Param("id")long id, @Param("likeCount") int likeCount);

    void updateReadCount(@Param("id")long id, @Param("readCount") int readCount);

    LicaiquanArticleContentModel findArticelContentById(@Param("id") long id);

    List<LicaiquanArticleModel> findExistedArticlesOrderByUpdateTime(@Param("startId") long startId, @Param("size") int size);

    List<LicaiquanArticleModel> findDeletedArticlesOrderByUpdateTime(@Param("startId") long startId, @Param("size") int size);
}
