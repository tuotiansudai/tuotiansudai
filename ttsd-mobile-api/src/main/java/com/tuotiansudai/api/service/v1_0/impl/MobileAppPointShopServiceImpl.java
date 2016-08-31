package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.UserAddressResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.model.UserAddressModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppPointShopServiceImpl implements MobileAppPointShopService{

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public BaseResponseDto updateUserAddress(String loginName, String contract, String mobile, String address) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        userAddressMapper.create(new UserAddressModel(loginName,contract,mobile,address,loginName));
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findUserAddressResponseDto(String loginName){
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        List<UserAddressModel> byLoginName = userAddressMapper.findByLoginName(loginName);
        if(CollectionUtils.isNotEmpty(byLoginName)){
            baseResponseDto.setData(new UserAddressResponseDto(byLoginName.get(0)));
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        return baseResponseDto;
    }


}
