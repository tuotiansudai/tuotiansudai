package com.tuotiansudai.smswrapper.controller;

import com.tuotiansudai.smswrapper.service.NeteaseCallbackService;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;


@Controller
@RequestMapping(path = "/sms")
public class NeteaseCallbackController {

    private final static Logger logger = Logger.getLogger(NeteaseCallbackController.class);

    private final NeteaseCallbackService neteaseCallbackService;

    @Autowired
    public NeteaseCallbackController(NeteaseCallbackService neteaseCallbackService) {
        this.neteaseCallbackService = neteaseCallbackService;
    }

    @RequestMapping(path = "/netease-callback", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> callback(HttpServletRequest request) {
        try {
            // 获取部分request header，并打印
            String contentType = request.getContentType();
            String appKey = request.getHeader("AppKey");
            String curTime = request.getHeader("CurTime");
            String md5 = request.getHeader("MD5");
            String checkSum = request.getHeader("CheckSum");
            String body = this.readBody(request);

            logger.info(MessageFormat.format("[netease callback] headers: ContentType={0}, AppKey={1}, CurTime={2}, MD5={3}, CheckSum={4}, body={5}",
                    contentType, appKey, curTime, md5, checkSum, body));


            this.neteaseCallbackService.updateStatus(curTime, md5, checkSum, body);

        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    private String readBody(HttpServletRequest request) throws IOException {
        if (request.getContentLength() == 0) {
            return null;
        }
        byte[] body = new byte[request.getContentLength()];
        IOUtils.readFully(request.getInputStream(), body);

        return new String(body, "utf-8");
    }
}
