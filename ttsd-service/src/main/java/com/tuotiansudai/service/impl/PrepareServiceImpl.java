package com.tuotiansudai.service.impl;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.repository.mapper.PrepareMapper;
import com.tuotiansudai.repository.model.PrepareModel;
import com.tuotiansudai.service.PrepareService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrepareServiceImpl implements PrepareService {

    private static Logger logger = Logger.getLogger(PrepareServiceImpl.class);

    @Autowired
    private PrepareMapper prepareMapper;

    @Override
    public BaseDataDto prepareRegister(PrepareRegisterRequestDto requestDto) {
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
}
