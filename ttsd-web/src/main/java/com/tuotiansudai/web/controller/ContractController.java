package com.tuotiansudai.web.controller;

import com.tuotiansudai.service.AnxinSignService;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(path = "/contract")
public class ContractController {

    static Logger logger = Logger.getLogger(ContractController.class);

    @Autowired
    private ContractService contractService;
    @Autowired
    private AnxinSignService anxinSignService;

    @RequestMapping(value = "/investor/loanId/{loanId}/investId/{investId}", method = RequestMethod.GET)
    public void generateInvestorContract(@PathVariable long loanId, @PathVariable long investId, HttpServletRequest httpServletRequest,
                                         HttpServletResponse response) throws ServletException, IOException {
        String loginName = LoginUserInfo.getLoginName();
        try {
            String pdfString = contractService.generateInvestorContract(loginName, loanId, investId);
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

    @RequestMapping(value = "/invest/contractNo/{contractNo}", method = RequestMethod.GET)
    public void findContract(@PathVariable String contractNo,HttpServletRequest httpServletRequest, HttpServletResponse response){
        byte[] pdf = anxinSignService.downContractByContractNo("JK20161107000000051");
        try {
            response.setContentType("application/pdf");
            OutputStream stream = response.getOutputStream();
            stream.write(pdf);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
