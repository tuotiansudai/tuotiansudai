package com.tuotiansudai.console.controller;

import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.console.service.ConsoleContractService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/project-manage/down-contracts")
public class ContractController {

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private ConsoleContractService consoleContractService;

    @RequestMapping(method = RequestMethod.GET)
    public void batchDownContracts(@RequestParam(value = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                   @RequestParam(value = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                   HttpServletResponse response) {

        String contractNos = consoleContractService.getContractNos(startTime, endTime);
        byte[] zip = anxinWrapperClient.batchAnxinContracts(contractNos);
        try {
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=contracts.zip");
            response.addHeader("Content-Length", "" + zip.length);
            OutputStream ous = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            ous.write(zip);
            ous.flush();
            ous.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
