package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.LicaiquanArticleListItemModel;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.service.LicaiquanArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicaiquanArticleServiceImpl implements LicaiquanArticleService {
    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;

    @Override
    public List<LicaiquanArticleModel> findLicaiquanArticleModel(String title, String section, Integer index) {
        List<LicaiquanArticleListItemModel>  licaiquanArticleListItemModelList = licaiquanArticleMapper.findArticleListByTitleAndSection(title,section,index);
        return null;
    }
}
