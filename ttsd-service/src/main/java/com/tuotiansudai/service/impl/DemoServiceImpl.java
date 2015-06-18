package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.DemoMapper;
import com.tuotiansudai.repository.model.DemoModel;
import com.tuotiansudai.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public DemoModel getDemoById(String id) {
        return demoMapper.getDemoById(id);
    }
}
