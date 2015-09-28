package com.tuotiansudai.console.controller;

import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable long id) {
        ModelAndView mv = new ModelAndView("/");

        return mv;
    }

}
