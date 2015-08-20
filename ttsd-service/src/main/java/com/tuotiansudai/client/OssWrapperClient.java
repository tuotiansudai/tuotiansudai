package com.tuotiansudai.client;

import com.tuotiansudai.utils.Uploader;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2015/8/20.
 */
@Component
public class OssWrapperClient extends HttpServlet{

    static Logger logger = Logger.getLogger(OssWrapperClient.class);

    public OssWrapperClient() {
        super();
    }

    public void init() throws ServletException {
        super.init();
    }

    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Uploader up = new Uploader(request);
        up.setSavePath("upload");
        String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
        up.setAllowFiles(fileType);
        up.setMaxSize(10000); //单位KB
        try {
            up.upload();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("出错了！");
        }
        response.getWriter().print("{'original':'"+up.getOriginalName()+"','url':'"+up.getUrl()+"','title':'"+up.getTitle()+"','state':'"+up.getState()+"'}");
    }

}
