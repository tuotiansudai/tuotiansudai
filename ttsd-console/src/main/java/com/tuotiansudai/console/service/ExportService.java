package com.tuotiansudai.console.service;

import java.util.List;

public interface ExportService {

   <T> List<List<String>> buildOriginListToCsvData(List<T> originList);
}
