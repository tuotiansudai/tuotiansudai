package com.tuotiansudai.console.controller;


import com.tuotiansudai.point.service.GoodsManageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/activity-manage")
public class GoodsManageController {
    private static Logger logger = Logger.getLogger(GoodsManageController.class);

    @Autowired
    private GoodsManageService goodsManageService;
}
