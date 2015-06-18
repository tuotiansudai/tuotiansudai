package com.tuotiansudai.web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.tuotiansudai.repository.model.DemoModel;
import com.tuotiansudai.service.DemoService;
import com.tuotiansudai.web.dto.UserDto;
import com.tuotiansudai.web.dto.UserJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    public ModelAndView helloWorld() {
        ModelAndView modelAndView = new ModelAndView("/view.helloworld");
        DemoModel demoModel = demoService.getDemoById("admin");
        modelAndView.addObject("placeHolder", demoModel.getId());
        return modelAndView;
    }

    @JsonView(UserJsonView.User.class)
    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public UserDto jsonHelloWorld() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("userName");
        return userDto;
    }

}
