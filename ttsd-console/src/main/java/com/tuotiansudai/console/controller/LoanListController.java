package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.console.service.ConsoleLoanService;
import com.tuotiansudai.dto.AnxinDataDto;
import com.tuotiansudai.dto.AnxinQueryContractDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.message.AnxinContractMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.PaginationUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/project-manage")
public class LoanListController {

    static Logger logger = Logger.getLogger(LoanListController.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private ConsoleLoanService consoleLoanService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private InvestService investService;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;


    public static final String LOAN_CONTRACT_IN_CREATING_KEY = "loanContractInCreating:";

    public static final String TRANSFER_CONTRACT_IN_CREATING_KEY = "transferContractInCreating:";

    @RequestMapping(value = "/loan-list", method = RequestMethod.GET)
    public ModelAndView ConsoleLoanList(@RequestParam(value = "fundPlatform", required = false) FundPlatform fundPlatform,
                                        @RequestParam(value = "status", required = false) LoanStatus status,
                                        @RequestParam(value = "loanId", required = false) Long loanId,
                                        @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                        @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                        @RequestParam(value = "loanName", required = false) String loanName) {
        int pageSize = 10;
        int loanListCount = consoleLoanService.findLoanListCount(fundPlatform, status, loanId, loanName,
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate());
        List<LoanListDto> loanListDtos = consoleLoanService.findLoanList(fundPlatform, status, loanId, loanName,
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate(),
                index, pageSize);
        ModelAndView modelAndView = new ModelAndView("/loan-list");
        modelAndView.addObject("loanListCount", loanListCount);
        modelAndView.addObject("loanListDtos", loanListDtos);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = PaginationUtil.calculateMaxPage(loanListCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("selectedStatus", status);
        modelAndView.addObject("loanId", loanId);
        modelAndView.addObject("loanName", loanName);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("loanStatusList", LoanStatus.values());
        modelAndView.addObject("fundPlatformList", FundPlatform.values());
        modelAndView.addObject("fundPlatform", fundPlatform);
        return modelAndView;
    }

    @RequestMapping(value = "/contract", method = RequestMethod.GET)
    public ModelAndView contract() {
        ModelAndView modelAndView = new ModelAndView("/contract-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/generate-contract", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> generateContract(@RequestParam(value = "businessId") Long businessId,
                                                  @RequestParam(value = "anxinContractType") AnxinContractType anxinContractType) {
        AnxinDataDto baseDataDto = new AnxinDataDto();
        BaseDto<AnxinDataDto> baseDto = new BaseDto<>(baseDataDto);
        if (businessId == null) {
            baseDataDto.setMessage("请填写标的ID!");
            return baseDto;
        }

        if (anxinContractType == null) {
            baseDataDto.setMessage("请填写合同类型!");
            return baseDto;
        }

        if (anxinContractType.equals(AnxinContractType.LOAN_CONTRACT)) {
            if (loanService.findLoanById(businessId) == null) {
                baseDataDto.setMessage("该标的不存在!");
                return baseDto;
            }

            if (redisWrapperClient.exists(LOAN_CONTRACT_IN_CREATING_KEY + businessId)) {
                baseDataDto.setMessage("该标的合同正在生成中,请稍后再试!");
                return baseDto;
            }

            if (CollectionUtils.isEmpty(investService.findContractFailInvest(businessId))) {
                baseDataDto.setMessage("该标的无可生成的合同!");
                return baseDto;
            }
            mqWrapperClient.sendMessage(MessageQueue.LoanFull_GenerateAnXinContract, new BankLoanFullMessage(businessId, null, null, null, null));
        }

        if (anxinContractType.equals(AnxinContractType.TRANSFER_CONTRACT)) {
            TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(businessId);
            if (transferApplicationModel == null) {
                baseDataDto.setMessage("该债权转让不存在!");
                return baseDto;
            }

            if (redisWrapperClient.exists(TRANSFER_CONTRACT_IN_CREATING_KEY + businessId)) {
                baseDataDto.setMessage("该债权转让合同正在生成中,请稍后再试!");
                return baseDto;
            }

            InvestModel investModel = investService.findById(transferApplicationModel.getInvestId());
            if (investModel == null || !Strings.isNullOrEmpty(investModel.getContractNo())) {
                baseDataDto.setMessage("该债权转让无可生成的合同!");
                return baseDto;
            }
            mqWrapperClient.sendMessage(MessageQueue.TransferAnxinContract, new AnxinContractMessage(transferApplicationModel.getId(), AnxinContractType.TRANSFER_CONTRACT.name()));
        }

        return new BaseDto<>(new AnxinDataDto(true, "生成成功！"));
    }

    @ResponseBody
    @RequestMapping(value = "/query-contract", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> queryContract(@RequestParam(value = "businessId") Long businessId,
                                               @RequestParam(value = "anxinContractType") AnxinContractType anxinContractType) {
        AnxinDataDto baseDataDto = new AnxinDataDto();
        BaseDto<AnxinDataDto> baseDto = new BaseDto<>(baseDataDto);

        if (businessId == null) {
            baseDataDto.setMessage("请填写标的ID!");
            return baseDto;
        }

        if (anxinContractType == null) {
            baseDataDto.setMessage("请填写合同类型!");
            return baseDto;
        }

        if (anxinContractType.equals(AnxinContractType.LOAN_CONTRACT)) {
            if (loanService.findLoanById(businessId) == null) {
                baseDataDto.setMessage("该标的不存在!");
                return baseDto;
            }

            if (CollectionUtils.isEmpty(investService.findContractFailInvest(businessId))) {
                baseDataDto.setMessage("该标的合同已经全部生成!");
                return baseDto;
            }
        }

        if (anxinContractType.equals(AnxinContractType.TRANSFER_CONTRACT)) {
            TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(businessId);
            if (transferApplicationModel == null) {
                baseDataDto.setMessage("该债权转让不存在!");
                return baseDto;
            }

            InvestModel investModel = investService.findById(transferApplicationModel.getInvestId());
            if (investModel != null && !Strings.isNullOrEmpty(investModel.getContractNo())) {
                baseDataDto.setMessage("该债权转让合同已经全部生成!");
                return baseDto;
            }
        }

        return anxinWrapperClient.queryContract(new AnxinQueryContractDto(businessId, anxinContractType));
    }

}
