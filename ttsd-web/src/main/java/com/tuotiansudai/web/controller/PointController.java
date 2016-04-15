package com.tuotiansudai.web.controller;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.dto.PointTaskDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.service.PointExchangeService;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/point")
public class PointController {

    static Logger logger = Logger.getLogger(PointController.class);

    @Autowired
    private SignInService signInService;
    @Autowired
    private PointService pointService;
    @Autowired
    private PointBillMapper pointBillMapper;
    @Autowired
    private PointTaskService pointTaskService;
    @Autowired
    private PointExchangeService pointExchangeService;
    @Autowired
    private CouponService couponService;

    @RequestMapping(path = "/sign-in", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> signIn() {
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = signInService.signIn(loginName);
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);
        baseDto.setSuccess(true);
        return baseDto;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView myPoint(){
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/point");
        long myPoint = pointService.getAvailablePoint(loginName);
        List<PointBillModel> pointBillModels = pointBillMapper.findByLoginName(loginName);
        if(pointBillModels != null){
            pointBillModels =  pointBillModels.size() > 3 ?pointBillModels.subList(0,3):pointBillModels;
            List<Map<String,Date>> obtainedPoints = Lists.newArrayList();
            for(PointBillModel pointBillModel : pointBillModels){
                obtainedPoints.add(Maps.newHashMap(ImmutableMap.<String,Date>builder().put("" + pointBillModel.getPoint(),pointBillModel.getCreatedTime()).build()));
            }
            modelAndView.addObject("obtainedPoints",obtainedPoints);
        }
        boolean signedIn = signInService.signInIsSuccess(loginName);
        List<PointTaskDto> pointTaskDtos = pointTaskService.displayPointTask(1,10,loginName);
        modelAndView.addObject("pointTaskDtos",pointTaskDtos);
        modelAndView.addObject("signedIn",signedIn);
        modelAndView.addObject("myPoint",myPoint);
        List<ExchangeCouponDto> exchangeCouponDtos = pointExchangeService.findExchangeableCouponList();
        modelAndView.addObject("exchangeCouponDtos", exchangeCouponDtos);
        return modelAndView;
    }

    @RequestMapping(value = "/exchange_coupon_list", method = RequestMethod.GET)
    public ModelAndView exchangeCouponList(){
        ModelAndView modelAndView = new ModelAndView("/point_exchange_list");
        return modelAndView;

    }

    @RequestMapping(value = "/{couponId}/exchange", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto exchangeCoupon(@PathVariable long couponId) {
        BaseDataDto baseDataDto = new BaseDataDto();
        long exchangePoint = couponService.findCouponExchangeByCouponId(couponId).getExchangePoint();
        boolean sufficient = pointExchangeService.exchangeableCoupon(couponId, LoginUserInfo.getLoginName());
        CouponModel couponModel = couponService.findCouponById(couponId);
        boolean couponExchangeable = couponModel.getIssuedCount() < couponModel.getTotalCount();
        if (sufficient && pointExchangeService.exchangeCoupon(couponId, LoginUserInfo.getLoginName(), exchangePoint)) {
            baseDataDto.setStatus(true);
        } else {
            baseDataDto.setStatus(false);
            if (!sufficient) {
                if(!couponExchangeable){
                    baseDataDto.setMessage("coupon exchangeable insufficient");
                }
                else{
                    baseDataDto.setMessage("point insufficient");
                }
            }
        }
        return baseDataDto;
    }
}
