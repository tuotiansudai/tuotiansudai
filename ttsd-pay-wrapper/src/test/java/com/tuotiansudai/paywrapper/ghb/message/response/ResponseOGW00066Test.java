package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tuotiansudai.paywrapper.ghb.security.provider.XML;
import com.tuotiansudai.paywrapper.ghb.service.GHBMessageRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ResponseOGW00066Test {

    @Autowired
    private GHBMessageRecordService ghbMessageRecordService;

    @Test
    public void shouldDeserializerResponse() throws Exception {
        String message = "<RETURN_STATUS>S</RETURN_STATUS><ERRORMSG>失败原因</ERRORMSG><OLDREQSEQNO>原放款交易流水号</OLDREQSEQNO><LOANNO>借款编号</LOANNO><BWACNAME>借款人姓名</BWACNAME><BWACNO>借款人账号</BWACNO><ACMNGAMT>账户管理费</ACMNGAMT><GUARANTAMT>风险保证金</GUARANTAMT><RSLIST><REQSEQNO>投标交易流水号1</REQSEQNO><LOANNO>借款编号1</LOANNO><ACNO>投资人账号1</ACNO><ACNAME>投资人账号户名1</ACNAME><AMOUNT>投标金额1</AMOUNT><STATUS>状态1</STATUS><ERRMSG>错误原因1</ERRMSG><HOSTDT>银行处理日期1</HOSTDT><HOSTJNLNO>银行止付流水号1</HOSTJNLNO></RSLIST><RSLIST><REQSEQNO>投标交易流水号2</REQSEQNO><LOANNO>借款编号2</LOANNO><ACNO>投资人账号2</ACNO><ACNAME>投资人账号户名2</ACNAME><AMOUNT>投标金额2</AMOUNT><STATUS>状态2</STATUS><ERRMSG>错误原因2</ERRMSG><HOSTDT>银行处理日期2</HOSTDT><HOSTJNLNO>银行止付流水号2</HOSTJNLNO></RSLIST>";

        ResponseOGW00066 responseOGW00066 = XML.deserializer(MessageFormat.format("<XMLPARA>{0}</XMLPARA>", message), ResponseOGW00066.class);

        assertThat(responseOGW00066.getRslist().size(), is(2));
    }

    @Test
    public void shouldSave() throws Exception {
        String message = "<Document><header><encryptData></encryptData><serverFlow>OGW01201606021000681498</serverFlow><channelCode>GHB</channelCode><status>0</status><serverTime>102450</serverTime><errorCode>0</errorCode><errorMsg>success</errorMsg><channelFlow>P2P00120160602066602Uu3rbc</channelFlow><serverDate>20160602</serverDate><transCode>OGW00066</transCode></header><body><TRANSCODE>OGW00066</TRANSCODE><BANKID>GHB</BANKID><XMLPARA><LOANNO>BH20160601000009</LOANNO><EXT_FILED2>2</EXT_FILED2><RETURN_STATUS>S</RETURN_STATUS><OLDREQSEQNO>P2P00120160602066602Uu3rbc</OLDREQSEQNO><ERRORMSG></ERRORMSG><BWACNAME>海棠</BWACNAME><GUARANTAMT>15.00</GUARANTAMT><ACMNGAMT>21.00</ACMNGAMT><BWACNO>6236882280000413620</BWACNO><RSLIST><HOSTDT>20160601</HOSTDT><ACNAME>海乐</ACNAME><ACNO>6236882280000413620</ACNO><LOANNO>BH20160601000009</LOANNO><REQSEQNO>P2P0012P16060305600006</REQSEQNO><EXT_FILED3></EXT_FILED3><ERRMSG></ERRMSG><HOSTJNLNO></HOSTJNLNO><STATUS>S</STATUS><AMOUNT>1000</AMOUNT></RSLIST><RSLIST><HOSTDT>20160601</HOSTDT><ACNAME>可乐</ACNAME><ACNO>6236882280000413623</ACNO><LOANNO>BH20160601000009</LOANNO><REQSEQNO>P2P0012P1606030520000609</REQSEQNO><EXT_FILED3></EXT_FILED3><ERRMSG></ERRMSG><HOSTJNLNO></HOSTJNLNO><STATUS>S</STATUS><AMOUNT>1000</AMOUNT></RSLIST><EXT_FILED1>1</EXT_FILED1></XMLPARA><MERCHANTID>XJP</MERCHANTID></body></Document>";

        ResponseMessageContent<ResponseOGW00066> responseMessageContent = new ResponseMessageContent<>(message, ResponseOGW00066.class);

        ghbMessageRecordService.saveResponseMessage(responseMessageContent);
    }
}
