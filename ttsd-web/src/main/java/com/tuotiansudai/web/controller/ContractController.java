package com.tuotiansudai.web.controller;

import com.tuotiansudai.repository.model.ContractType;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.utils.FreeMarkerHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(path = "/contract")
public class ContractController {
    static Logger logger = Logger.getLogger(ContractController.class);
    @Autowired
    private ContractService contractService;

    @RequestMapping(value = "/investor/{loanId}", method = RequestMethod.GET)
    public void generateInvestorContract(@PathVariable long loanId, HttpServletResponse response) {
        String pdfString = contractService.generateInvestorContract(loanId, ContractType.INVEST);
        contractService.generateContractPdf(pdfString,response);
    }

    @RequestMapping(value = "/loaner/{loanId}", method = RequestMethod.GET)
    public void generateLoanerContract(@PathVariable long loanId, HttpServletResponse response) {
        String pdfString = contractService.generateInvestorContract(loanId,ContractType.LOAN);
        contractService.generateContractPdf(pdfString,response);
    }

}
