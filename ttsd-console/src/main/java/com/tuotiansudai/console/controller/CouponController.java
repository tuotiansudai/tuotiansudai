package com.tuotiansudai.console.controller;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.console.dto.ImportExcelDto;
import com.tuotiansudai.console.service.ConsoleCouponService;
import com.tuotiansudai.console.service.ConsoleUserService;
import com.tuotiansudai.console.service.CouponActivationService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.point.repository.mapper.UserPointPrizeMapper;
import com.tuotiansudai.repository.mapper.CouponUserGroupMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/activity-manage")
public class CouponController {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    static Logger logger = Logger.getLogger(CouponController.class);

    @Autowired
    private ConsoleCouponService consoleCouponService;

    @Autowired
    private CouponActivationService couponActivationService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ConsoleUserService consoleUserService;

    @Autowired
    private CouponUserGroupMapper couponUserGroupMapper;

    @Autowired
    private UserPointPrizeMapper userPointPrizeMapper;

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    private static String redisKeyTemplate = "console:{0}:importcouponuser";

    @RequestMapping(value = "/coupon/{couponId}/exchange-code", method = RequestMethod.GET)
    public ModelAndView expertExchangeCode(@PathVariable long couponId, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("兑换码" + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");
        List<List<String>> data = Lists.newArrayList();
        CouponModel couponModel = consoleCouponService.findCouponById(couponId);
        List<String> exchangeCodes = exchangeCodeService.getExchangeCodes(couponId);

        for (int i = 0; i < exchangeCodes.size(); i++) {
            List<String> dataModel = Lists.newArrayList();
            if (i == 0) {
                dataModel.add(couponModel.getCouponType().getName());
                dataModel.add(new DateTime(couponModel.getCreatedTime()).toString("yyyy-MM-dd"));
                dataModel.add(couponModel.getCouponType() == CouponType.INVEST_COUPON ? AmountConverter.convertCentToString(couponModel.getAmount()) + "元" : "");
                dataModel.add(couponModel.getCouponType() == CouponType.INTEREST_COUPON ? couponModel.getRate() * 100 + "%" : "");
                dataModel.add(couponModel.getCouponType() == CouponType.RED_ENVELOPE ? AmountConverter.convertCentToString(couponModel.getAmount()) + "元" : "");
                dataModel.add(new DateTime(couponModel.getStartTime()).toString("yyyy-MM-dd") + "至" + new DateTime(couponModel.getEndTime()).toString("yyyy-MM-dd"));
                dataModel.add(couponModel.getTotalCount() + "个");
                dataModel.add("满" + AmountConverter.convertCentToString(couponModel.getInvestLowerLimit()) + "元");
                dataModel.add(StringUtils.join(Lists.transform(couponModel.getProductTypes(), new Function<ProductType, Object>() {
                    @Override
                    public Object apply(ProductType input) {
                        return input.getName();
                    }
                }), ";"));
                dataModel.add(couponModel.isShared() ? "是" : "否");
            } else {
                dataModel.add("");
                dataModel.add("");
                dataModel.add("");
                dataModel.add("");
                dataModel.add("");
                dataModel.add("");
                dataModel.add("");
                dataModel.add("");
                dataModel.add("");
                dataModel.add("");
            }
            dataModel.add(exchangeCodes.get(i));
            data.add(dataModel);
        }
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ExchangeCodeCsv, data, response.getOutputStream());
        return null;
    }

    @RequestMapping(value = "/red-envelope", method = RequestMethod.GET)
    public ModelAndView redEnvelope() {
        ModelAndView modelAndView = new ModelAndView("/red-envelope");
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        modelAndView.addObject("userGroups", Lists.newArrayList(UserGroup.ALL_USER, UserGroup.IMPORT_USER, UserGroup.CHANNEL,
                UserGroup.EXCHANGER_CODE, UserGroup.MEMBERSHIP_V0, UserGroup.MEMBERSHIP_V1, UserGroup.MEMBERSHIP_V2,
                UserGroup.MEMBERSHIP_V3, UserGroup.MEMBERSHIP_V4, UserGroup.MEMBERSHIP_V5, UserGroup.FIRST_INVEST_ACHIEVEMENT,
                UserGroup.MAX_AMOUNT_ACHIEVEMENT, UserGroup.LAST_INVEST_ACHIEVEMENT));
        long initNum = consoleCouponService.findEstimatedCount(UserGroup.ALL_USER);
        modelAndView.addObject("initNum", initNum);
        return modelAndView;
    }

