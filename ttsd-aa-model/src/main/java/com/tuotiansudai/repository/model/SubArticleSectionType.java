package com.tuotiansudai.repository.model;

/**
 * Created by qduljs2011 on 2018/9/20.
 */
public enum SubArticleSectionType {
    LAW_RULE("法律法规",ArticleSectionType.KNOWLEDGE),
    INVESTOR_EDUCATION("出借人教育",ArticleSectionType.KNOWLEDGE),
    BASIC_KNOWLEDGE("基础知识",ArticleSectionType.KNOWLEDGE)
    ;

    private String articleSectionTypeName;

    private ArticleSectionType parent;

    SubArticleSectionType(String articleSectionTypeName,ArticleSectionType parent) {
        this.articleSectionTypeName = articleSectionTypeName;
    }

    public String getArticleSectionTypeName() {
        return articleSectionTypeName;
    }

    public void setArticleSectionTypeName(String articleSectionTypeName) {
        this.articleSectionTypeName = articleSectionTypeName;
    }

    public ArticleSectionType getParent() {
        return parent;
    }

    public void setParent(ArticleSectionType parent) {
        this.parent = parent;
    }
}
