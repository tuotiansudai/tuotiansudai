package com.tuotiansudai.console.service;

import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleContractService {

    @Autowired
    private InvestMapper investMapper;

    public String getContractNos(Date startTime, Date endTime){
        List<InvestModel> list = investMapper.countSuccessInvestByInvestTime(null, startTime, endTime);
        List<String> contractNos = list.stream()
                .filter(investModel -> investModel.getContractNo() !=null)
                .map(InvestModel::getContractNo).collect(Collectors.toList());
        return String.join("@", contractNos);
    }
}
