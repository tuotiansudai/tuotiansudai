package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinContractRequestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinContractRequestMapper {

    void create(AnxinContractRequestModel anxinContractRequestModel);

    void updateContractNoByInvestId(@Param(value = "contractNo") String contractNo,
                                    @Param(value = "investId") long investId);
}
