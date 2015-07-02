package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.MerRegisterPersonRequestModel;

public interface MerRegisterPersonMapper {

    void createMerRegisterPersonRequest(MerRegisterPersonRequestModel model);

    MerRegisterPersonRequestModel findMerRegisterPsersonRequestById(Long id);
}