    @RequestMapping(value = "/coupon", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createCoupon(@Valid @ModelAttribute ExchangeCouponDto exchangeCouponDto, RedirectAttributes redirectAttributes) {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView();
        Long id = exchangeCouponDto.getId();
        try {
            if (id != null) {
                consoleCouponService.editCoupon(loginName, exchangeCouponDto);
            } else {
                consoleCouponService.createCoupon(loginName, exchangeCouponDto);
            }
            if (exchangeCouponDto.getCouponType() == CouponType.INTEREST_COUPON) {
                modelAndView.setViewName("redirect:/activity-manage/coupons-list?couponType=INTEREST_COUPON");
            } else if (exchangeCouponDto.getCouponType() == CouponType.RED_ENVELOPE) {
                modelAndView.setViewName("redirect:/activity-manage/coupons-list?couponType=RED_ENVELOPE");
            }
            return modelAndView;
        } catch (CreateCouponException e) {
            if (id != null) {
                modelAndView.setViewName("redirect:/activity-manage/coupon/" + id + "/edit");
            } else {
                if (exchangeCouponDto.getCouponType() == CouponType.INTEREST_COUPON) {
                    modelAndView.setViewName("redirect:/activity-manage/interest-coupon");
                } else if (exchangeCouponDto.getCouponType() == CouponType.RED_ENVELOPE) {
                    modelAndView.setViewName("redirect:/activity-manage/red-envelope");
                }
            }
            redirectAttributes.addFlashAttribute("coupon", exchangeCouponDto);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return modelAndView;
        }

    }

    @RequestMapping(value = "/interest-coupon", method = RequestMethod.GET)
    public ModelAndView interestCoupon() {
        ModelAndView modelAndView = new ModelAndView("/interest-coupon");
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        modelAndView.addObject("userGroups", Lists.newArrayList(UserGroup.ALL_USER, UserGroup.IMPORT_USER, UserGroup.CHANNEL,
                UserGroup.EXCHANGER_CODE, UserGroup.MEMBERSHIP_V0, UserGroup.MEMBERSHIP_V1, UserGroup.MEMBERSHIP_V2,
                UserGroup.MEMBERSHIP_V3, UserGroup.MEMBERSHIP_V4, UserGroup.MEMBERSHIP_V5, UserGroup.FIRST_INVEST_ACHIEVEMENT,
                UserGroup.MAX_AMOUNT_ACHIEVEMENT, UserGroup.LAST_INVEST_ACHIEVEMENT));
        long initNum = consoleCouponService.findEstimatedCount(UserGroup.ALL_USER);
        modelAndView.addObject("initNum", initNum);
        return modelAndView;
    }

    @RequestMapping(value = "/coupon/{id:^\\d+$}/edit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView edit(@PathVariable long id, Model model) {
        CouponModel couponModel = consoleCouponService.findCouponById(id);
        ModelAndView modelAndView = new ModelAndView();
        switch (couponModel.getCouponType()) {
            case INTEREST_COUPON:
                modelAndView = new ModelAndView("/interest-coupon-edit");
                break;
            case RED_ENVELOPE:
                modelAndView = new ModelAndView("/red-envelope-edit");
                break;
        }
        if (!model.containsAttribute("coupon")) {
            CouponDto couponDto = new CouponDto(couponModel);
            modelAndView.addObject("coupon", couponDto);
        } else {
            Map modelMap = model.asMap();
            for (Object modelKey : modelMap.keySet()) {
                modelAndView.addObject(modelKey.toString(), modelMap.get(modelKey));
            }
        }
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        modelAndView.addObject("userGroups", Lists.newArrayList(UserGroup.values()));
        CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(couponModel.getId());
        if (couponUserGroupModel != null) {
            modelAndView.addObject("agents", consoleUserService.findAllAgents());
            modelAndView.addObject("channels", consoleUserService.findAllUserChannels());
            modelAndView.addObject("agentsOrChannels", couponUserGroupModel.getUserGroupItems());
        }
        if (couponModel.getUserGroup() == UserGroup.IMPORT_USER && redisWrapperClient.hexists(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())), "success")) {
            String importUsers = redisWrapperClient.hget(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())), "success");
            modelAndView.addObject("importUsers", Lists.newArrayList(importUsers.split(",")));
        }
        return modelAndView;

    }

    @RequestMapping(value = "/coupon/{couponId:^\\d+$}/active", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> activeCoupon(@PathVariable long couponId, HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        String loginName = LoginUserInfo.getLoginName();
        couponActivationService.active(loginName, couponId, ip);

        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/coupon/{couponId:^\\d+$}/inactive", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> inactiveCoupon(@PathVariable long couponId, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        couponActivationService.inactive(loginName, couponId, ip);
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/coupon/user-group/{userGroup}/estimate", method = RequestMethod.GET)
    @ResponseBody
    public long findEstimatedCount(@PathVariable UserGroup userGroup) {
        return consoleCouponService.findEstimatedCount(userGroup);
    }

    @RequestMapping(value = "/coupon/{couponId:^\\d+$}/detail", method = RequestMethod.GET)
    public ModelAndView couponDetail(@PathVariable long couponId, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                     @RequestParam(value = "usedStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date usedStartTime,
                                     @RequestParam(value = "usedEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date usedEndTime,
                                     @RequestParam(value = "loginName", required = false) String loginName,
                                     @RequestParam(value = "mobile", required = false) String mobile,
                                     @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/coupon-detail");
        List<CouponDetailsDto> userCoupons = consoleCouponService.findCouponDetail(couponId, isUsed, loginName, mobile, null, null, null, usedStartTime, usedEndTime, index, pageSize);
        int userCouponsCount = consoleCouponService.findCouponDetailCount(couponId, isUsed, loginName, mobile, null, null, usedStartTime, usedEndTime);

        long investAmount = 0l;
        long interest = 0l;
        for(CouponDetailsDto couponDetailsDto : userCoupons){
            investAmount += couponDetailsDto.getInvestAmount() != null ? couponDetailsDto.getInvestAmount() : 0l;
            interest += couponDetailsDto.getAnnualInterest() != null ? couponDetailsDto.getAnnualInterest() : 0l;
        }
        CouponModel couponModel = consoleCouponService.findCouponById(couponId);
        modelAndView.addObject("investAmount", investAmount);
        modelAndView.addObject("interest", interest);
        modelAndView.addObject("userCoupons", userCoupons);
        modelAndView.addObject("isUsed", isUsed);
        modelAndView.addObject("couponId", couponId);
        modelAndView.addObject("usedStartTime", usedStartTime);
        modelAndView.addObject("usedEndTime", usedEndTime);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("userCouponsCount", userCouponsCount);
        long totalPages = PaginationUtil.calculateMaxPage(userCouponsCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        String sideLabType;
        String headLab;
        if (couponModel.getCouponType() == CouponType.INTEREST_COUPON) {
            sideLabType = "statisticsInterestCoupon";
        } else if (couponModel.getCouponType() == CouponType.RED_ENVELOPE) {
            sideLabType = "statisticsRedEnvelope";
        } else if (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON) {
            sideLabType = "statisticsBirthdayCoupon";
        } else {
            sideLabType = "statisticsCoupon";
        }
        if (couponModel.getUserGroup() == UserGroup.EXCHANGER) {
            sideLabType = "couponExchangeManage";
            headLab = "point-manage";
        } else {
            headLab = "activity-manage";
        }
        modelAndView.addObject("sideLabType", sideLabType);
        modelAndView.addObject("headLab", headLab);
        return modelAndView;
    }

    @RequestMapping(value = "/coupon/{couponId:^\\d+$}", method = RequestMethod.DELETE)
    @ResponseBody
    public BaseDto<BaseDataDto> couponDelete(@PathVariable long couponId) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        String loginName = LoginUserInfo.getLoginName();
        dataDto.setStatus(consoleCouponService.deleteCoupon(loginName, couponId));
        return baseDto;
    }

    @RequestMapping(value = "/import-excel", method = RequestMethod.POST)
    @ResponseBody
    public ImportExcelDto importExcel(HttpServletRequest request) throws Exception {
        ImportExcelDto importExcelDto = new ImportExcelDto();
        String uuid = UUIDGenerator.generate();
        String redisKey = MessageFormat.format(redisKeyTemplate, uuid);
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multiRequest.getFile("file");
        InputStream inputStream = null;
        try {
            if (!multipartFile.getOriginalFilename().endsWith(".xls")) {
                importExcelDto.setStatus(false);
                importExcelDto.setMessage("上传失败！请使用2003格式的表格进行上传！");
                return importExcelDto;
            }
            inputStream = multipartFile.getInputStream();
        } catch (NullPointerException e) {
            importExcelDto.setStatus(false);
            importExcelDto.setMessage("您已经取消上传！");
            return importExcelDto;
        }
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        List<String> listSuccess = new ArrayList<>();
        List<String> listFailed = new ArrayList<>();
        for (int rowNum = 0; rowNum < hssfSheet.getLastRowNum() + 1; rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            int firstCellNum = hssfRow.getFirstCellNum();
            HSSFCell hssfCell = hssfRow.getCell(firstCellNum);
            String loginName = getStringVal(hssfCell);
            UserModel userModel = userMapper.findByLoginNameOrMobile(loginName);
            if (userModel == null) {
                listFailed.add(loginName);
            } else {
                listSuccess.add(loginName);
            }
        }
        redisWrapperClient.hset(redisKey, "failed", StringUtils.join(listFailed, ","));
        redisWrapperClient.hset(redisKey, "success", StringUtils.join(listSuccess, ","));

        if (CollectionUtils.isNotEmpty(listFailed)) {
            importExcelDto.setStatus(false);
            importExcelDto.setMessage("用户导入失败," + StringUtils.join(listFailed, ",") + "等用户导入有误!");
        } else {
            importExcelDto.setStatus(true);
            importExcelDto.setFileUuid(uuid);
            importExcelDto.setTotalCount(hssfSheet.getLastRowNum() + 1);
            importExcelDto.setSuccessLoginNames(listSuccess);
            importExcelDto.setMessage("用户导入成功!");
        }
        return importExcelDto;
    }

    private String getStringVal(HSSFCell hssfCell) {
        switch (hssfCell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                return hssfCell.getStringCellValue();
            case Cell.CELL_TYPE_STRING:
                return hssfCell.getStringCellValue();
            default:
                return "";
        }
    }

    @RequestMapping(value = "/coupon/{couponId:^\\d+$}/redis", method = RequestMethod.POST)
    @ResponseBody
    public List<String> getRedisImportExcel(@PathVariable long couponId) {
        List<String> list = new ArrayList<>();
        String redisKey = MessageFormat.format(redisKeyTemplate, String.valueOf(couponId));
        if (redisWrapperClient.hexists(redisKey, "failed")) {
            list.add(redisWrapperClient.hget(redisKey, "failed"));
        }
        if (redisWrapperClient.hexists(redisKey, "success")) {
            list.add(redisWrapperClient.hget(redisKey, "success"));
        }
        return list;
    }

    @RequestMapping(value = "/coupons-list",method=RequestMethod.GET)
    public ModelAndView CouponsList(@RequestParam(value = "couponType",required = false,defaultValue = "RED_ENVELOPE") String couponType,
                                    @RequestParam(value = "couponSource",required = false) String couponSource,
                                    @RequestParam(value = "amount",required = false, defaultValue = "0") float amount,
                                    @RequestParam(value = "index", required = false, defaultValue = "1") int index){
        int pageSize=10;
        ModelAndView modelAndView;
        if (couponType.startsWith("RED_ENVELOPE")){
            modelAndView=new ModelAndView("/red-envelopes");
        }else if(couponType.startsWith("INTEREST_COUPON")){
            modelAndView=new ModelAndView("/interest-coupons");
        }else if(couponType.startsWith("BIRTHDAY_COUPON")){
            modelAndView=new ModelAndView("/birthday-coupons");
        }else{
            modelAndView=new ModelAndView("/coupons");
        }
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("coupons", consoleCouponService.findCouponsByTypeRedAndMoney(index, pageSize,couponType,amount,couponSource));
        int couponsCount = consoleCouponService.findCouponsCountByTypeRedAndMoney(couponType,amount,couponSource);
        modelAndView.addObject("couponsCount", couponsCount);
        long totalPages = PaginationUtil.calculateMaxPage(couponsCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("couponType", couponType==null?"":couponType);
        modelAndView.addObject("couponSource", couponSource==null?"":couponSource);
        modelAndView.addObject("amount", amount==0?"":amount);
        return modelAndView;
    }
}
