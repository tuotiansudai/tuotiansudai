package com.tuotiansudai.service.impl;


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
    public boolean prepareRegister(PrepareRegisterRequestDto requestDto) {
        try {
            PrepareModel prepareModel = new PrepareModel();
            prepareModel.setReferrerMobile(requestDto.getReferrerMobile());
            prepareModel.setMobile(requestDto.getMobile());
            requestDto.setChannel(requestDto.getChannel());
            prepareMapper.create(prepareModel);
            return true;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
