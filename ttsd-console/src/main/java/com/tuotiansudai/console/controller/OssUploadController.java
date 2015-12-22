package com.tuotiansudai.console.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.OssWrapperClient;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class OssUploadController {

    static Logger logger = Logger.getLogger(OssUploadController.class);

    @Autowired
    private OssWrapperClient ossWrapperClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final static Map<String, Object> ueditorConfig = new HashMap<>();

    static {
        ueditorConfig.put("imageActionName", "uploadimage");
        ueditorConfig.put("imageFieldName", "upfile");
        ueditorConfig.put("imageMaxSize", 2048000);
        ueditorConfig.put("imageAllowFiles", new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp"});
        ueditorConfig.put("imageCompressEnable", true);
        ueditorConfig.put("imageCompressBorder", 1600);
        ueditorConfig.put("imageInsertAlign", "none");
        ueditorConfig.put("imageUrlPrefix", "/upload");
        ueditorConfig.put("imagePathFormat", "/upload/{yyyy}{mm}{dd}");
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public void upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        JSONArray jsonArray = new JSONArray();
        String imgTemplate = "\'<img src=\'{0}\' class=\'file-preview-image\' alt=\'{1}\' title=\'{2}\'>\'";
        StringBuffer stringBuffer = new StringBuffer("[");
        Iterator<String> itr =  request.getFileNames();
        MultipartFile dfi = null;
        String fileName = "";
        while (itr.hasNext()) {
            dfi  = request.getFile(itr.next());
            fileName = dfi.getOriginalFilename();
            String ossPath = "";
            try {
                String url = request.getRequestURL().toString();
                ossPath = ossWrapperClient.upload(FilenameUtils.getExtension(fileName), dfi.getInputStream(), rootPath, url.substring(0,url.lastIndexOf("/")+1), true);
                jsonArray.put(MessageFormat.format(imgTemplate, ossPath, fileName, fileName));
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("initialPreview", jsonArray);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                response.getWriter().close();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @RequestMapping(value = "/ueditor", method = RequestMethod.POST)
    public void upload(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action.equals("uploadimage")) {
            try {
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                JSONObject jsonObject = uploadFile(request);
                response.getWriter().print(jsonObject);
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

    private JSONObject uploadFile(HttpServletRequest request) {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return buildUploadFileResult("未包含文件上传域", "", "", "");
        }
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile dfi = multiRequest.getFile("upfile");
        String originalName = dfi.getOriginalFilename().substring(dfi.getOriginalFilename().lastIndexOf(System.getProperty("file.separator")) + 1);
        String fileExtName = FilenameUtils.getExtension(originalName);
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String url = request.getRequestURL().toString();
            String absoluteUrl = ossWrapperClient.upload(fileExtName, dfi.getInputStream(), rootPath, url.substring(0,url.lastIndexOf("/")+1), false);
            if (absoluteUrl.indexOf(":") > 0 ) {
                absoluteUrl = absoluteUrl.substring(absoluteUrl.indexOf("upload"), absoluteUrl.length());
            }
            String relativeUrl = absoluteUrl.substring(absoluteUrl.indexOf("/"), absoluteUrl.length());
            return buildUploadFileResult("SUCCESS", originalName, relativeUrl, absoluteUrl);
        } catch (Exception e) {
            return buildUploadFileResult(e.getMessage(), "", "", "");
        }
    }

    private JSONObject buildUploadFileResult(String state, String originalName, String url, String title) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("original", originalName);
        jsonObject.put("url", url);
        jsonObject.put("title", title);
        jsonObject.put("state", state);
        return jsonObject;
    }


    @RequestMapping(value = "/ueditor", method = RequestMethod.GET)
    public void config(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action.equals("config")) {
            response.setContentType("application/json");
            try (PrintWriter writer = response.getWriter()) {
                String exec = objectMapper.writeValueAsString(ueditorConfig);
                writer.write(exec);
                writer.flush();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

}
