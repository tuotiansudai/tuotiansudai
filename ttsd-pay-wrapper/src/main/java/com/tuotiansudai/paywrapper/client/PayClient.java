package com.tuotiansudai.paywrapper.client;

import com.squareup.okhttp.*;
import com.tuotiansudai.paywrapper.repository.model.BaseRequestModel;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class PayClient {

    static Logger logger = Logger.getLogger(PayClient.class);

    @Autowired
    private OkHttpClient httpClient;

    public boolean send(BaseRequestModel requestData) {
        try {
            ReqData reqData = Mer2Plat_v40.makeReqDataByPost(requestData.generatePayRequestData());

            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            Map field = reqData.getField();
            for (Object key : field.keySet()) {
                Object value = field.get(key);
                formEncodingBuilder.add(key.toString(), value.toString());
            }

            Request request = new Request.Builder().url(reqData.getUrl()).post(formEncodingBuilder.build()).build();
            Response response = httpClient.newCall(request).execute();
            String responseBodyString = response.body().string();
            logger.debug(responseBodyString);
            Map resData = Plat2Mer_v40.getResData(responseBodyString);
            return true;
        } catch (ReqDataException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (RetDataException e) {
            logger.error(e);
        }
        return false;
    }
}
