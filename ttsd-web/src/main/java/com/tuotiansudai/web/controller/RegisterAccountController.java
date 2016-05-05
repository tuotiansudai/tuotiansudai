package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(path = "/register/account")
public class RegisterAccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registerAccount() {
        return new ModelAndView("/register-account", "responsive", true);
    }

    private static Hashtable regionCoude = null;

    @RequestMapping(value = "/identity-number/{identityNumber:^[1-9]\\d{13,16}[a-zA-Z0-9]{1}$}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> isIdentityNumberExist(@PathVariable String identityNumber) {
        boolean isExist = accountService.isIdentityNumberExist(identityNumber);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(isExist);

        return baseDto;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView registerAccount(@Valid @ModelAttribute RegisterAccountDto registerAccountDto, RedirectAttributes redirectAttributes) {
        boolean isRegisterSuccess = false;

        if(checkIdentity(registerAccountDto.getIdentityNumber())){
            registerAccountDto.setLoginName(LoginUserInfo.getLoginName());
            registerAccountDto.setMobile(LoginUserInfo.getMobile());
            BaseDto<PayDataDto> dto = this.userService.registerAccount(registerAccountDto);
            isRegisterSuccess = dto.getData().getStatus();
        }

        if (!isRegisterSuccess) {
            redirectAttributes.addFlashAttribute("originalFormData", registerAccountDto);
            redirectAttributes.addFlashAttribute("success", false);
        }

        return new ModelAndView(isRegisterSuccess ? "redirect:/loan-list" : "redirect:/register/account");
    }

    private static boolean checkIdentity(String IDStr){
        String Ai = "";
        String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2" };
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            return false;
        }

        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }

        if (isNumeric(Ai) == false) {
            return false;
        }

        String first = Ai.substring(0, 1);
        String firstYear = Ai.substring(6, 8);
        String strYear = Ai.substring(6, 10);
        String strMonth = Ai.substring(10, 12);
        String strDay = Ai.substring(12, 14);
        if(Integer.parseInt(first) == 0 || Integer.parseInt(first) > 9){
            return false;
        }
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            return false;
        }

        if (Integer.parseInt(firstYear) < 19 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false;
        }
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            return false;
        }

        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (!Ai.equals(IDStr)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private static Hashtable GetAreaCode() {
        if(regionCoude == null){
            regionCoude = new Hashtable();
            regionCoude.put("11", "北京");
            regionCoude.put("12", "天津");
            regionCoude.put("13", "河北");
            regionCoude.put("14", "山西");
            regionCoude.put("15", "内蒙古");
            regionCoude.put("21", "辽宁");
            regionCoude.put("22", "吉林");
            regionCoude.put("23", "黑龙江");
            regionCoude.put("31", "上海");
            regionCoude.put("32", "江苏");
            regionCoude.put("33", "浙江");
            regionCoude.put("34", "安徽");
            regionCoude.put("35", "福建");
            regionCoude.put("36", "江西");
            regionCoude.put("37", "山东");
            regionCoude.put("41", "河南");
            regionCoude.put("42", "湖北");
            regionCoude.put("43", "湖南");
            regionCoude.put("44", "广东");
            regionCoude.put("45", "广西");
            regionCoude.put("46", "海南");
            regionCoude.put("50", "重庆");
            regionCoude.put("51", "四川");
            regionCoude.put("52", "贵州");
            regionCoude.put("53", "云南");
            regionCoude.put("54", "西藏");
            regionCoude.put("61", "陕西");
            regionCoude.put("62", "甘肃");
            regionCoude.put("63", "青海");
            regionCoude.put("64", "宁夏");
            regionCoude.put("65", "新疆");
            regionCoude.put("71", "台湾");
            regionCoude.put("81", "香港");
            regionCoude.put("82", "澳门");
            regionCoude.put("91", "国外");
        }
        return regionCoude;
    }
}
