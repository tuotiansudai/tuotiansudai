package com.tuotiansudai.web.controller;


import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.repository.model.ExperienceLoanDto;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.ExperienceLoanDetailService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping(value = "/loan")
public class ExperienceLoanDetailController {

    @Autowired
    private ExperienceLoanDetailService ExperienceLoanDetailService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public ModelAndView getLoanDetail() {
        ExperienceLoanDto experienceLoanDto = ExperienceLoanDetailService.findExperienceLoanDtoDetail(1,LoginUserInfo.getLoginName());
        List<UserCouponView> userCouponViewList = userCouponMapper.findUnusedCoupons(LoginUserInfo.getLoginName());
        UserCouponView coupon = null;
        if(CollectionUtils.isNotEmpty(userCouponViewList)){
            for(UserCouponView userCouponView : userCouponViewList){
                if(userCouponView.getProductTypeList().get(0).equals(ProductType.EXPERIENCE)){
                    coupon = userCouponView;
                    break;
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView("/experience-loan", "responsive", true);
        modelAndView.addObject("loan", experienceLoanDto);
        modelAndView.addObject("coupon", coupon);
        return modelAndView;
    }

}
