package com.tuotiansudai.console.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
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
import java.util.Iterator;
import java.util.Map;

@Controller
public class OssUploadController {

    private static Logger logger = Logger.getLogger(OssUploadController.class);

    @Autowired
    private OssWrapperClient ossWrapperClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final static Map<String, Object> ueditorConfig = Maps.newHashMap(ImmutableMap.<String, Object>builder()
            .put("imageActionName", "uploadimage")
            .put("imageFieldName", "upfile")
            .put("imageMaxSize", 2048000)
            .put("imageAllowFiles", new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp"})
            .put("imageCompressEnable", true)
            .put("imageCompressBorder", 1600)
            .put("imageInsertAlign", "none")
            .put("imageUrlPrefix", "")
            .put("imagePathFormat", "/upload/{yyyy}{mm}{dd}").build());

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        JSONArray jsonArray = new JSONArray();
        String imgTemplate = "\'<img src=\'{0}\' class=\'file-preview-image\' alt=\'{1}\' title=\'{2}\'>\'";
        Iterator<String> itr = request.getFileNames();
        while (itr.hasNext()) {
            MultipartFile dfi = request.getFile(itr.next());
            String fileName = dfi.getOriginalFilename();
            try {
                String ossPath = ossWrapperClient.upload(FilenameUtils.getExtension(fileName), dfi.getInputStream(), true);
                jsonArray.put(MessageFormat.format(imgTemplate, ossPath, fileName, fileName));
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        JSONObject jsonObject = new JSONObject();
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
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        if (!request.getParameter("action").equals("uploadimage")) {
            return;
        }

        try (PrintWriter writer = response.getWriter()) {
            if (!ServletFileUpload.isMultipartContent(request)) {
                writer.print(new JSONObject(Maps.newHashMap(ImmutableMap.builder().put("original", "").put("url", "").put("title", "").put("state", "未包含文件上传域").build())));
                return;
            }

            JSONObject jsonObject = uploadFile(((MultipartHttpServletRequest) request).getFile("upfile"));
            writer.print(jsonObject);
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0}|{1}", "[OSS UPLOAD]", e.getLocalizedMessage()), e);
        }
    }

    private JSONObject uploadFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String originalName = originalFilename.substring(originalFilename.lastIndexOf(System.getProperty("file.separator")) + 1);
        try {
            String absoluteUrl = ossWrapperClient.upload(FilenameUtils.getExtension(originalName), multipartFile.getInputStream(), false);
            if (absoluteUrl.indexOf(":") > 0) {
                absoluteUrl = absoluteUrl.substring(absoluteUrl.indexOf("/upload"), absoluteUrl.length());
            }
            String relativeUrl = absoluteUrl.substring(absoluteUrl.indexOf("/"), absoluteUrl.length());
            logger.info(MessageFormat.format("[OSS UPLOAD] originalName: {0}, absoluteUrl: {}, relativeUrl: {}", originalName, absoluteUrl, relativeUrl));
            return new JSONObject(Maps.newHashMap(ImmutableMap.builder()
                    .put("original", originalName)
                    .put("url", relativeUrl)
                    .put("title", absoluteUrl)
                    .put("state", "SUCCESS")
                    .build()));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new JSONObject(Maps.newHashMap(ImmutableMap.builder()
                    .put("original", "")
                    .put("url", "")
                    .put("title", "")
                    .put("state", e.getMessage())
                    .build()));
        }
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
