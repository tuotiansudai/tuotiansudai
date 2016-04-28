package com.tuotiansudai.web.controller;

import com.tuotiansudai.repository.model.ContractType;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(path = "/contract")
public class ContractController {

    static Logger logger = Logger.getLogger(ContractController.class);

    @Autowired
    private ContractService contractService;

    @RequestMapping(value = "/investor/loanId/{loanId}", method = RequestMethod.GET)
    public void generateInvestorContract(@PathVariable long loanId, HttpServletRequest httpServletRequest , HttpServletResponse response) throws ServletException, IOException {
        String loginName = LoginUserInfo.getLoginName();
        try {
            String pdfString = contractService.generateInvestorContract(loginName, loanId, ContractType.INVEST);
            if(StringUtils.isEmpty(pdfString)){
                httpServletRequest.getRequestDispatcher("/error/404").forward(httpServletRequest, response);
                return;
            }
            response.setContentType("application/pdf;charset=UTF-8");
            contractService.generateContractPdf(pdfString, response.getOutputStream());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @RequestMapping(value = "/loaner/loanId/{loanId}", method = RequestMethod.GET)
    public void generateLoanerContract(@PathVariable long loanId,HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
        String loginName = LoginUserInfo.getLoginName();
        try {
            String pdfString = contractService.generateInvestorContract(loginName, loanId, ContractType.LOAN);
            if(StringUtils.isEmpty(pdfString)){
                httpServletRequest.getRequestDispatcher("/error/404").forward(httpServletRequest, response);
                return;
            }
            response.setContentType("application/pdf;charset=UTF-8");
            contractService.generateContractPdf(pdfString, response.getOutputStream());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @RequestMapping(value = "/transfer/transferApplicationId/{transferApplicationId}", method = RequestMethod.GET)
    public void generateTransferContract(@PathVariable long transferApplicationId,HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
        try {
            String pdfString = contractService.generateTransferContract(transferApplicationId);
            if(StringUtils.isEmpty(pdfString)){
                httpServletRequest.getRequestDispatcher("/error/404").forward(httpServletRequest, response);
                return;
            }
            response.setContentType("application/pdf;charset=UTF-8");
            contractService.generateContractPdf(pdfString, response.getOutputStream());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
