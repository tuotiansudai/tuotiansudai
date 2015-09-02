package com.tuotiansudai.dto;

import java.util.List;

public class InvestRecordDataDto extends BaseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private Integer totalPages;
    private boolean hasPreviousPage;
    private boolean hasNextPage;

    public InvestRecordDataDto(){

    }
    public InvestRecordDataDto(int index,int pageSize,int totalCount){
        this.index = index;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        if(totalCount % pageSize > 0){
            this.totalPages = totalCount/pageSize + 1;
        }else{
            this.totalPages = totalCount/pageSize;
        }
        this.isHasNextPage();
        this.isHasPreviousPage();
    }

    private List<InvestRecordDto> investRecordDtoList;

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

    public List<InvestRecordDto> getInvestRecordDtoList() {
        return investRecordDtoList;
    }

    public void setInvestRecordDtoList(List<InvestRecordDto> investRecordDtoList) {
        this.investRecordDtoList = investRecordDtoList;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasPreviousPage() {
        hasPreviousPage = false;
        if(this.index > 1 ){
            hasPreviousPage = true;
        }
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        hasNextPage = false;
        if(this.index >= 1 && this.index < this.totalPages){
            hasNextPage =  true;
        }
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }


}
