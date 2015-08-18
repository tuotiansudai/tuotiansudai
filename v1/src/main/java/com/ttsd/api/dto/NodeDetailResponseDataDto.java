package com.ttsd.api.dto;

import com.esoft.archer.node.model.Node;

import java.text.SimpleDateFormat;

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
            this.content = node.getNodeBody().getBody();
        }
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
