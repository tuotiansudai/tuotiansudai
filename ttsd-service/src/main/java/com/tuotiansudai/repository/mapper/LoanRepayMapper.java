package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRepayModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2015/9/8.
 */
@Repository
public interface LoanRepayMapper {

    public void insertLoanRepay(List<LoanRepayModel> loanRepayModels);

}
