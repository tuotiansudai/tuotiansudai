package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.FinanceReportDto;
import com.tuotiansudai.repository.model.PreferenceType;

import java.util.Date;
import java.util.List;

public interface FinanceReportService {
    BasePaginationDataDto<FinanceReportDto> getFinanceReportDtos(Long loanId, Integer period, String investLoginName,
                                                                 Date investStartTime, Date investEndTime,
                                                                 PreferenceType preferenceType, int index, int pageSize);

    List<List<String>> getFinanceReportCsvData(Long loanId, Integer period, String investLoginName, Date investStartTime,
                                               Date investEndTime, PreferenceType preferenceType);

}
