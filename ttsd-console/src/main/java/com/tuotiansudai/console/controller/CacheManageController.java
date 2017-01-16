package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.CacheManageService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/cache-manage")
public class CacheManageController {

    @Autowired
    private CacheManageService cacheManageService;

    @RequestMapping(path = "/clear-db-cache", method = RequestMethod.GET)
    public ModelAndView clearDbCache() {
        ModelAndView modelAndView = new ModelAndView("/clear-db-cache");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/clear-db-cache", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> clearCache() {
        String statusCode = cacheManageService.clearMybatisCache();

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        baseDataDto.setMessage(statusCode);
        baseDataDto.setStatus(true);
        baseDto.setSuccess(true);
        return baseDto;
    }
}
