package com.tuotiansudai.web.controller;

import cfca.sadk.algorithm.common.PKIException;
import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping(path = "/anxinSign")
public class AnxinSignController {

    @Autowired
    private AnxinSignService anxinSignService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView anxinSignPage() {

        String loginName = LoginUserInfo.getLoginName();

        AnxinSignPropertyModel anxinProp = anxinSignService.getAnxinSignProp(loginName);
        anxinProp = anxinProp != null ? anxinProp : new AnxinSignPropertyModel();

        if (anxinSignService.hasAuthed(loginName)) {
            // 如果以前授权过，则进入列表页
            return new ModelAndView("/myAccount/anxin-sign-list", "anxinProp", anxinProp);
        } else {
            // 否则进入开通安心签账户的初始页面
            return new ModelAndView("/myAccount/anxin-sign-init", "anxinProp", anxinProp);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public BaseDto createAccount() throws PKIException {
        String loginName = LoginUserInfo.getLoginName();
        return anxinSignService.createAccount3001(loginName);
    }

    @ResponseBody
    @RequestMapping(value = "/sendCaptcha", method = RequestMethod.POST)
    public BaseDto sendCaptcha(boolean isVoice) throws PKIException {
        String loginName = LoginUserInfo.getLoginName();
        return anxinSignService.sendCaptcha3101(loginName, isVoice);
    }

    @ResponseBody
    @RequestMapping(value = "/verifyCaptcha", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> verifyCaptcha(String captcha, boolean skipAuth, HttpServletRequest request) throws PKIException {
        String ip = RequestIPParser.parse(request);
        String loginName = LoginUserInfo.getLoginName();
        return anxinSignService.verifyCaptcha3102(loginName, captcha, skipAuth, ip);
    }

    @ResponseBody
    @RequestMapping(value = "/switchSkipAuth", method = RequestMethod.POST)
    public BaseDto switchSkipAuth(boolean open) {
        String loginName = LoginUserInfo.getLoginName();
        return anxinSignService.switchSkipAuth(loginName, open);
    }

    @ResponseBody
    @RequestMapping(value = "/download-contract", method = RequestMethod.GET)
    public void downloadContract(HttpServletRequest request, HttpServletResponse response) {
        //在SSH框架中，可以通过HttpServletResponse response=ServletActionContext.getResponse();取出Respond对象
        //清空一下response对象，否则出现缓存什么的
        response.reset();
        //指明这是一个下载的respond
        response.setContentType("application/x-download");
        //这里是提供给用户默认的文件名
        String filename = "中文是没有问题的.txt";
        //双重解码、防止乱码
        try {
            filename = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        /*
         * 这里是最关键的一步。
         * 直接把这个东西写到response输出流里面，给用户下载。
         * 如果输出到c:\b.txt就是：
         * PrintWriter printwriter = new PrintWriter(new FileWriter("c:\\b.txt",true));
         * 现在写好respond头，就写成：
         * PrintWriter printwriter = new PrintWriter(response.getOutputStream());
         * 把PrintWriter的输出点改一下
         */
        PrintWriter printwriter = null;
        try {
            printwriter = new PrintWriter(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        printwriter.println("呵呵！");
        //打印流的所有输出内容，必须关闭这个打印流才有效
        printwriter.close();
    }

}
