package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller()
@RequestMapping(path = "/")
public class HelloWorld {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        UserModel loginName = userMapper.findByLoginName("sidneygao");
        ModelAndView modelAndView = new ModelAndView("/hello-world");
        modelAndView.addObject("id", loginName.getId());
        return modelAndView;
    }
}
