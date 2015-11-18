package com.tuotiansudai.util;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DownLoadCsvUtil {

    public static BufferedWriter createCsvOutputStream(Map<String,String> headerMap, List<List<String>> data) {
        BufferedWriter csvFileOutputStream = null;
        try {
            for (Iterator propertyIterator = headerMap.entrySet().iterator(); propertyIterator.hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream.write((String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            for (List<String> dataList : data) {
                for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
                    csvFileOutputStream.write((String)iterator.next() != null ? (String)iterator.next() : "");
                    if (iterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
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
