package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.SerializeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

    private static Map<String,Long> paramMap = new HashMap<>();


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
    public ArticlePaginationDataDto findLiCaiQuanArticleDto(String title, ArticleSectionType articleSectionType,
                                                            int pageSize,
                                                            int index){
        int count = 0;
        List<LiCaiQuanArticleDto> list;
        List<LiCaiQuanArticleDto> articleDtoList = findRedisArticleDto(title,articleSectionType);
        if(CollectionUtils.isNotEmpty(articleDtoList)) count += articleDtoList.size();

        List<LicaiquanArticleModel> articleListItemModelList = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime(title,articleSectionType,1,10000);
        if(CollectionUtils.isNotEmpty(articleListItemModelList)){
            count += articleListItemModelList.size();
            for(LicaiquanArticleModel model : articleListItemModelList){
                articleDtoList.add(new LiCaiQuanArticleDto(model,ArticleStatus.PUBLISH));
            }
        }
        Collections.sort(articleDtoList, new Comparator<LiCaiQuanArticleDto>(){
            @Override
            public int compare(LiCaiQuanArticleDto o1, LiCaiQuanArticleDto o2) {
                return o2.getCreateTime().after(o1.getCreateTime()) ? 1 : -1;
            }
        });

        int indexCount = (index - 1) * pageSize;
        int totalPages = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
        index = index > totalPages ? totalPages : index;
        int toIndex = (indexCount + pageSize) > articleDtoList.size()?articleDtoList.size():indexCount + pageSize;
        list = updateArticleDtoTitle(articleDtoList.subList(indexCount,toIndex));
        ArticlePaginationDataDto dto = new ArticlePaginationDataDto(index,pageSize,count,list);
        return dto;
    }

    private List<LiCaiQuanArticleDto> findRedisArticleDto(String title, ArticleSectionType articleSectionType){
        List<LiCaiQuanArticleDto> articleDtoList = new ArrayList<>();
        Map<byte[],byte[]> articleDtoListHkey = redisWrapperClient.hgetAllSeri(articleRedisKey);
        for (byte[] key : articleDtoListHkey.keySet()) {
            LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto)SerializeUtil.deserialize(articleDtoListHkey.get(key));
            if(StringUtils.isNotEmpty(title) && liCaiQuanArticleDto.getTitle().indexOf(title) == -1){
                continue;
            }
            if(articleSectionType != null && !liCaiQuanArticleDto.getSection().equals(articleSectionType)){
                continue;
            }
            articleDtoList.add(liCaiQuanArticleDto);
        }
        return articleDtoList;
    }

    private List<LiCaiQuanArticleDto> updateArticleDtoTitle(List<LiCaiQuanArticleDto> list){
        long redisId = 0l;
        long databaseId = 0l;
        List<LiCaiQuanArticleDto> redisList = new ArrayList<>();
        List<LiCaiQuanArticleDto> databaseList = new ArrayList<>();
        for(LiCaiQuanArticleDto dto : list){
            if(dto.getArticleStatus().equals(ArticleStatus.PUBLISH)){
                if(databaseId < dto.getId()) databaseId = dto.getId();
                databaseList.add(dto);
            }else{
                if(redisId < dto.getId()) redisId = dto.getId();
                redisList.add(dto);
            }
        }

        if(CollectionUtils.isNotEmpty(redisList) && CollectionUtils.isNotEmpty(databaseList)){
            for(LiCaiQuanArticleDto database : databaseList){
                for(LiCaiQuanArticleDto redis : redisList){
                    if(redis.getTitle().equals(database.getTitle())){
                        database.setTitle(database.getTitle() + "(原文)");
                        break;
                    }
                }
            }
        }
        return list;
    }

}
