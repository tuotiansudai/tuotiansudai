package com.tuotiansudai.paywrapper.ghb.message.response;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ResponseOGW00042Test {

    @Test
    public void name() throws Exception {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Document><header><encryptData></encryptData><channelCode>GHB</channelCode><transCode>OGW00043</transCode><channelFlow>20160409878999</channelFlow><serverFlow>OGW01201603161000008948</serverFlow><status>0</status><serverTime>175419</serverTime><errorCode>0</errorCode><errorMsg></errorMsg><serverDate>20160316</serverDate></header><body><TRANSCODE>OGW00019</TRANSCODE><MERCHANTID>XQB</MERCHANTID><BANKID>GHB</BANKID><XMLPARA>7wPjJiSOm4uabTGPdh1Bnl81CzsONRae+NLuv3Zv1V7fa25nzxEgrJbXiYSCRmBn4XFahhEUzL33aSFWxoWlXisukbo+xPh4nRO/whIS5tP4AOkKjn0UvUqhmbS73Wj+nSNT5vNz6LFxNcPoRvoN2zokfuPQTo7ZoWzQZnhn+GQ=</XMLPARA></body></Document>";

        ResponseMessageContent<ResponseOGW00042> response = new ResponseMessageContent<>(s, ResponseOGW00042.class);

        ResponseOGW00042 content = response.getBody().getContent();
        assertThat(response, is(0));
    }

    @Test
    public void shouldGenerateAsyncResponse() throws Exception {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Document><header><encryptData></encryptData><channelCode>GHB</channelCode><transCode>OGW00043</transCode><channelFlow>20160409878999</channelFlow><serverFlow>OGW01201603161000008948</serverFlow><status>0</status><serverTime>175419</serverTime><errorCode>0</errorCode><errorMsg></errorMsg><serverDate>20160316</serverDate></header><body><TRANSCODE>OGW00019</TRANSCODE><MERCHANTID>XQB</MERCHANTID><BANKID>GHB</BANKID><XMLPARA>7wPjJiSOm4uabTGPdh1Bnl81CzsONRae+NLuv3Zv1V7fa25nzxEgrJbXiYSCRmBn4XFahhEUzL33aSFWxoWlXisukbo+xPh4nRO/whIS5tP4AOkKjn0UvUqhmbS73Wj+nSNT5vNz6LFxNcPoRvoN2zokfuPQTo7ZoWzQZnhn+GQ=</XMLPARA></body></Document>";

        ResponseMessageContent<ResponseOGW00042> response = new ResponseMessageContent<>(s, ResponseOGW00042.class);

        System.out.println(response.getAsyncResponse().generateXML());
        assertThat(response, is(0));
    }
}
