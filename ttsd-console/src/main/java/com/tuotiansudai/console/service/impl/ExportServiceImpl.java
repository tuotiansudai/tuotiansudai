package com.tuotiansudai.console.service.impl;

import com.tuotiansudai.console.service.ExportService;
import com.tuotiansudai.util.ExportCsvUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ExportServiceImpl implements ExportService{

    @Override
    public <T> List<List<String>> buildOriginListToCsvData(List<T> originList) {
        List<List<String>> csvData = new ArrayList<>();
        for (T item : originList){
            List<String> dtoStrings = ExportCsvUtil.dtoToStringList(item);
            csvData.add(dtoStrings);
        }
        return csvData;
    }
}
