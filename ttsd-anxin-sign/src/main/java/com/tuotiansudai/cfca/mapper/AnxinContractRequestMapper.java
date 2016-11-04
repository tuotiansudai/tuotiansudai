package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinContractRequestModel;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinContractRequestMapper {

    void create(AnxinContractRequestModel anxinContractRequestModel);
}
