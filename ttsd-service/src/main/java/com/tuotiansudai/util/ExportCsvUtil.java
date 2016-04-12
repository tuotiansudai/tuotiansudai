package com.tuotiansudai.util;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class ExportCsvUtil {

    public static void createCsvOutputStream(CsvHeaderType csvHeaderType, List<List<String>> data, OutputStream outputStream) {
        BufferedWriter csvFileOutputStream = null;
        try {
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(outputStream, "GBK"));
            csvFileOutputStream.write(csvHeaderType.getHeader());
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
}
