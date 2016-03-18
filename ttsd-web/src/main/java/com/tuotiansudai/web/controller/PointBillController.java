package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.AutoInvestMonthPeriod;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.util.Date;

@Controller
@RequestMapping(path = "/point")
public class PointBillController {

    @Autowired
    private PointBillService pointBillService;

    @RequestMapping(value = "/point-list", method = RequestMethod.GET)
    public ModelAndView investList() {
        return new ModelAndView("/point-bill-list");
    }

    @RequestMapping(value = "/point-bill-list-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> pointBillListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                         @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                         @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                         @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                         @RequestParam(name = "businessType", required = false) PointBusinessType businessType) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<PointBillPaginationItemDataDto> dataDto = pointBillService.getPointBillPagination(loginName, index, pageSize, startTime, endTime, businessType);
        dataDto.setStatus(true);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }
}
