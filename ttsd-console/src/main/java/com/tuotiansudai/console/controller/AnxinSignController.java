package com.tuotiansudai.console.controller;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(path = "/anxin-sign")
public class AnxinSignController {

    static Logger logger = Logger.getLogger(AnxinSignController.class);

    private final RedisWrapperClient redisWrapperClient;

    private final UserService userService;

    @Autowired
    public AnxinSignController(RedisWrapperClient redisWrapperClient, UserService userService) {
        this.redisWrapperClient = redisWrapperClient;
        this.userService = userService;
    }

    @RequestMapping(path = "/switch", method = RequestMethod.GET)
    public ModelAndView anxinSwitch() {
        boolean anxinSwitch = Strings.isNullOrEmpty(redisWrapperClient.hget("anxin-sign:switch", "switch")) ?
                true : Boolean.valueOf(redisWrapperClient.hget("anxin-sign:switch", "switch"));

        String whitelist = redisWrapperClient.hget("anxin-sign:switch", "whitelist");
        ModelAndView modelAndView = new ModelAndView("/anxin-switch");
        modelAndView.addObject("switch", anxinSwitch);
        List<String> mobiles =  Strings.isNullOrEmpty(whitelist) ?  Lists.newArrayList() : Arrays.stream(whitelist.split("\\|")).collect(Collectors.toList());

        List<UserModel> userModels = mobiles.stream().map(userService::findByMobile).collect(Collectors.toList());
        modelAndView.addObject("whitelist", userModels);
        return modelAndView;
    }

    @RequestMapping(path = "/switch", method = RequestMethod.POST)
    public ModelAndView updateSwitch(@RequestParam boolean anxinSwitch) {
        redisWrapperClient.hset("anxin-sign:switch", "whitelist", String.valueOf(anxinSwitch));
        return new ModelAndView("redirect:/anxin-sign/switch");
    }

    @RequestMapping(path = "/whitelist", method = RequestMethod.POST)
    public ModelAndView addWhitelist(@RequestParam String mobile) {
        UserModel userModel = userService.findByMobile(mobile);
        String whitelist = redisWrapperClient.hget("anxin-sign:switch", "whitelist");
        if (userModel != null) {
            List<String> mobiles =  Strings.isNullOrEmpty(whitelist) ?  Lists.newArrayList() : Arrays.stream(whitelist.split("\\|")).collect(Collectors.toList());
            if (mobiles.contains(mobile)) {
                mobiles.remove(mobile);
            } else {
                mobiles.add(mobile);
            }
            redisWrapperClient.hset("anxin-sign:switch", "whitelist", Joiner.on("|").join(mobiles));
        }
        return new ModelAndView("redirect:/anxin-sign/switch");
    }
}
