package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.Granularity;
import com.tuotiansudai.dto.RoleStage;
import com.tuotiansudai.dto.UserStage;
import com.tuotiansudai.repository.model.KeyValueModel;
import com.tuotiansudai.service.BusinessIntelligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bi")
public class BusinessIntelligenceController {
    @Autowired
    private BusinessIntelligenceService businessIntelligenceService;

    private static final List<String> PROVINCES = Arrays.asList("北京","上海","天津","重庆","辽宁","吉林","黑龙江","河北","山西","陕西","甘肃","青海","山东","安徽","江苏","浙江","河南",
            "湖北","湖南","江西","福建","云南","海南","四川","贵州","广东","内蒙古","新疆","广西","西藏","宁夏","香港","澳门","台湾");

    @ResponseBody
    @RequestMapping(value = "/province", method = RequestMethod.GET)
    public List<String> queryProvinces() {
        return PROVINCES;
    }

    @ResponseBody
    @RequestMapping(value = "/user-register-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserRegisterTrend(
            @RequestParam(name = "granularity") Granularity granularity,
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province",required = false) String province,
            @RequestParam(name = "userStage",required = false) UserStage userStage,
            @RequestParam(name = "roleStage",required = false) RoleStage roleStage){
        return businessIntelligenceService.queryUserRegisterTrend(granularity, startTime, endTime, province, userStage, roleStage);
    }

    @ResponseBody
    @RequestMapping(value = "/user-recharge-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserRechargeTrend(
            @RequestParam(name = "granularity") Granularity granularity,
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province",required = false) String province){
        return businessIntelligenceService.queryUserRechargeTrend(granularity, startTime, endTime, province);
    }

    @ResponseBody
    @RequestMapping(value = "/user-withdraw-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserWithdrawTrend(
            @RequestParam(name = "granularity") Granularity granularity,
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province",required = false) String province){
        return businessIntelligenceService.queryUserWithdrawTrend(granularity, startTime, endTime, province);
    }

    @ResponseBody
    @RequestMapping(value = "/user-invest-viscosity", method = RequestMethod.GET)
    public List<KeyValueModel> queryInvestViscosity(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province",required = false) String province) {
        return businessIntelligenceService.queryInvestViscosity(startTime, endTime, province);
    }

    @ResponseBody
    @RequestMapping(value = "/user-invest-amount-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserInvestAmountTrend(
            @RequestParam(name = "granularity") Granularity granularity,
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province",required = false) String province,
            @RequestParam(name = "roleStage",required = false) RoleStage roleStage){
        return businessIntelligenceService.queryUserInvestAmountTrend(granularity, startTime, endTime, province, roleStage);
    }

    @ResponseBody
    @RequestMapping(value = "/user-invest-count-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserInvestCountTrend(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province",required = false) String province){
        return businessIntelligenceService.queryUserInvestCountTrend(startTime, endTime, province);
    }

    @ResponseBody
    @RequestMapping(value = "/register-user-age-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryRegisterUserAgeTrend(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province",required = false) String province){
        return businessIntelligenceService.queryUserAgeTrend(startTime, endTime, province, null);
    }

    @ResponseBody
    @RequestMapping(value = "/investor-user-age-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryInvestorUserAgeTrend(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province",required = false) String province){
        return businessIntelligenceService.queryUserAgeTrend(startTime, endTime, province, "true");
    }

    @ResponseBody
    @RequestMapping(value = "/loan-raising-time-costing-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryLoanRaisingTimeCostingTrend(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime){
        return businessIntelligenceService.queryLoanRaisingTimeCostingTrend(startTime, endTime);
    }
}
