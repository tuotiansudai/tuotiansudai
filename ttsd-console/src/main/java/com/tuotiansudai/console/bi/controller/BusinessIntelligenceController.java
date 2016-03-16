package com.tuotiansudai.console.bi.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.bi.dto.Granularity;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.bi.dto.UserStage;
import com.tuotiansudai.console.bi.repository.model.InvestViscosityDetailTableView;
import com.tuotiansudai.console.bi.repository.model.InvestViscosityDetailView;
import com.tuotiansudai.console.bi.repository.model.KeyValueModel;
import com.tuotiansudai.console.bi.service.BusinessIntelligenceService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bi")
public class BusinessIntelligenceController {
    @Autowired
    private BusinessIntelligenceService businessIntelligenceService;

    private static final List<String> PROVINCES = Arrays.asList("北京", "上海", "天津", "重庆", "辽宁", "吉林", "黑龙江", "河北", "山西", "陕西", "甘肃", "青海", "山东", "安徽", "江苏", "浙江", "河南",
            "湖北", "湖南", "江西", "福建", "云南", "海南", "四川", "贵州", "广东", "内蒙古", "新疆", "广西", "西藏", "宁夏", "香港", "澳门", "台湾");

    @ResponseBody
    @RequestMapping(value = "/channels", method = RequestMethod.GET)
    public List<String> queryChannels() {
        return businessIntelligenceService.getChannels();
    }

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
            @RequestParam(name = "province", required = false) String province,
            @RequestParam(name = "userStage", required = false) UserStage userStage,
            @RequestParam(name = "roleStage", required = false) RoleStage roleStage,
            @RequestParam(name = "channel", required = false) String channel) {
        return businessIntelligenceService.queryUserRegisterTrend(granularity, startTime, endTime, province, userStage, roleStage, channel);
    }

    @ResponseBody
    @RequestMapping(value = "/user-recharge-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserRechargeTrend(
            @RequestParam(name = "granularity") Granularity granularity,
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province", required = false) String province) {
        return businessIntelligenceService.queryUserRechargeTrend(granularity, startTime, endTime, province);
    }

    @ResponseBody
    @RequestMapping(value = "/user-withdraw-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserWithdrawTrend(
            @RequestParam(name = "granularity") Granularity granularity,
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province", required = false) String province) {
        return businessIntelligenceService.queryUserWithdrawTrend(granularity, startTime, endTime, province);
    }

    @ResponseBody
    @RequestMapping(value = "/user-invest-viscosity", method = RequestMethod.GET)
    public List<KeyValueModel> queryInvestViscosity(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province", required = false) String province) {
        return businessIntelligenceService.queryInvestViscosity(startTime, endTime, province);
    }

    @ResponseBody
    @RequestMapping(value = "/user-invest-viscosity-detail", method = RequestMethod.GET)
    public InvestViscosityDetailTableView queryInvestViscosityDetail(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province", required = false) String province,
            @RequestParam(name = "loanCount", required = true) int loanCount,
            @RequestParam(name = "pageNo", required = true) int pageNo,
            @RequestParam(name = "pageSize", required = true) int pageSize) {
        return businessIntelligenceService.queryInvestViscosityDetail(startTime, endTime, province, loanCount, pageNo, pageSize);
    }


    @RequestMapping(value = "/user-invest-viscosity-detail-csv", method = RequestMethod.GET)
    public void getInvestList(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province", required = false) String province,
            @RequestParam(name = "loanCount", required = true) int loanCount,
            HttpServletResponse response) throws IOException {

        InvestViscosityDetailTableView view = businessIntelligenceService.queryInvestViscosityDetail(startTime, endTime, province, loanCount, 1, Integer.MAX_VALUE);
        List<InvestViscosityDetailView> items = view.getItems();

        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("用户续投详情.csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/csv");
        List<List<String>> data = Lists.newArrayList();
        for (int i = 0; i < items.size(); i++) {
            List<String> dataModel = Lists.newArrayList();
            dataModel.add(items.get(i).getLoginName());
            dataModel.add(items.get(i).getUserName());
            dataModel.add(items.get(i).getMobile());
            dataModel.add("1".equals(items.get(i).getIsStaff()) ? "是" : "否");
            dataModel.add(items.get(i).getReferrer());
            dataModel.add(items.get(i).getReferrerUserName());
            dataModel.add("1".equals(items.get(i).getIsReferrerStaff()) ? "是" : "否");
            dataModel.add(AmountConverter.convertCentToString(items.get(i).getTotalAmount()));
            dataModel.add(String.valueOf(items.get(i).getLoanCount()));
            dataModel.add(new DateTime(items.get(i).getLastInvestTime()).toString("yyyy-MM-dd HH:mm:ss"));
            data.add(dataModel);
        }
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.BIInvestViscosity, data, response.getOutputStream());
    }

    @ResponseBody
    @RequestMapping(value = "/user-invest-amount-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserInvestAmountTrend(
            @RequestParam(name = "granularity") Granularity granularity,
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province", required = false) String province,
            @RequestParam(name = "roleStage", required = false) RoleStage roleStage,
            @RequestParam(name = "channel", required = false) String channel) {
        return businessIntelligenceService.queryUserInvestAmountTrend(granularity, startTime, endTime, province, roleStage, channel);
    }

    @ResponseBody
    @RequestMapping(value = "/user-invest-count-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserInvestCountTrend(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province", required = false) String province) {
        return businessIntelligenceService.queryUserInvestCountTrend(startTime, endTime, province);
    }

    @ResponseBody
    @RequestMapping(value = "/register-user-age-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryRegisterUserAgeTrend(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province", required = false) String province) {
        return businessIntelligenceService.queryUserAgeTrend(startTime, endTime, province, null);
    }

    @ResponseBody
    @RequestMapping(value = "/investor-user-age-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryInvestorUserAgeTrend(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(name = "province", required = false) String province) {
        return businessIntelligenceService.queryUserAgeTrend(startTime, endTime, province, "true");
    }

    @ResponseBody
    @RequestMapping(value = "/loan-amount-distribution", method = RequestMethod.GET)
    public List<KeyValueModel> queryLoanAmountDistribution(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        return businessIntelligenceService.queryLoanAmountDistribution(startTime, endTime);
    }

    @ResponseBody
    @RequestMapping(value = "/loan-raising-time-costing-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryLoanRaisingTimeCostingTrend(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        return businessIntelligenceService.queryLoanRaisingTimeCostingTrend(startTime, endTime);
    }

    @ResponseBody
    @RequestMapping(value = "/withdraw-user-count-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryWithdrawUserCountTrend(
            @RequestParam(name = "granularity") Granularity granularity,
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        return businessIntelligenceService.queryWithdrawUserCountTrend(startTime, endTime, granularity);
    }
}
