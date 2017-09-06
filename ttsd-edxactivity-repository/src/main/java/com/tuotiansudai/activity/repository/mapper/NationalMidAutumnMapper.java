package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.NationalMidAutumnModel;
import com.tuotiansudai.activity.repository.model.NationalMidAutumnView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NationalMidAutumnMapper {

    void create(NationalMidAutumnModel nationalMidAutumnModel);

    void update(NationalMidAutumnModel nationalMidAutumnModel);

    NationalMidAutumnModel findNationalMidAutumnModel(@Param(value = "loginName") String loginName,
                                                      @Param(value = "loanId") long loanId,
                                                      @Param(value = "loanType") String loanType);

    List<NationalMidAutumnView> findAll();
}
