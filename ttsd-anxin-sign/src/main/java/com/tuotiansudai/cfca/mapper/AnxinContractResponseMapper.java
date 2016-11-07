package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinContractResponseModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AnxinContractResponseMapper {

    void create(AnxinContractResponseModel anxinContractResponseModel);

    void update(AnxinContractResponseModel anxinContractResponseModel);

    void updateRetByContractNo(@Param(value = "contractNo") String contractNo,
                @Param(value = "retCode") String retCode,
                @Param(value = "retMessage") String retMessage,
                @Param(value = "updatedTime") Date updatedTime);
}
