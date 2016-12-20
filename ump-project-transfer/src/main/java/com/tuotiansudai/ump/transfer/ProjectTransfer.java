package com.tuotiansudai.ump.transfer;

import com.google.common.collect.Maps;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class ProjectTransfer {

    static Logger logger = Logger.getLogger(ProjectTransfer.class);

    private static Properties UMP_PROPS = new Properties();

    private static Map<String, String> payRequestData = Maps.newHashMap();

    private OkHttpClient okHttpClient = new OkHttpClient();

    static {
        if (UMP_PROPS.isEmpty()) {
            Resource resource = new ClassPathResource("SignVerProp.properties");
            try {
                ProjectTransfer.UMP_PROPS = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }

        }
    }

    public ProjectTransfer() {
        payRequestData.clear();
        payRequestData.put("service", "project_transfer");
        payRequestData.put("sign_type", UMP_PROPS.getProperty("sign_type"));
        payRequestData.put("charset", UMP_PROPS.getProperty("charset"));
        payRequestData.put("version", UMP_PROPS.getProperty("version"));
        payRequestData.put("mer_id", UMP_PROPS.getProperty("mer_id"));
        payRequestData.put("partic_user_id", UMP_PROPS.getProperty("mer_id"));
        payRequestData.put("partic_acc_type", "02");
        payRequestData.put("ret_url", "www.abc.com");
        payRequestData.put("notif_url", "www.abc.com");
        payRequestData.put("serv_type", "52");
        payRequestData.put("trans_action", "02");
        payRequestData.put("partic_type", "03");
        payRequestData.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
    }

    @SuppressWarnings(value = "unchecked")
    public void transfer(String projectId, String amount) throws ReqDataException, RetDataException, IOException {
        payRequestData.put("project_id", projectId);
        payRequestData.put("order_id", String.valueOf(new Date().getTime()));
        payRequestData.put("amount", amount);

        ReqData reqData = Mer2Plat_v40.makeReqDataByPost(payRequestData);

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        Map<String, String> field = (Map<String, String>) reqData.getField();
        for (String key : field.keySet()) {
            String value = field.get(key);
            formEncodingBuilder.add(key, value);
        }

        Request request = new Request.Builder().url(reqData.getUrl()).post(formEncodingBuilder.build()).build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBodyString = response.body().string();
        Map<String, String> resData = Plat2Mer_v40.getResData(responseBodyString);

        logger.info(MessageFormat.format("标的({0}) ({1})", projectId, resData.get("ret_msg")));
    }
}
