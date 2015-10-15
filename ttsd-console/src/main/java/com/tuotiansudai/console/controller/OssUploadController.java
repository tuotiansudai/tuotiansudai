package com.tuotiansudai.console.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.OssWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/21.
 */
@Controller
public class OssUploadController {

    static Logger logger = Logger.getLogger(OssUploadController.class);

    @Autowired
    private OssWrapperClient ossWrapperClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final static Map<String, Object> ueditorConfig = new HashMap<>();
    static {
        ueditorConfig.put("imageActionName","uploadimage");
        ueditorConfig.put("imageFieldName","upfile");
        ueditorConfig.put("imageMaxSize",2048000);
        ueditorConfig.put("imageAllowFiles",new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp"});
        ueditorConfig.put("imageCompressEnable",true);
        ueditorConfig.put("imageCompressBorder",1600);
        ueditorConfig.put("imageInsertAlign","none");
        ueditorConfig.put("imageUrlPrefix","/upload");
        ueditorConfig.put("imagePathFormat","/upload/{yyyy}{mm}{dd}/{time}{rand:6}");
    }

    private String uploadImage = "{'original':'{0}','url':'{1}','title':'{2}','state':'{3}'}";

    @RequestMapping(value = "/ueditor", method = RequestMethod.POST)
    public void uploadimage(HttpServletRequest request,  HttpServletResponse response){
        String action = request.getParameter("action");
        if (action.equals("uploadimage")) {
            try {
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                System.out.println("11111111111111111111111111111111111111111111111111111");
                ossWrapperClient.upload(request);
                System.out.println("22222222222222222222222222222222222222222222222222222");
                response.getWriter().print(MessageFormat.format(uploadImage,ossWrapperClient.getOriginalName(),ossWrapperClient.getUrl(),ossWrapperClient.getTitle(),ossWrapperClient.getState()));
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            } finally {
                try {
                    response.getWriter().close();
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    @RequestMapping(value = "/ueditor", method = RequestMethod.GET)
    public void config(HttpServletRequest request,  HttpServletResponse response) {
        String action = request.getParameter("action");
        PrintWriter writer = null;
        if (action.equals("config")) {
            response.setContentType("application/json");
            try {
                String exec = objectMapper.writeValueAsString(ueditorConfig);;
                writer = response.getWriter();
                writer.write(exec);
                writer.flush();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            } finally {
                writer.close();
            }
        }
    }

}
