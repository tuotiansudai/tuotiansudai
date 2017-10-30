package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.PayrollDetailMapper;
import com.tuotiansudai.repository.mapper.PayrollMapper;
import com.tuotiansudai.repository.model.PayrollDetailModel;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.PayrollStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PayrollService {

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private PayrollDetailMapper payrollDetailMapper;

    public BasePaginationDataDto<PayrollModel> list(Date createStartTime, Date createEndTime,
                                                    Date sendStartTime, Date sendEndTime,
                                                    String amountMin, String amountMax,
                                                    PayrollStatusType payrollStatusType, String title,
                                                    int index, int pageSize) {
        List<PayrollModel> list = payrollMapper.findPayrollPagination(createStartTime, createEndTime, sendStartTime, sendEndTime,
                Integer.parseInt(amountMin) * 100, Integer.parseInt(amountMax) * 100, payrollStatusType, title, index-1, pageSize);
        long count = payrollMapper.count(createStartTime, createEndTime, sendStartTime, sendEndTime,
                Integer.parseInt(amountMin) * 100, Integer.parseInt(amountMax) * 100, payrollStatusType, title);
        return new BasePaginationDataDto<>(index, pageSize, count, list);
    }

    public void updateRemark(long id, String remark, String loginName){
        payrollMapper.updateRemark(id, remark, loginName, new Date());
    }

    public BasePaginationDataDto<PayrollDetailModel> detail(long payrollId, int index, int pageSize){
        List<PayrollDetailModel> list = payrollDetailMapper.findByPayrollId(payrollId, index-1, pageSize);
        return new BasePaginationDataDto<>(index, pageSize, payrollDetailMapper.countByPayrollId(payrollId), list);
    }
}
