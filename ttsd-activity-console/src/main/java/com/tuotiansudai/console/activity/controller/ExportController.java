package com.tuotiansudai.console.activity.controller;


import com.tuotiansudai.console.activity.dto.AutumnExportDto;
import com.tuotiansudai.console.activity.service.ExportService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @RequestMapping(value = "/autumn-list", method = RequestMethod.GET)
    public void autumnActivityListExport(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.AutumnActivityList.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");

        List<AutumnExportDto> autumnExportDtos = exportService.getAutumnExport();

        List<List<String>> csvData = exportService.buildAutumnList(autumnExportDtos);

        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.AutumnActivityList, csvData, response.getOutputStream());
    }


}
