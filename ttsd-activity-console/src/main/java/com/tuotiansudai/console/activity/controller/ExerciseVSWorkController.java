package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleExerciseVSWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class ExerciseVSWorkController {

    @Autowired
    private ActivityConsoleExerciseVSWorkService activityConsoleExerciseVSWorkService;

    @RequestMapping(value = "/exercise-work-list")
    public ModelAndView exchangePrizeList(@RequestParam(value = "index", defaultValue = "1") int index){
        ModelAndView modelAndView=new ModelAndView("exercise-work-list");
        final int pageSize = 10;
        modelAndView.addObject("data",activityConsoleExerciseVSWorkService.exchangePrizeList(index,pageSize));
        return modelAndView;
    }


}
