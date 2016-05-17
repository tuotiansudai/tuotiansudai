package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
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

    void deleteArticle(@Param("articleId") long articleId);

    void updateArticle(LicaiquanArticleModel licaiquanArticleModel);

    void updateLikeCount(@Param("articleId") long articleId, @Param("likeCount") int likeCount);

    void updateReadCount(@Param("articleId") long articleId, @Param("readCount") int readCount);

    LicaiquanArticleContentModel findArticleContentByArticleId(@Param("articleId") long articleId);

    LicaiquanArticleModel findArticleByArticleId(@Param("articleId") long articleId);

    List<LicaiquanArticleListItemModel> findExistedArticleListOrderByCreateTime(@Param("title") String title,
                                                                                @Param("section") ArticleSectionType sectionType,
                                                                                @Param("startId") long startId,
                                                                                @Param("size") int size);

    List<LicaiquanArticleListItemModel> findDeletedArticleListOrderByCreateTime(@Param("title") String title,
                                                                                @Param("section") ArticleSectionType sectionType,
                                                                                @Param("startId") long startId,
                                                                                @Param("size") int size);

    //文章驳回意见部分
    void createComment(ArticleReviewComment articleReviewComment);

    List<ArticleReviewComment> findCommentsByArticleId(@Param("articleId") long articleId);

    void deleteCommentById(@Param("id") long id);

    void deleteCommentsByArticleId(@Param("articleId") long articleId);
}
