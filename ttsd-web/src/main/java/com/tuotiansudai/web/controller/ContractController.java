package com.tuotiansudai.web.controller;

import com.tuotiansudai.repository.model.ContractType;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(path = "/contract")
public class ContractController {

    static Logger logger = Logger.getLogger(ContractController.class);

    @Autowired
    private ContractService contractService;

    @RequestMapping(value = "/investor/loanId/{loanId}", method = RequestMethod.GET)
    public void generateInvestorContract(@PathVariable long loanId, HttpServletResponse response) {
        String loginName = LoginUserInfo.getLoginName();
        String pdfString = contractService.generateInvestorContract(loginName,loanId, ContractType.INVEST);
        response.setContentType("application/pdf;charset=UTF-8");
        try {
            contractService.generateContractPdf(pdfString,response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/loaner/loanId/{loanId}", method = RequestMethod.GET)
    public void generateLoanerContract(@PathVariable long loanId, HttpServletResponse response) {
        String loginName = LoginUserInfo.getLoginName();
        String pdfString = contractService.generateInvestorContract(loginName,loanId, ContractType.LOAN);
        response.setContentType("application/pdf;charset=UTF-8");
        try {
            contractService.generateContractPdf(pdfString,response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
