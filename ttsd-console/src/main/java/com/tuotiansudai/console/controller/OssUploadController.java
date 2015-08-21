package com.tuotiansudai.console.controller;

import com.tuotiansudai.client.OssWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2015/8/21.
 */
@Controller
public class OssUploadController {

    static Logger logger = Logger.getLogger(OssUploadController.class);

    @Autowired
    private OssWrapperClient ossWrapperClient;

    @RequestMapping(value = "/ImageUpload", method = RequestMethod.POST)
    public void ossUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            ossWrapperClient.upload(request);
            response.getWriter().print("{'original':'" + ossWrapperClient.getOriginalName() + "','url':'" + ossWrapperClient.getUrl() +
                    "','title':'" + ossWrapperClient.getTitle() + "','state':'" + ossWrapperClient.getState()+"'}");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            response.getWriter().print("出错了！");
        }
    }

}
