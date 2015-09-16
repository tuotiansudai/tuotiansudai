package com.tuotiansudai.web.controller;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.InvestSource;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class InvestController {

    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto) {
        investDto.setInvestSource(InvestSource.WEB);
        BaseDto<PayFormDataDto> baseDto = investService.invest(investDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }

    @RequestMapping(value = "/investor/invests", method = RequestMethod.GET)
    public ModelAndView userInvest(){
        ModelAndView mv = new ModelAndView("/investrecord");
        return mv;
    }

    @RequestMapping(value = "/investor/query_invests", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDto<InvestDetailDto> queryUserInvest(
            Long loanId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            LoanStatus loanStatus, InvestStatus investStatus,
            Integer pageIndex, Integer pageSize
    ) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDto<InvestDetailDto> paginationList = null;
        if (StringUtils.isBlank(loginName)) {
            paginationList = new BasePaginationDto<>();
            paginationList.setRecordDtoList(new ArrayList<InvestDetailDto>());
            paginationList = fakeData(pageIndex,pageSize);
        } else {
            InvestDetailQueryDto queryDto = new InvestDetailQueryDto();
            queryDto.setLoanId(loanId);
            queryDto.setLoginName(loginName);
            queryDto.setBeginTime(beginTime);
            if(endTime!=null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endTime);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                endTime = calendar.getTime();
            }
            queryDto.setEndTime(endTime);
            queryDto.setLoanStatus(loanStatus);
            queryDto.setInvestStatus(investStatus);
            if (pageIndex == null || pageIndex <= 0) {
                queryDto.setPageIndex(1);
            } else {
                queryDto.setPageIndex(pageIndex);
            }
            if (pageSize == null || pageSize <= 0) {
                queryDto.setPageSize(10);
            } else {
                queryDto.setPageSize(pageSize);
            }
            paginationList = investService.queryInvests(queryDto,true);
            List<InvestDetailDto> dtoList = paginationList.getRecordDtoList();
            List<InvestDetailDto> jsonDtoList = new ArrayList<>(dtoList.size());
            for (InvestDetailDto dto : dtoList) {
                jsonDtoList.add(new InvestJsonDetailDto(dto));
            }
            paginationList.setRecordDtoList(jsonDtoList);
        }
        return paginationList;
    }

    private BasePaginationDto<InvestDetailDto> fakeData(Integer pageIndex, Integer pageSize) {
        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        BasePaginationDto<InvestDetailDto> paginationList = new BasePaginationDto<>(pageIndex, pageSize, 327);
        List<InvestDetailDto> list = new ArrayList<>();
        for(int i=0;i<pageSize;i++){
            InvestDetailDto dto = new InvestDetailDto();
            dto.setSource(InvestSource.WEB);
            dto.setUserReferrer("admin");
            dto.setLoanType(LoanType.LOAN_TYPE_1);
            dto.setStatus(InvestStatus.SUCCESS);
            dto.setLoanName("这是测试的标的");
            dto.setAmount("32.12");
            dto.setCreatedTime(new Date());
            dto.setId(Long.parseLong(RandomStringUtils.randomNumeric(8)));
            dto.setIsAutoInvest(false);
            dto.setLoanId(Long.parseLong(RandomStringUtils.randomNumeric(8)));
            dto.setLoanStatus(LoanStatus.COMPLETE);
            dto.setNextRepayDate(new Date());
            dto.setNextRepayAmount(3248);
            InvestJsonDetailDto jsonDto = new InvestJsonDetailDto(dto);
            list.add(jsonDto);
        }
        paginationList.setRecordDtoList(list);
        return paginationList;
    }

    public static class InvestJsonDetailDto extends InvestDetailDto {
        @JsonProperty("nextRepayAmount")
        public String getNextRepayAmountString() {
            return AmountUtil.convertCentToString(super.getNextRepayAmount());
        }

        @JsonIgnore
        @Override
        public long getNextRepayAmount() {
            return super.getNextRepayAmount();
        }

        @JsonFormat(pattern = "yyyy-MM-dd")
        public Date getNextRepayDay() {
            return super.getNextRepayDate();
        }

        @JsonIgnore
        @Override
        public Date getNextRepayDate() {
            return super.getNextRepayDate();
        }

        public String getLoanStatusDesc() {
            return this.getLoanStatus().getDescription();
        }

        public String getLoanTypeName() {
            return this.getLoanType().getName();
        }

        public String getInvestStatus() {
            return this.getStatus().getDescription();
        }

        @JsonIgnore
        @Override
        public InvestStatus getStatus() {
            return super.getStatus();
        }

        @JsonIgnore
        @Override
        public LoanStatus getLoanStatus() {
            return super.getLoanStatus();
        }

        @JsonIgnore
        @Override
        public LoanType getLoanType() {
            return super.getLoanType();
        }

        @Override
        public String getUserReferrer() {
            if (StringUtils.isBlank(super.getUserReferrer())) {
                return "";
            } else {
                return super.getUserReferrer();
            }
        }

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Override
        public Date getCreatedTime() {
            return super.getCreatedTime();
        }

        public InvestJsonDetailDto(InvestDetailDto dto) {
            this.setLoanStatus(dto.getLoanStatus());
            this.setLoginName(dto.getLoginName());
            this.setAmount(dto.getAmount());
            this.setCreatedTime(dto.getCreatedTime());
            this.setId(dto.getId());
            this.setIsAutoInvest(dto.isAutoInvest());
            this.setLoanId(dto.getLoanId());
            this.setLoanName(dto.getLoanName());
            this.setLoanType(dto.getLoanType());
            this.setSource(dto.getSource());
            this.setStatus(dto.getStatus());
            this.setUserReferrer(dto.getUserReferrer());
            this.setNextRepayAmount(dto.getNextRepayAmount());
            this.setNextRepayDate(dto.getNextRepayDate());
        }
    }
}
