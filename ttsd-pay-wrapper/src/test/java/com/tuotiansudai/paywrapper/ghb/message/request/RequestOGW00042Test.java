package com.tuotiansudai.paywrapper.ghb.message.request;

import com.tuotiansudai.repository.model.Source;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RequestOGW00042Test {

    @Test
    public void generateXML() throws Exception {
        RequestMessageContent<RequestOGW00042> pcRequest = new RequestMessageContent<>(new RequestOGW00042(Source.WEB, "userName", "identityNumber", "mobile"));

        assertTrue(Pattern.matches("^<Document><header><channelCode>.*</channelCode><channelFlow>.*</channelFlow><channelDate>.*</channelDate><channelTime>.*</channelTime><encryptData/></header><body><TRANSCODE>OGW00042</TRANSCODE><XMLPARA><MERCHANTID>merchantid</MERCHANTID><APPID>PC</APPID><TTRANS>6</TTRANS><MERCHANTNAME>拓天速贷</MERCHANTNAME><ACNAME>userName</ACNAME><IDTYPE>1010</IDTYPE><IDNO>identityNumber</IDNO><MOBILE>mobile</MOBILE><EMAIL></EMAIL><RETURNURL></RETURNURL><CUSTMNGRNO></CUSTMNGRNO></XMLPARA></body></Document>$"
                , pcRequest.getPlainXML()));

        assertThat(pcRequest.getPlainXMLPARA(), is("<MERCHANTID>merchantid</MERCHANTID><APPID>PC</APPID><TTRANS>6</TTRANS><MERCHANTNAME>拓天速贷</MERCHANTNAME><ACNAME>userName</ACNAME><IDTYPE>1010</IDTYPE><IDNO>identityNumber</IDNO><MOBILE>mobile</MOBILE><EMAIL></EMAIL><RETURNURL></RETURNURL><CUSTMNGRNO></CUSTMNGRNO>"));

        RequestMessageContent<RequestOGW00042> appRequest = new RequestMessageContent<>(new RequestOGW00042(Source.MOBILE, "userName", "identityNumber", "mobile"));
        assertTrue(Pattern.matches("^<Document><header><channelCode>.*</channelCode><channelFlow>.*</channelFlow><channelDate>.*</channelDate><channelTime>.*</channelTime><encryptData/></header><body><TRANSCODE>OGW00090</TRANSCODE><XMLPARA><MERCHANTID>merchantid</MERCHANTID><APPID>APP</APPID><TTRANS>6</TTRANS><MERCHANTNAME>拓天速贷</MERCHANTNAME><ACNAME>userName</ACNAME><IDTYPE>1010</IDTYPE><IDNO>identityNumber</IDNO><MOBILE>mobile</MOBILE><EMAIL></EMAIL><RETURNURL></RETURNURL><CUSTMNGRNO></CUSTMNGRNO></XMLPARA></body></Document>$"
                , appRequest.getPlainXML()));

        assertThat(appRequest.getPlainXMLPARA(), is("<MERCHANTID>merchantid</MERCHANTID><APPID>APP</APPID><TTRANS>6</TTRANS><MERCHANTNAME>拓天速贷</MERCHANTNAME><ACNAME>userName</ACNAME><IDTYPE>1010</IDTYPE><IDNO>identityNumber</IDNO><MOBILE>mobile</MOBILE><EMAIL></EMAIL><RETURNURL></RETURNURL><CUSTMNGRNO></CUSTMNGRNO>"));
    }
}
