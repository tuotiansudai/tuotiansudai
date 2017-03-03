package com.tuotiansudai.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExportCsvUtil {

    public static void createCsvOutputStream(CsvHeaderType csvHeaderType, List<List<String>> data, OutputStream outputStream) {
        BufferedWriter csvFileOutputStream = null;
        try {
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(outputStream, "GBK"));
            csvFileOutputStream.write(csvHeaderType.getHeader());
            csvFileOutputStream.newLine();
            for (List<String> dataList : data) {
                csvFileOutputStream.write(join(dataList, ","));
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
                e.printStackTrace();
            }
            String fieldString = "";
            if (null == fieldObject) {
                fieldString = "";
            } else if (fieldObject instanceof Date) {
                fieldString = simpleDateFormat.format((Date) fieldObject);
            } else if (fieldObject instanceof List) {
                for (Object item : (List) fieldObject) {
                    fieldString += String.valueOf(item);
                }
            } else if(fieldObject instanceof Boolean) {
                fieldString = (boolean) fieldObject ? "是" : "否";
            }
            else {
                fieldString = String.valueOf(fieldObject);
            }
            dtoStrings.add(fieldString);
        }
        return dtoStrings;
    }

    public static String join(Collection var0, String var1) {
        StringBuilder var2 = new StringBuilder();

        for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.append((String)var3.next())) {
            if(var2.length() != 0) {
                var2.append(var1);
            }
        }

        return var2.toString();
    }
}
