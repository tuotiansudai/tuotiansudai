package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.LoanDetailDataDto;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.LoanDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanDetailServiceImpl implements LoanDetailService {
    @Autowired
    private LoanMapper loanMapper;

    @Override
    public LoanDetailDataDto getLoanDetail(String loanId) {
        LoanDetailDataDto dto = new LoanDetailDataDto();
        LoanModel loanModel = loanMapper.findById(Long.parseLong(loanId));
        if(loanModel == null){
            dto.setStatus(false);
            return dto;
        }



        return null;
    }
}
