package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinSignRequestModel;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinSignRequestMapper {

    void create(AnxinSignRequestModel anxinSignRequestModel);

    void update(AnxinSignRequestModel anxinSignRequestModel);

    AnxinSignRequestModel findByUserId(String userId);

}
