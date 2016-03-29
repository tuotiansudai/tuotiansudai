package com.tuotiansudai.transfer.repository.mapper;

import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.repository.model.TransferStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransferApplicationMapper {

    void create(TransferApplicationModel model);

    void update(TransferApplicationModel model);

    List<TransferApplicationModel> findByTransferInvestId(@Param("transferInvestId")long transferInvestId, @Param("transferStatusList")List<TransferStatus> transferStatusList);

    TransferApplicationModel findById(long id);

    TransferApplicationModel findByInvestId(Long investId);
}
