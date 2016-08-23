package com.tuotiansudai.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExportCsvUtil {

    static Logger logger = Logger.getLogger(ExportCsvUtil.class);

    public static void createCsvOutputStream(String header, List<List<String>> data, OutputStream outputStream) {
        BufferedWriter csvFileOutputStream = null;
        try {
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(outputStream, "GBK"));
            csvFileOutputStream.write(header);
            csvFileOutputStream.newLine();
            for (List<String> dataList : data) {
                csvFileOutputStream.write(StringUtils.join(dataList, ","));
                csvFileOutputStream.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (csvFileOutputStream != null) {
                    csvFileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> List<String> dtoToStringList(T t) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> dtoStrings = new ArrayList<>();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldObject = null;
            try {
                fieldObject = field.get(t);
            } catch (IllegalAccessException e) {
                logger.error("T:" + t.getClass().getName() + ", field:" + field.getName() + "\n" + e.getMessage());
            }
            String fieldString;
            if (null == fieldObject) {
                fieldString = "";
            } else if (fieldObject instanceof Date) {
                fieldString = simpleDateFormat.format((Date) fieldObject);
            } else {
                fieldString = String.valueOf(fieldObject);
            }
            dtoStrings.add(fieldString);
        }
        return dtoStrings;
    }
}
