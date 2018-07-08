package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.console.service.ConsoleLoanService;
import com.tuotiansudai.dto.AnxinDataDto;
import com.tuotiansudai.dto.AnxinQueryContractDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.dto.query.LoanQueryDto;
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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
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
    public ModelAndView ConsoleLoanList( LoanQueryDto loanQueryDto) {
        ModelAndView modelAndView = new ModelAndView("/loan-list");
        loanQueryDto.setRealCount(consoleLoanService.findLoanListCountByDto(loanQueryDto));
        List<LoanListDto> loanListDtos = consoleLoanService.findLoanListByDto(loanQueryDto);
        modelAndView.addObject("loanListDtos", loanListDtos);
        modelAndView.addObject("loanStatusList", LoanStatus.values());
        modelAndView.addObject("fundPlatformList", FundPlatform.values());
        modelAndView.addObject("hasPreviousPage", loanQueryDto.hasPreviousPage());
        modelAndView.addObject("hasNextPage", loanQueryDto.hasNextPage());
        modelAndView.addObject("loanQueryDto",loanQueryDto );
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
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder){
        final SimpleDateFormat dateFormatTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatTime.setLenient(false);
        final SimpleDateFormat dateFormatDate=new SimpleDateFormat("yyyy-MM-dd");
        dateFormatDate.setLenient(false);
        DateFormat dateFormat=new DateFormat() {
            @Override
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
                StringBuffer sb=dateFormatTime.format(date,toAppendTo,fieldPosition);
                return sb;
            }

            @Override
            public Date parse(String source, ParsePosition pos) {
                Date date=null;
                date =dateFormatTime.parse(source,pos);
                if(date != null){
                    return date;
                }
                date =dateFormatDate.parse(source,pos);
                if(date != null){
                    return date;
                }
                return null;
            }
        };
        binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat,true));
    }
}
