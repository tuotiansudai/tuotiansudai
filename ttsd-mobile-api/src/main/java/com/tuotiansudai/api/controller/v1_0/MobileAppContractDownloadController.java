package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.dto.AnxinLookContractDto;
import com.tuotiansudai.repository.model.AnxinContractType;
import com.tuotiansudai.repository.model.InvestModel;
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

@Controller
@RequestMapping(path = "/v1.0/contract")
public class MobileAppContractDownloadController extends MobileAppBaseController{

    private static Logger logger = Logger.getLogger(MobileAppContractDownloadController.class);

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/investor/loanId/{loanId}/investId/{investId}", method = RequestMethod.GET)
    public void generateInvestorContract(@PathVariable long loanId, @PathVariable long investId,
                                         HttpServletResponse response) throws ServletException, IOException {
        InvestModel investModel = investService.findById(investId);

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
