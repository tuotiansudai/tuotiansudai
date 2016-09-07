package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.console.activity.dto.AutumnExportDto;

import java.util.List;


public interface ExportService {

    List<AutumnExportDto> getAutumnExport();

    List<List<String>> buildAutumnList(List<AutumnExportDto> records);

}
