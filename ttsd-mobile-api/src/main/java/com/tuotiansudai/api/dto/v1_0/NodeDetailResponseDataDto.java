package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.message.repository.model.AnnounceModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeDetailResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "文章ID", example = "10")
    private String nodeId;

    @ApiModelProperty(value = "文章标题", example = "拓天速贷")
    private String title;

    @ApiModelProperty(value = "描述", example = "拓天速贷")
    private String desc;

    @ApiModelProperty(value = "文章内容", example = "拓天速贷")
    private String content;

    @ApiModelProperty(value = "时间", example = "2016-11-25 11:27:01")
    private String time;

    public NodeDetailResponseDataDto() {

    }
    public NodeDetailResponseDataDto(AnnounceModel input, boolean includeContent){

        this.setNodeId("" + input.getId());
        this.setContent(input.getContent());
        this.setTitle(input.getTitle());
        if(StringUtils.isNotEmpty(input.getContentText())){
            if(input.getContentText().length() > 48){
                this.desc = input.getContentText().substring(0,48);
            }else{
                this.desc = input.getContentText();
            }
        }
        this.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(input.getUpdatedTime()));
        if(includeContent){
            StringBuffer sb = new StringBuffer();
            sb.append("<h1 style=\"text-align:center;\">");
            sb.append(input.getTitle());
            sb.append("</h1>");
            sb.append("<span style='font-size:14px;color:#666;'>时间：");
            sb.append(this.time);
            sb.append("</span>");
            sb.append(input.getContent());
            this.content = sb.toString();
        }

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
