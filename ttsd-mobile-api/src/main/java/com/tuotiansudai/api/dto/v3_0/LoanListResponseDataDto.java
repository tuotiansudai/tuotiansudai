package com.tuotiansudai.api.dto.v3_0;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class LoanListResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "标的列表", example = "list")
    private List<LoanResponseDataDto> loanList = Lists.newArrayList();

    public List<LoanResponseDataDto> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<LoanResponseDataDto> loanList) {
        this.loanList = loanList;
    }
}
