package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.AnnounceModel;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeDetailResponseDataDto extends BaseResponseDataDto {
    private String nodeId;
    private String title;
    private String desc;
    private String content;
    private String time;

    public NodeDetailResponseDataDto() {

    }
    public NodeDetailResponseDataDto(AnnounceModel input){

        this.setNodeId("" + input.getId());
        this.setContent(input.getContent());
        this.setTitle(input.getTitle());
        this.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(input.getUpdateTime()));

        

    }

    public void addDomainNameToImageUrl(String urlPattern, String domain) {
        String text = this.content;
        if (StringUtils.isEmpty(text)) {
            return;
        }
        Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        text = matcher.replaceAll(domain + "$0");
        this.content = text;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
