package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RiskEstimateRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "测评答案", example = "[1,2,3]")
    private List<Integer> answers;

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }
}
