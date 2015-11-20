package com.tuotiansudai.api.dto;

import java.util.List;

public class NodeListResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private String termId;
    private List<NodeDetailResponseDataDto> nodeList;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public List<NodeDetailResponseDataDto> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeDetailResponseDataDto> nodeList) {
        this.nodeList = nodeList;
    }
}
