package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinContractRequestModel;
import com.tuotiansudai.cfca.model.AnxinContractResponseModel;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinContractResponseMapper {

    void create(AnxinContractResponseModel anxinContractResponseModel);
}
