package com.tuotiansudai.api.controller;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.HTrackingRequestDto;
import com.tuotiansudai.api.dto.HTrackingRequestInfoDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/thracking")
public class MobileAppTHrackingController {


//    @RequestMapping(value = "/callback", method = RequestMethod.GET)
//    @ResponseBody
//    public BaseResponseDto callback(HttpServletRequest request) {
//
//        System.out.print(request.getParameterMap());
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("jsonO", request.getParameterMap());
//
//        HTrackingRequestDto list = (HTrackingRequestDto) jsonObj.get("jsonO");
//
//
//        return new BaseResponseDto(ReturnMessage.SUCCESS);
//    }



    public static void main(String[] args){
        List<HTrackingRequestInfoDto> dtos = Lists.newArrayList();
        HTrackingRequestInfoDto d1 = new HTrackingRequestInfoDto();
        d1.setIdfa("KKKKK-OKKSDF-SFDAF");
        d1.setUid("15210006491");
        HTrackingRequestInfoDto d2 = new HTrackingRequestInfoDto();
        d2.setIdfa("KKKKK-OKKSDF-SFDAF");
        d2.setUid("15210006491");
        dtos.add(d1);
        dtos.add(d2);

        HTrackingRequestDto dto = new HTrackingRequestDto();
        dto.setRegs(dtos);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("calendarEvents", dto);
        } catch (JSONException e) {
            // The JSONException is thrown by the JSON.org classes when things
            // are amiss.
            e.printStackTrace();
        }

        System.out.print(jsonObj.toString());

        dto = (HTrackingRequestDto)jsonObj.get("calendarEvents");

    }

}
