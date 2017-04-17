package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.HTrackingRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppHTrackingService;
import com.tuotiansudai.repository.mapper.HTrackingUserMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.HTrackingStatus;
import com.tuotiansudai.repository.model.HTrackingUserModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppHTrackingServiceImpl implements MobileAppHTrackingService {

    @Autowired
    private HTrackingUserMapper hTrackingUserMapper;

    @Autowired
    private UserMapper userMapper;

    private final static String HTRACKING_CHANNEL = "htracking";

    @Override
    public BaseResponseDto save(HTrackingRequestDto hTrackingRequestDto) {
        if (hTrackingRequestDto == null && CollectionUtils.isEmpty(hTrackingRequestDto.getRegs())) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG);
        }

        hTrackingRequestDto.getRegs().stream()
                .filter(dto -> hTrackingUserMapper.findByMobileAndDeviceId(dto.getUid(), dto.getIdfa()) == null)
                .forEach(dto -> {
                    UserModel userModel = userMapper.findByLoginNameOrMobile(dto.getUid());
                    HTrackingStatus status = HTrackingStatus.UN_REGISTER;
                    if (userModel != null) {
                        status = HTrackingStatus.REGISTERED;
                        userModel.setChannel(HTRACKING_CHANNEL);
                        userMapper.updateUser(userModel);
                    }

                    hTrackingUserMapper.create(new HTrackingUserModel(dto.getUid(), dto.getIdfa(), status));
                });

        return new BaseResponseDto(ReturnMessage.SUCCESS);
    }


}
