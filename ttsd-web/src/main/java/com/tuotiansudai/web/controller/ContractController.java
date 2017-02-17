package com.tuotiansudai.web.controller;

import com.tuotiansudai.cfca.service.AnxinSignService;
import com.tuotiansudai.cfca.service.ContractService;
import com.tuotiansudai.cfca.service.impl.ContractServiceImpl;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
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
        byte[] pdf = contractService.printContractPdf(ContractServiceImpl.LOAN_CONTRACT, LoginUserInfo.getLoginName(), loanId, investId);

        try (OutputStream ous = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            response.addHeader("Content-Disposition", String.format("attachment;filename=%s.pdf", String.valueOf(investId)));
            response.addHeader("Content-Length", "" + pdf.length);
            response.setContentType("application/octet-stream");
            ous.write(pdf);
            ous.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/transfer/transferApplicationId/{transferApplicationId}", method = RequestMethod.GET)
    public void generateTransferContract(@PathVariable long transferApplicationId, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
        byte[] pdf = contractService.printContractPdf(ContractServiceImpl.TRANSFER_CONTRACT, LoginUserInfo.getLoginName(), transferApplicationId, null);

        try (OutputStream ous = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            response.addHeader("Content-Disposition", String.format("attachment;filename=%s.pdf", String.valueOf(transferApplicationId)));
            response.addHeader("Content-Length", "" + pdf.length);
            response.setContentType("application/octet-stream");
            ous.write(pdf);
            ous.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/invest/contractNo/{contractNo}", method = RequestMethod.GET)
    public void findContract(@PathVariable String contractNo, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        byte[] pdf = anxinSignService.downContractByContractNo(contractNo);
        try {
            response.reset();
            response.addHeader("Content-Disposition", String.format("attachment;filename=%s.pdf", contractNo));
            response.addHeader("Content-Length", "" + pdf.length);
            OutputStream ous = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            ous.write(pdf);
            ous.flush();
            ous.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
