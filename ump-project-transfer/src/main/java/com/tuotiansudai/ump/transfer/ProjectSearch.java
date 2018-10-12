package com.tuotiansudai.ump.transfer;

import com.google.common.collect.ImmutableMap;
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
import java.util.Map;
import java.util.Properties;

public class ProjectSearch {

    static Logger logger = Logger.getLogger(ProjectSearch.class);

    private static final Map<String, String> HUMAN_READABLE_PROJECT_STATE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("-1", "取消")
            .put("90", "初始")
            .put("91", "建标中")
            .put("92", "建标成功")
            .put("93", "建标失败")
            .put("94", "建标锁定")
            .put("0", "开标")
            .put("1", "出借中")
            .put("2", "还款中")
            .put("3", "已还款")
            .put("4", "结束")
            .build());

    private static Properties UMP_PROPS = new Properties();

    private static Map<String, String> payRequestData = Maps.newHashMap();

    private OkHttpClient okHttpClient = new OkHttpClient();

    static {
        if (UMP_PROPS.isEmpty()) {
            Resource resource = new ClassPathResource("SignVerProp.properties");
            try {
                ProjectSearch.UMP_PROPS = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }

        }
    }

    public ProjectSearch() {
        payRequestData.clear();
        payRequestData.put("service", "project_account_search");
        payRequestData.put("sign_type", UMP_PROPS.getProperty("sign_type"));
        payRequestData.put("charset", UMP_PROPS.getProperty("charset"));
        payRequestData.put("version", UMP_PROPS.getProperty("version"));
        payRequestData.put("mer_id", UMP_PROPS.getProperty("mer_id"));
    }

    @SuppressWarnings(value = "unchecked")
    public String search(String projectId) throws ReqDataException, RetDataException, IOException {
        payRequestData.put("project_id", projectId);
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

        if (!"0000".equals(resData.get("ret_code"))) {
            logger.info(MessageFormat.format("标的({0}) 查询失败({1})", projectId, resData.get("ret_msg")));
            return null;
        }

        return resData.get("balance");
    }
}
