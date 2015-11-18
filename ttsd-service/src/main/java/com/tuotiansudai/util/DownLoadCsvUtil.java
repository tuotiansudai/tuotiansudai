package com.tuotiansudai.util;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class DownLoadCsvUtil {

    public static BufferedWriter createCsvOutputStream(CsvHeaderType csvHeaderType, List<List<String>> data, OutputStream outputStream) {
        BufferedWriter csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(outputStream));
        try {
            csvFileOutputStream.write(csvHeaderType.getHeader());
            csvFileOutputStream.newLine();
            for (List<String> dataList : data) {
                csvFileOutputStream.write(StringUtils.join(dataList,","));
                csvFileOutputStream.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFileOutputStream;
    }

}
