package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.dto.ArticleStatus;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class LiCaiQuanArticleServiceImpl implements LiCaiQuanArticleService{
    static Logger logger = Logger.getLogger(LiCaiQuanArticleServiceImpl.class);

    private final static String articleRedisKey = "console:article:key";

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;

    @Override
    public BaseDto<PayDataDto> retrace(long articleId) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setData(payDataDto);
        if(!redisWrapperClient.hexistsSeri(articleRedisKey,String.valueOf(articleId))){
            payDataDto.setStatus(false);
            payDataDto.setMessage(MessageFormat.format("id:{0}不存在,请核实!",articleId));
            return baseDto;
        }
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto)redisWrapperClient.hgetSeri(articleRedisKey,String.valueOf(articleId));
        if(liCaiQuanArticleDto != null && liCaiQuanArticleDto.getArticleStatus() == ArticleStatus.APPROVING){
            payDataDto.setStatus(false);
            payDataDto.setMessage(MessageFormat.format("id:{0}正在审核!",articleId));
            return baseDto;
        }
        liCaiQuanArticleDto.setArticleStatus(ArticleStatus.RETRACED);
        liCaiQuanArticleDto.setCreateTime(new Date());
        redisWrapperClient.hsetSeri(articleRedisKey,String.valueOf(articleId), liCaiQuanArticleDto);
        return baseDto;
    }

    @Override
    public void createArticle(LiCaiQuanArticleDto liCaiQuanArticleDto) {
        long articleId = idGenerator.generate();
        liCaiQuanArticleDto.setId(articleId);
        liCaiQuanArticleDto.setArticleStatus(ArticleStatus.TO_APPROVE);
        liCaiQuanArticleDto.setCreateTime(new Date());
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(articleId), liCaiQuanArticleDto);
    }

    @Override
    public List<LicaiquanArticleModel> findLicaiquanArticleModel(String title, String section){
        List<LicaiquanArticleModel> articleModelList = null;
//        licaiquanArticleMapper.findExistedArticleListOrderByCreateTime();

        return articleModelList;
    }
}
