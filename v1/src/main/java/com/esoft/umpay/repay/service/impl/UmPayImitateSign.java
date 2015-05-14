package com.esoft.umpay.repay.service.impl;

import com.esoft.core.annotations.Logger;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.util.HttpMerParserUtil;
import com.umpay.api.util.PlainUtil;
import com.umpay.api.util.StringUtil;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/12.
 */
@Service("umPayImitateSignService")
public class UmPayImitateSign {

    @Logger
    Log log;

    public Map getResData(String html) throws RetDataException {
        new HashMap();
        try {
            Map data = getData(html);
            return data;
        } catch (Exception var3) {
            throw new RetDataException("解析后台平台响应数据出错", var3);
        }
    }

    private Map getData(String html) throws VerifyException {
        if(StringUtil.isEmpty(html)) {
            throw new RuntimeException("请传入需解析的HTML");
        } else {
            String content = HttpMerParserUtil.getMeta(html);
            return getDataByContent(content);
        }
    }

    private Map getDataByContent(String content) throws VerifyException {
        Object map = new HashMap();
        try {
            map = PlainUtil.getResPlain(content);
        } catch (Exception var5) {
            log.info("请求数据分解发生异常" + var5);
        }
        log.info("验签正确");
        return (Map)map;
    }

}
