package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LicaiquanArticleCommentModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicaiquanArticleCommentMapper {
    void createComment(LicaiquanArticleCommentModel licaiquanArticleCommentModel);

    List<LicaiquanArticleCommentModel> findCommentsByArticleId(@Param("articleId") long articleId);
}
