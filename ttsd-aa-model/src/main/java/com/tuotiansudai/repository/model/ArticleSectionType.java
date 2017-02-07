package com.tuotiansudai.repository.model;

public enum  ArticleSectionType {
    PLATFORM_ACTIVITY("平台活动"),
    PLATFORM_NEWS("平台新闻"),
    INDUSTRY_NEWS("行业资讯");

    private String articleSectionTypeName;

    ArticleSectionType(String articleSectionTypeName) {
        this.articleSectionTypeName = articleSectionTypeName;
    }

    public String getArticleSectionTypeName() {
        return articleSectionTypeName;
    }

    public void setArticleSectionTypeName(String articleSectionTypeName) {
        this.articleSectionTypeName = articleSectionTypeName;
    }
}
