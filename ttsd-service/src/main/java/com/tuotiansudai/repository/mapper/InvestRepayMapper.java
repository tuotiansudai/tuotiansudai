package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestRepayModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 */
@Repository
public interface InvestRepayMapper {

    public void insertInvestRepay(List<InvestRepayModel> investRepayModels);

}
