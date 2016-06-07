package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LicaiquanArticleCommentMapper;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleCommentModel;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.SerializeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LiCaiQuanArticleServiceImpl implements LiCaiQuanArticleService {
    static Logger logger = Logger.getLogger(LiCaiQuanArticleServiceImpl.class);

    private final static String articleRedisKey = "console:article:key";
    private final static String articleCommentRedisKey = "console:article:comment";
    private final static String articleLikeCounterKey = "console:article:likeCounter";
    private final static String articleReadCounterKey = "console:article:readCounter";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;
    @Autowired
    private LicaiquanArticleCommentMapper licaiquanArticleCommentMapper;

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
    public void createAndEditArticle(LiCaiQuanArticleDto liCaiQuanArticleDto) {
        if (liCaiQuanArticleDto.getArticleId() == null) {
            long articleId = idGenerator.generate();
            liCaiQuanArticleDto.setArticleId(articleId);
            liCaiQuanArticleDto.setCreateTime(new Date());
        } else {
            LicaiquanArticleModel licaiquanArticleModel = licaiquanArticleMapper.findArticleById(liCaiQuanArticleDto.getArticleId());
            if(licaiquanArticleModel != null){
                liCaiQuanArticleDto.setCreateTime(new Date());
            }
            liCaiQuanArticleDto.setUpdateTime(new Date());
        }
        liCaiQuanArticleDto.setArticleStatus(ArticleStatus.TO_APPROVE);
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()), liCaiQuanArticleDto);
    }

    @Override
    public LiCaiQuanArticleDto obtainEditArticleDto(long articleId) {
        if (redisWrapperClient.hexistsSeri(articleRedisKey, String.valueOf(articleId))) {
            return (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
        } else {
            LicaiquanArticleModel licaiquanArticleModel = licaiquanArticleMapper.findArticleById(articleId);
            if (licaiquanArticleModel != null) {
                return new LiCaiQuanArticleDto(licaiquanArticleModel);
            }
        }

        return null;
    }

    private List<LiCaiQuanArticleDto> setLikeAndReadCount(List<LiCaiQuanArticleDto> list) {
        for (LiCaiQuanArticleDto dto : list) {
            dto.setLikeCount(getLikeCount(dto.getArticleId()));
            dto.setReadCount(getReadCount(dto.getArticleId()));
        }
        return list;
    }

    private List<LiCaiQuanArticleDto> findRedisArticleDto(String title, ArticleSectionType articleSectionType) {
        List<LiCaiQuanArticleDto> articleDtoList = new ArrayList<>();
        Map<byte[], byte[]> articleDtoListHkey = redisWrapperClient.hgetAllSeri(articleRedisKey);
        for (byte[] key : articleDtoListHkey.keySet()) {
            LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) SerializeUtil.deserialize(articleDtoListHkey.get(key));
            if (StringUtils.isNotEmpty(title) && liCaiQuanArticleDto.getTitle().indexOf(title) == -1) {
                continue;
            }
            if (articleSectionType != null && !liCaiQuanArticleDto.getSection().equals(articleSectionType)) {
                continue;
            }
            articleDtoList.add(liCaiQuanArticleDto);
        }
        return articleDtoList;
    }

    @Override
    public ArticlePaginationDataDto findLiCaiQuanArticleDto(String title, ArticleSectionType articleSectionType,
                                                            int pageSize, int index) {
        int count = 0;
        List<LiCaiQuanArticleDto> list;
        List<LiCaiQuanArticleDto> articleDtoList = findRedisArticleDto(title, articleSectionType);
        if (CollectionUtils.isNotEmpty(articleDtoList)) {
            count += articleDtoList.size();
        }

        List<LicaiquanArticleModel> articleListItemModelList = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime(title, articleSectionType, 1, 10000);
        if (CollectionUtils.isNotEmpty(articleListItemModelList)) {
            count += articleListItemModelList.size();
            for (LicaiquanArticleModel model : articleListItemModelList) {
                articleDtoList.add(new LiCaiQuanArticleDto(model));
            }
        }
        Collections.sort(articleDtoList, new Comparator<LiCaiQuanArticleDto>() {
            @Override
            public int compare(LiCaiQuanArticleDto o1, LiCaiQuanArticleDto o2) {
                return o2.getCreateTime().after(o1.getCreateTime()) ? 1 : -1;
            }
        });

        int indexCount = (index - 1) * pageSize;
        int totalPages = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
        index = index > totalPages ? totalPages : index;
        int toIndex = (indexCount + pageSize) > articleDtoList.size() ? articleDtoList.size() : indexCount + pageSize;
        list = setLikeAndReadCount(updateArticleDtoTitle(articleDtoList.subList(indexCount, toIndex)));
        ArticlePaginationDataDto dto = new ArticlePaginationDataDto(index, pageSize, count, list);
        return dto;
    }


    private List<LiCaiQuanArticleDto> updateArticleDtoTitle(List<LiCaiQuanArticleDto> list) {
        List<LiCaiQuanArticleDto> redisList = new ArrayList<>();
        List<LiCaiQuanArticleDto> databaseList = new ArrayList<>();
        for (LiCaiQuanArticleDto dto : list) {
            if (dto.getArticleStatus().equals(ArticleStatus.PUBLISH)) {
                databaseList.add(dto);
            } else {
                redisList.add(dto);
            }
        }

        if (CollectionUtils.isNotEmpty(redisList) && CollectionUtils.isNotEmpty(databaseList)) {
            Set<Long> ids = new HashSet<>();
            for (LiCaiQuanArticleDto redis : redisList) {
                ids.add(redis.getArticleId());
            }
            for (LiCaiQuanArticleDto database : databaseList) {
               if(ids.contains(database.getArticleId())){
                   database.setOriginal(true);
               }
            }


        }
        return list;
    }

    public LiCaiQuanArticleDto getArticleContent(long articleId) {
        if (redisWrapperClient.hexists(articleRedisKey, String.valueOf(articleId))) {
            return (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String> getAllComments(long articleId) {
        Map<String, String> existedComments = new HashMap<>();
        if (redisWrapperClient.hexists(articleCommentRedisKey, String.valueOf(articleId))) {
            existedComments = (Map<String, String>) redisWrapperClient.hgetSeri(articleCommentRedisKey, String.valueOf(articleId));
        }
        return existedComments;
    }

    @Override
    public BaseDto<BaseDataDto> checkArticleOnStatus(long articleId) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        if (getArticleContent(articleId).getArticleStatus().equals(ArticleStatus.APPROVING)) {
            BaseDataDto baseDataDto = new BaseDataDto();
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("文章正在审核中!");
            baseDto.setData(baseDataDto);
        } else {
            changeArticleStatus(articleId, ArticleStatus.APPROVING);
            BaseDataDto baseDataDto = new BaseDataDto();
            baseDataDto.setStatus(true);
            baseDto.setData(baseDataDto);
        }
        return baseDto;
    }


    @Override
    public long getLikeCount(long articleId) {
        String likeCountString = redisWrapperClient.hget(articleLikeCounterKey, String.valueOf(articleId));
        if (null == likeCountString) {
            return 0L;
        }
        return Long.valueOf(likeCountString);
    }

    @Override
    public long getReadCount(long articleId) {
        String readCountString = redisWrapperClient.hget(articleReadCounterKey, String.valueOf(articleId));
        if (null == readCountString) {
            return 0L;
        }
        return Long.valueOf(readCountString);
    }

    @Override
    public void updateLikeCount(long articleId) {
        redisWrapperClient.hincrby(articleLikeCounterKey, String.valueOf(articleId), 1);
    }

    @Override
    public void updateReadCount(long articleId) {
        redisWrapperClient.hincrby(articleReadCounterKey, String.valueOf(articleId), 1);
    }

    @Override
    public void checkPassAndCreateArticle(long articleId, String checkName) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(articleId));
        if (liCaiQuanArticleDto != null) {
            redisWrapperClient.hdel(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
            liCaiQuanArticleDto.setChecker(checkName);
            liCaiQuanArticleDto.setUpdateTime(new Date());
            LicaiquanArticleModel model = new LicaiquanArticleModel(liCaiQuanArticleDto);
            if (this.licaiquanArticleMapper.findArticleById(model.getId()) != null) {
                this.licaiquanArticleMapper.updateArticle(model);
            } else {
                this.licaiquanArticleMapper.createArticle(model);
            }

            List<LicaiquanArticleCommentModel> commentModelList = new ArrayList<>();
            Map<String, String> comment = getAllComments(model.getId());
            redisWrapperClient.hdel(articleCommentRedisKey, String.valueOf(model.getId()));
            for(String key: comment.keySet()){
                commentModelList.add(new LicaiquanArticleCommentModel(model.getId(),comment.get(key),new Date(key)));
            }

            if(CollectionUtils.isNotEmpty(commentModelList)){
                for(LicaiquanArticleCommentModel commentModel : commentModelList){
                    licaiquanArticleCommentMapper.createComment(commentModel);
                }
            }
        }
    }

    private void storeComment(long articleId, String comment) {
        String timeStamp = new Date().toString();
        Map<String, String> existedComments = getAllComments(articleId);
        existedComments.put(timeStamp, comment);
        redisWrapperClient.hsetSeri(articleCommentRedisKey, String.valueOf(articleId), existedComments);
    }

    @Override
    public void rejectArticle(long articleId, String comment) {
        changeArticleStatus(articleId, ArticleStatus.TO_APPROVE);
        storeComment(articleId, comment);
    }

    @Override
    public void deleteArticle(long articleId) {
        this.licaiquanArticleMapper.deleteArticle(articleId);
    }

    private void changeArticleStatus(long articleId, ArticleStatus articleStatus) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey,
                String.valueOf(articleId));
        liCaiQuanArticleDto.setArticleStatus(articleStatus);
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(articleId), liCaiQuanArticleDto);
    }

    @Override
    public LiCaiQuanArticleDto getArticleContentByDataBase(long articleId){
        LicaiquanArticleModel licaiquanArticleModel = this.licaiquanArticleMapper.findArticleById(articleId);
        if(licaiquanArticleModel != null){
            return new LiCaiQuanArticleDto(licaiquanArticleModel);
        }
        return new LiCaiQuanArticleDto();
    }
}
