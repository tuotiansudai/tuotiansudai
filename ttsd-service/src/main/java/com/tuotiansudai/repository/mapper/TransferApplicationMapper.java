package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.repository.model.TransferStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferApplicationMapper {

    void create(TransferApplicationModel model);

    void update(TransferApplicationModel model);

    TransferApplicationModel findByTransferInvestId(@Param("transferInvestId")long transferInvestId, @Param("status")TransferStatus status);

    TransferApplicationModel findByInvestId(Long investId);
}
