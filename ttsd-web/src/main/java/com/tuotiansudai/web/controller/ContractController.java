package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.dto.AnxinLookContractDto;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.AnxinContractType;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.service.InvestService;
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
import java.text.MessageFormat;

@Controller
@RequestMapping(path = "/contract")
public class ContractController {

    static Logger logger = Logger.getLogger(ContractController.class);

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private InvestService investService;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @RequestMapping(value = "/investor/loanId/{loanId}/investId/{investId}", method = RequestMethod.GET)
    public void generateInvestorContract(@PathVariable long loanId, @PathVariable long investId,
                                         HttpServletResponse response) throws ServletException, IOException {
        String loginName = LoginUserInfo.getLoginName();
        InvestModel investModel = investService.findById(investId);
        if (Strings.isNullOrEmpty(loginName) || investModel == null || !loginName.equalsIgnoreCase(investModel.getLoginName())) {
            return;
        }

        byte[] pdf = anxinWrapperClient.printContract(new AnxinLookContractDto(investModel.getLoginName(), loanId, investId, AnxinContractType.LOAN_CONTRACT));

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
        String loginName = LoginUserInfo.getLoginName();
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        if (Strings.isNullOrEmpty(loginName) || transferApplicationModel == null || !loginName.equalsIgnoreCase(transferApplicationModel.getLoginName())) {
            return;
        }

        byte[] pdf = anxinWrapperClient.printContract(new AnxinLookContractDto(LoginUserInfo.getLoginName(), transferApplicationId, null, AnxinContractType.TRANSFER_CONTRACT));

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
    public void findContract(@PathVariable String contractNo, HttpServletResponse response) {

        logger.info(MessageFormat.format("contract download: loginName:{0}", LoginUserInfo.getLoginName()));

        String loginName = LoginUserInfo.getLoginName();

        if (Strings.isNullOrEmpty(loginName) || !investService.isUserContractNo(loginName, contractNo)) {
            return;
        }

        byte[] pdf = anxinWrapperClient.printAnxinContract(contractNo);
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
