package com.tuotiansudai.service;

import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.dto.SiteMapDataDto;

import java.util.List;
import java.util.Map;

public interface HomeService {

    List<HomeLoanDto> getNormalLoans();

    List<HomeLoanDto> getNewbieLoans();

    List<HomeLoanDto> getEnterpriseLoans();

    List<SiteMapDataDto> getSiteMapData();
}
