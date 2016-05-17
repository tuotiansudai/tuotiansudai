package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ArticleReviewComment;
import com.tuotiansudai.repository.model.LicaiquanArticleContentModel;
import com.tuotiansudai.repository.model.LicaiquanArticleListItemModel;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huoxuanbo on 16/5/13.
 */

@Repository
public interface LicaiquanArticleMapper {
    //文章相关部分
    void createArticle(LicaiquanArticleModel licaiquanArticleModel);

    void deleteArticle(@Param("id") long id);

    void updateArticle(LicaiquanArticleModel licaiquanArticleModel);

    void updateLikeCount(@Param("id") long id, @Param("likeCount") int likeCount);

    void updateReadCount(@Param("id") long id, @Param("readCount") int readCount);

    LicaiquanArticleContentModel findArticleContentById(@Param("id") long id);

    LicaiquanArticleModel findArticleById(@Param("id") long id);

    List<LicaiquanArticleListItemModel> findExistedArticleListOrderByUpdateTime(@Param("startId") long startId, @Param("size") int size);

    List<LicaiquanArticleListItemModel> findDeletedArticleListOrderByUpdateTime(@Param("startId") long startId, @Param("size") int size);

    List<LicaiquanArticleListItemModel> findArticleListByTitleAndSection(@Param(value = "title") String title,
                                                                         @Param(value = "section") String section,
                                                                         @Param(value = "index") Integer index);
    //文章驳回意见部分
    void createComment(ArticleReviewComment articleReviewComment);

    List<ArticleReviewComment> findCommentsByArticleId(@Param("articleId") long articleId);

    void deleteCommentById(@Param("id") long id);

    void deleteCommentsByArticleId(@Param("articleId") long articleId);
}
