package com.tuotiansudai.service;

import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.dto.SiteMapDataDto;

import java.util.List;
import java.util.Map;

public interface HomeService {

    List<HomeLoanDto> getNormalLoans();

    HomeLoanDto getNewbieLoan();

    List<HomeLoanDto> getEnterpriseLoans();

    List<SiteMapDataDto> siteMapData();

    Map<String, String> siteMapIndex();

    List<String> subSiteMap(String subSiteMapType);

    String getLastModifyDate(String lastModifyDateKey);


}
