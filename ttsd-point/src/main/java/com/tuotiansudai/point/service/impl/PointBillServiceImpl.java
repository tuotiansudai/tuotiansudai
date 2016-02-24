package com.tuotiansudai.point.service.impl;

import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointBillServiceImpl implements PointBillService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Override
    @Transactional
    public void createPointBill(String loginName, PointBusinessType businessType, long point) {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        accountModel.setPoint(accountModel.getPoint() + point);
        pointBillMapper.create(new PointBillModel(loginName, point, businessType, ""));
    }
}
