package com.tuotiansudai.service.impl;

import com.google.common.primitives.Longs;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleListItemModel;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import com.tuotiansudai.util.IdGenerator;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LiCaiQuanArticleServiceImpl implements LiCaiQuanArticleService {
    static Logger logger = Logger.getLogger(LiCaiQuanArticleServiceImpl.class);

    private final static String articleRedisKey = "console:article:key";

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public BaseDto<PayDataDto> retrace(long articleId) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setData(payDataDto);
        if (!redisWrapperClient.hexistsSeri(articleRedisKey, String.valueOf(articleId))) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(MessageFormat.format("id:{0}不存在,请核实!", articleId));
            return baseDto;
        }
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
        if (liCaiQuanArticleDto != null && liCaiQuanArticleDto.getArticleStatus() == ArticleStatus.APPROVING) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(MessageFormat.format("id:{0}正在审核!", articleId));
            return baseDto;
        }
        liCaiQuanArticleDto.setArticleStatus(ArticleStatus.RETRACED);
        liCaiQuanArticleDto.setCreateTime(new Date());
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(articleId), liCaiQuanArticleDto);
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
    public List<LiCaiQuanArticleDto> findLiCaiQuanArticleDto(String title, ArticleSectionType articleSectionType,long startId, int size){
        List<LiCaiQuanArticleDto> liCaiQuanArticleDtoList = new ArrayList<>();
        List<LicaiquanArticleListItemModel> licaiquanArticleListItemModelList = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime(title,articleSectionType,startId,size);
        Map<byte[],byte[]> liCaiQuanArticleDtoListHkey = redisWrapperClient.hgetAllSeri(articleRedisKey);
        for (byte[] key : liCaiQuanArticleDtoListHkey.keySet()) {
            String linkListHValue = "";
            LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto();
            try {
                linkListHValue = new String(liCaiQuanArticleDtoListHkey.get(key),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String[] values = linkListHValue.split("\\|");
            liCaiQuanArticleDto.setId(Long.parseLong(values[0]));
            liCaiQuanArticleDto.setAuthor(values[1]);
            liCaiQuanArticleDto.setThumbPicture(values[2]);
            liCaiQuanArticleDto.setShowPicture(values[3]);
            liCaiQuanArticleDto.setCarousel(Boolean.parseBoolean(values[4]));
            liCaiQuanArticleDto.setSection(ArticleSectionType.valueOf(values[5]));
            liCaiQuanArticleDto.setContent(values[6]);
            liCaiQuanArticleDto.setCreateTime(strToDate(values[7]));
            liCaiQuanArticleDto.setArticleStatus(ArticleStatus.valueOf(values[8]));
            liCaiQuanArticleDtoList.add(liCaiQuanArticleDto);
        }

        Collections.sort(liCaiQuanArticleDtoList, new Comparator<LiCaiQuanArticleDto>(){
            @Override
            public int compare(LiCaiQuanArticleDto o1, LiCaiQuanArticleDto o2) {
                return o2.getCreateTime().after(o1.getCreateTime()) ? 1 : 0;
            }
        });

        return liCaiQuanArticleDtoList;
    }

    private Date strToDate(String strDate){
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            logger.debug("The date conversion errors");
        }
        return date;
    }
}
