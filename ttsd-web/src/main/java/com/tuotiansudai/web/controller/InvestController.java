package com.tuotiansudai.web.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.InvestSource;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/invest")
public class InvestController {

    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto) {
        investDto.setInvestSource(InvestSource.WEB);
        BaseDto<PayFormDataDto> baseDto = investService.invest(investDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDto<InvestDetailDto> queryUserInvest(
            Long loanId,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date beginTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
            LoanStatus loanStatus, InvestStatus investStatus,
            Integer pageIndex, Integer pageSize
    ) {
        BasePaginationDto<InvestDetailDto> paginationList = null;
        if (StringUtils.isBlank(LoginUserInfo.getLoginName())) {
            paginationList = new BasePaginationDto<>();
            paginationList.setRecordDtoList(new ArrayList<InvestDetailDto>());
        } else {
            InvestDetailQueryDto queryDto = new InvestDetailQueryDto();
            queryDto.setLoanId(loanId);
            queryDto.setLoginName(LoginUserInfo.getLoginName());
            queryDto.setBeginTime(beginTime);
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
            paginationList = investService.queryInvests(queryDto);
            List<InvestDetailDto> dtoList = paginationList.getRecordDtoList();
            List<InvestDetailDto> jsonDtoList = new ArrayList<>(dtoList.size());
            for (InvestDetailDto dto : dtoList) {
                jsonDtoList.add(new InvestJsonDetailDto(dto));
            }
            paginationList.setRecordDtoList(jsonDtoList);
        }
        return paginationList;
    }

    public static class InvestJsonDetailDto extends InvestDetailDto {
        private String repayAmount;
        private String repayDay;

        public String getRepayAmount() {
            return repayAmount;
        }

        public void setRepayAmount(String repayAmount) {
            this.repayAmount = repayAmount;
        }

        public String getRepayDay() {
            return repayDay;
        }

        public void setRepayDay(String repayDay) {
            this.repayDay = repayDay;
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
        }
    }
}
