package com.tuotiansudai.service;


import com.tuotiansudai.repository.model.LicaiquanArticleModel;

import java.util.List;

public interface LicaiquanArticleService {
    List<LicaiquanArticleModel> findLicaiquanArticleModel(String title,String section,Integer index);
}
