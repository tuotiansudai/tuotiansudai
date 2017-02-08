package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.anxin.service.impl.AnxinSignServiceImpl;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.console.service.ConsoleLoanService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.PaginationUtil;
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

    @Autowired
    private ConsoleLoanService consoleLoanService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private AnxinSignService anxinSignService;

    @Autowired
    private InvestService investService;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @RequestMapping(value = "/loan-list", method = RequestMethod.GET)
    public ModelAndView ConsoleLoanList(@RequestParam(value = "status", required = false) LoanStatus status,
                                        @RequestParam(value = "loanId", required = false) Long loanId,
                                        @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                        @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                        @RequestParam(value = "loanName", required = false) String loanName) {
        int pageSize = 10;
        int loanListCount = consoleLoanService.findLoanListCount(status, loanId, loanName,
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate());
        List<LoanListDto> loanListDtos = consoleLoanService.findLoanList(status, loanId, loanName,
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
        modelAndView.addObject("status", status);
        modelAndView.addObject("loanId", loanId);
        modelAndView.addObject("loanName", loanName);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        return modelAndView;
    }


    @RequestMapping(value = "/contract", method = RequestMethod.GET)
    public ModelAndView contract() {
        ModelAndView modelAndView = new ModelAndView("/contract-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/generate-contract", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> generateContract(@RequestParam(value = "businessId", required = true) Long businessId,
                                                 @RequestParam(value = "anxinContractType", required = true) AnxinContractType anxinContractType) {

        BaseDto baseDto = new BaseDto();
        baseDto.setSuccess(false);
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);

        if(businessId == null){
            baseDataDto.setMessage("请填写标的ID!");
            return baseDto;
        }

        if (anxinContractType.equals(AnxinContractType.LOAN_CONTRACT)) {

            if (loanService.findLoanById(businessId) == null) {
                baseDataDto.setMessage("该标的不存在!");
                return baseDto;
            }

            if (redisWrapperClient.exists(AnxinSignServiceImpl.LOAN_CONTRACT_IN_CREATING_KEY + businessId)) {
                baseDataDto.setMessage("该标的合同正在生成中,请稍后再试!");
                return baseDto;
            }

            if (CollectionUtils.isEmpty(investService.findContractFailInvest(businessId))) {
                baseDataDto.setMessage("该标的无可生成的合同!");
                return baseDto;
            }

            return anxinSignService.createLoanContracts(businessId);
        } else {

            TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(businessId);
            if (transferApplicationModel == null) {
                baseDataDto.setMessage("该债权转让不存在!");
                return baseDto;
            }

            if (redisWrapperClient.exists(AnxinSignServiceImpl.TRANSFER_CONTRACT_IN_CREATING_KEY + businessId)) {
                baseDataDto.setMessage("该债权转让合同正在生成中,请稍后再试!");
                return baseDto;
            }

            InvestModel investModel = investService.findById(transferApplicationModel.getInvestId());

            if(investModel == null || !Strings.isNullOrEmpty(investModel.getContractNo())){
                baseDataDto.setMessage("该债权转让无可生成的合同!");
                return baseDto;
            }

            return anxinSignService.createTransferContracts(businessId);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/query-contract", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> queryContract(@RequestParam(value = "businessId", required = true) Long businessId,
                                              @RequestParam(value = "anxinContractType", required = true) AnxinContractType anxinContractType) {

        BaseDto baseDto = new BaseDto();
        baseDto.setSuccess(false);
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        String batchStr;

        if(businessId == null){
            baseDataDto.setMessage("请填写标的ID!");
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

            batchStr = redisWrapperClient.get(AnxinSignServiceImpl.LOAN_BATCH_NO_LIST_KEY + businessId);
        } else {
            TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(businessId);
            if (transferApplicationModel == null) {
                baseDataDto.setMessage("该债权转让不存在!");
                return baseDto;
            }

            InvestModel investModel = investService.findById(transferApplicationModel.getInvestId());

            if (investModel == null || !Strings.isNullOrEmpty(investModel.getContractNo())) {
                baseDataDto.setMessage("该债权转让合同已经全部生成!");
                return baseDto;
            }

            batchStr = redisWrapperClient.get(AnxinSignServiceImpl.TRANSFER_BATCH_NO_LIST_KEY + businessId);
        }

        if(Strings.isNullOrEmpty(batchStr)){
            baseDataDto.setMessage("该标的已经超过7天，无法再次［查询合同结果并更新合同编号］");
            return baseDto;
        }

        anxinSignService.queryContract(businessId, Lists.newArrayList(batchStr.split(",")), anxinContractType);
        return new BaseDto<>(true);
    }
}
