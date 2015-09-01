package com.tuotiansudai.console.controller;

import com.tuotiansudai.client.OssWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2015/8/21.
 */
@Controller

public class OssUploadController {

    static Logger logger = Logger.getLogger(OssUploadController.class);

    @Autowired
    private OssWrapperClient ossWrapperClient;

    @RequestMapping(value = "/ueditor")
    public void config(HttpServletRequest request,  HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action.equals("config")) {
            response.setContentType("application/json");
            try {
                String exec = "{\"imageActionName\":\"uploadimage\",\"imageFieldName\": \"upfile\",\"imageMaxSize\": 2048000," +
                        "\"imageAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"]," +
                        "\"imageCompressEnable\": true,\"imageCompressBorder\": 1600,\"imageInsertAlign\": \"none\"," +
                        "\"imageUrlPrefix\": \"/upload\",\"imagePathFormat\": \"/upload/{yyyy}{mm}{dd}/{time}{rand:6}\"}";
                PrintWriter writer = response.getWriter();
                writer.write(exec);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        } else if (action.equals("uploadimage")) {
            try {
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                ossWrapperClient.upload(request);
                response.getWriter().print("{'original':'" + ossWrapperClient.getOriginalName() + "','url':'" + ossWrapperClient.getUrl() +
                    "','title':'" + ossWrapperClient.getTitle() + "','state':'" + ossWrapperClient.getState()+"'}");
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
