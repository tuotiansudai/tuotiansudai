package com.tuotiansudai.api.dto;

import java.util.List;

public class EvidenceResponseDataDto {
    private String title;
    private List<String> imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}
