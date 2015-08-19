package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleModel;

import java.util.List;

/**
 * Created by tuotian on 15/8/18.
 */
public interface LoanTitleMapper {
    void createLoanTitle(List<LoanTitleModel> loanTitleModels);
}
