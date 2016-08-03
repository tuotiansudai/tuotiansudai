package com.tuotiansudai.service.impl;


import com.tuotiansudai.dto.ActivityRegisterRequestDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.PrepareMapper;
import com.tuotiansudai.repository.model.PrepareModel;
import com.tuotiansudai.service.PrepareService;
import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrepareServiceImpl implements PrepareService {

    private static Logger logger = Logger.getLogger(PrepareServiceImpl.class);

    @Autowired
    private PrepareMapper prepareMapper;

    @Autowired
    private UserService userService;

    @Override
    public BaseDataDto prepareRegister(PrepareRegisterRequestDto requestDto) {
        boolean referrerMobileIsExist = userService.mobileIsExist(requestDto.getReferrerMobile());
        boolean mobileIsExist = userService.mobileIsExist(requestDto.getMobile());

        if (!referrerMobileIsExist || mobileIsExist) {
            return new BaseDataDto(false, null);
        }

        try {
            PrepareModel prepareModel = new PrepareModel();
            prepareModel.setReferrerMobile(requestDto.getReferrerMobile());
            prepareModel.setMobile(requestDto.getMobile());
            requestDto.setChannel(requestDto.getChannel());
            prepareMapper.create(prepareModel);
            return new BaseDataDto(true, null);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return new BaseDataDto(false, e.getMessage());
        }
    }

    @Override
    public BaseDataDto register(ActivityRegisterRequestDto requestDto) {
        RegisterUserDto userDto = requestDto.convertToRegisterUserDto();
        try {
            boolean isRegisterSuccess = userService.registerUser(userDto);
            return new BaseDataDto(isRegisterSuccess, null);
        } catch (ReferrerRelationException e) {
            logger.error(e.getLocalizedMessage());
            return new BaseDataDto(false, e.getLocalizedMessage());
        }
    }

}
