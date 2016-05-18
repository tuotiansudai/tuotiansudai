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
    void createArticle(LicaiquanArticleModel licaiquanArticleModel);

    void deleteArticle(@Param("id") long id);

    void updateArticle(LicaiquanArticleModel licaiquanArticleModel);

    LicaiquanArticleModel findArticleById(@Param("id") long id);

    List<LicaiquanArticleModel> findExistedArticleListOrderByCreateTime(@Param("title") String title,
                                                                        @Param("section") ArticleSectionType sectionType,
                                                                        @Param("startId") long startId,
                                                                        @Param("size") int size);

    List<LicaiquanArticleModel> findDeletedArticleListOrderByCreateTime(@Param("title") String title,
                                                                        @Param("section") ArticleSectionType sectionType,
                                                                        @Param("startId") long startId,
                                                                        @Param("size") int size);
}
