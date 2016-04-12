package com.ttsd.api.dto;

import com.esoft.archer.node.model.Node;
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

    public NodeDetailResponseDataDto(){

    }

    public NodeDetailResponseDataDto(Node node, boolean includeContent){
        this.nodeId = node.getId();
        this.title = node.getTitle();
        this.desc = node.getDescription();
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(node.getUpdateTime()).toString();
        if(includeContent){
            StringBuffer sb = new StringBuffer();
            sb.append("<h1>");
            sb.append(node.getTitle());
            sb.append("</h1>");
            sb.append("<span style='font-size:14px;color:#666;'>时间：");
            sb.append(this.time);
            sb.append("</span>");
            sb.append("<hr/>");
            sb.append(node.getNodeBody().getBody());
            this.content = sb.toString();
        }
    }

    public void addDomainNameToImageUrl(String urlPattern, String domain){
        String text = this.content;
        if(StringUtils.isEmpty(text)){
            return;
        }
        Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        text = matcher.replaceAll(domain+"$0");
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
