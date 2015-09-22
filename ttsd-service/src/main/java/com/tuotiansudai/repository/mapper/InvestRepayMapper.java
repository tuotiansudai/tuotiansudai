package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestRepayModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestRepayMapper {

    void create(List<InvestRepayModel> investRepayModels);

    List<InvestRepayModel> findByInvestId(long investId);

}
