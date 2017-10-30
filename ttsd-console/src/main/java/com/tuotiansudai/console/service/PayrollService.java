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
        List<PayrollModel> payrollModels =  payrollMapper.findPayroll(createStartTime, createEndTime, sendStartTime, sendEndTime,
                Integer.parseInt(amountMin) * 100, Integer.parseInt(amountMax) * 100, payrollStatusType, title);
        int count = payrollModels.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, payrollModels.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }

    public void updateRemark(long id, String remark, String loginName){
        payrollMapper.updateRemark(id, remark, loginName, new Date());
    }

    public BasePaginationDataDto<PayrollDetailModel> detail(long payrollId, int index, int pageSize){
            List<PayrollDetailModel> payrollDetailModels = payrollDetailMapper.findByPayrollId(payrollId);
            int count = payrollDetailModels.size();
            int endIndex = pageSize * index;
            int startIndex = (index - 1) * 10;
            if (count <= endIndex) {
                endIndex = count;
            }
            if (count < startIndex) {
                startIndex = count;
            }
            BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, payrollDetailModels.subList(startIndex, endIndex));
            return basePaginationDataDto;
    }
}
