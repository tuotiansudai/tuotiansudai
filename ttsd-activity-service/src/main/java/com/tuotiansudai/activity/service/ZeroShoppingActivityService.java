package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.ZeroShoppingPrizeConfigMapper;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZeroShoppingActivityService {

    @Autowired
    private ZeroShoppingPrizeConfigMapper zeroShoppingMapper;

    public List<ZeroShoppingPrizeConfigModel> getAllPrize(){
        return zeroShoppingMapper.findAll();
    }
}
