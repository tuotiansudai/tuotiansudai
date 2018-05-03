package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LicaiquanArticleCommentMapper;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleCommentModel;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.PaginationUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.SerializeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsoleLiCaiQuanArticleService {

    private final static Logger logger = Logger.getLogger(ConsoleLiCaiQuanArticleService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String articleRedisKey = "console:article:key";
    private final static String articleCommentRedisKey = "console:article:comment";
    private final static String articleLikeCounterKey = "console:article:likeCounter";
    private final static String articleReadCounterKey = "console:article:readCounter";
    private final static String articleCheckerKey = "console:article:checker";

    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;

    @Autowired
    private LicaiquanArticleCommentMapper licaiquanArticleCommentMapper;

    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;

    public void retrace(long articleId) {
        redisWrapperClient.hdelSeri(articleRedisKey, String.valueOf(articleId));
    }

    public void createAndEditArticle(LiCaiQuanArticleDto liCaiQuanArticleDto) {
        if (liCaiQuanArticleDto.getArticleId() == null) {
            long articleId = IdGenerator.generate();
            liCaiQuanArticleDto.setArticleId(articleId);
            liCaiQuanArticleDto.setCreateTime(new Date());
        } else {
            liCaiQuanArticleDto.setUpdateTime(new Date());
        }
        liCaiQuanArticleDto.setArticleStatus(ArticleStatus.TO_APPROVE);
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()), liCaiQuanArticleDto);
    }

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
            dto.setLikeCount(liCaiQuanArticleService.getLikeCount(dto.getArticleId()));
            dto.setReadCount(liCaiQuanArticleService.getReadCount(dto.getArticleId()));
        }
        return list;
    }

    private List<LiCaiQuanArticleDto> findRedisArticleDto(String title, ArticleSectionType articleSectionType, ArticleStatus articleStatus) {
        List<LiCaiQuanArticleDto> articleDtoList = new ArrayList<>();
        Map<byte[], byte[]> articleDtoListHkey = redisWrapperClient.hgetAllSeri(articleRedisKey);
        for (byte[] key : articleDtoListHkey.keySet()) {
            LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) SerializeUtil.deserialize(articleDtoListHkey.get(key));
            if (StringUtils.isNotEmpty(title) && !liCaiQuanArticleDto.getTitle().toUpperCase().contains(title.toUpperCase())) {
                continue;
            }

            if (articleSectionType == null && articleStatus == null) {
                articleDtoList.add(liCaiQuanArticleDto);
                continue;
            }

            if (liCaiQuanArticleDto.getSection() != null && liCaiQuanArticleDto.getSection().equals(articleSectionType)) {
                articleDtoList.add(liCaiQuanArticleDto);
            }

            if (liCaiQuanArticleDto.getArticleStatus() != null && liCaiQuanArticleDto.getArticleStatus() == articleStatus) {
                articleDtoList.add(liCaiQuanArticleDto);
            }
        }
        return articleDtoList;
    }

    public ArticlePaginationDataDto findLiCaiQuanArticleDto(String title, ArticleSectionType articleSectionType,
                                                            ArticleStatus articleStatus,
                                                            int pageSize, int index) {
        int count = 0;
        List<LiCaiQuanArticleDto> list;
        List<LiCaiQuanArticleDto> articleDtoList;
        if (articleStatus != null && articleStatus.equals(ArticleStatus.PUBLISH)) {
            articleDtoList = Lists.newArrayList();
        } else {
            articleDtoList = findRedisArticleDto(title, articleSectionType, articleStatus);
        }

        if (CollectionUtils.isNotEmpty(articleDtoList)) {
            count += articleDtoList.size();
        }

        if (articleStatus == null || articleStatus.equals(ArticleStatus.PUBLISH)) {
            List<LicaiquanArticleModel> articleListItemModelList = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime(title, articleSectionType, 1, 10000);
            if (CollectionUtils.isNotEmpty(articleListItemModelList)) {
                count += articleListItemModelList.size();
                for (LicaiquanArticleModel model : articleListItemModelList) {
                    articleDtoList.add(new LiCaiQuanArticleDto(model));
                }
            }
        }

        articleDtoList.sort((o1, o2) -> {
            int compare = Longs.compare(o1.getCreateTime().getTime(), o2.getCreateTime().getTime());
            if (compare == 0) {
                return Long.compare(o1.getArticleId(), o2.getArticleId());
            }
            return compare;
        });

        int indexCount = (index - 1) * pageSize;
        int totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
        index = index > totalPages ? totalPages : index;
        int toIndex = (indexCount + pageSize) > articleDtoList.size() ? articleDtoList.size() : indexCount + pageSize;
        list = setLikeAndReadCount(updateArticleDtoTitle(articleDtoList.subList(indexCount, toIndex)));
        return new ArticlePaginationDataDto(index, pageSize, count, list);
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
            for (LiCaiQuanArticleDto database : databaseList) {
                ids.add(database.getArticleId());
            }
            for (LiCaiQuanArticleDto redis : redisList) {
                if (ids.contains(redis.getArticleId())) {
                    redis.setOriginal(true);
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

    public BaseDto<BaseDataDto> checkArticleOnStatus(long articleId, String checkerLoginName) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        String originChecker = redisWrapperClient.hget(articleCheckerKey, String.valueOf(articleId));
        if (getArticleContent(articleId).getArticleStatus().equals(ArticleStatus.APPROVING) && !StringUtils.isEmpty(originChecker) && !originChecker.equals(checkerLoginName)) {
            BaseDataDto baseDataDto = new BaseDataDto();
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("文章正在审核中!");
            baseDto.setData(baseDataDto);
        } else {
            redisWrapperClient.hset(articleCheckerKey, String.valueOf(articleId), checkerLoginName);
            changeArticleStatus(articleId, ArticleStatus.APPROVING);
            BaseDataDto baseDataDto = new BaseDataDto();
            baseDataDto.setStatus(true);
            baseDto.setData(baseDataDto);
        }
        return baseDto;
    }

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

            redisWrapperClient.hdel(articleCheckerKey, String.valueOf(articleId));

            List<LicaiquanArticleCommentModel> commentModelList = new ArrayList<>();
            Map<String, String> comment = getAllComments(model.getId());
            redisWrapperClient.hdel(articleCommentRedisKey, String.valueOf(model.getId()));
            for (String key : comment.keySet()) {
                commentModelList.add(new LicaiquanArticleCommentModel(model.getId(), comment.get(key), new DateTime(key).toDate()));
            }

            if (CollectionUtils.isNotEmpty(commentModelList)) {
                for (LicaiquanArticleCommentModel commentModel : commentModelList) {
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

    public void rejectArticle(long articleId, String comment) {
        changeArticleStatus(articleId, ArticleStatus.REFUSED);
        redisWrapperClient.hdel(articleCheckerKey, String.valueOf(articleId));
        storeComment(articleId, comment);
    }

    public void deleteArticle(long articleId) {
        this.licaiquanArticleMapper.deleteArticle(articleId);
    }

    private void changeArticleStatus(long articleId, ArticleStatus articleStatus) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey,
                String.valueOf(articleId));
        liCaiQuanArticleDto.setArticleStatus(articleStatus);
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(articleId), liCaiQuanArticleDto);
    }

    public LiCaiQuanArticleDto getArticleContentByDataBase(long articleId) {
        LicaiquanArticleModel licaiquanArticleModel = this.licaiquanArticleMapper.findArticleById(articleId);
        if (licaiquanArticleModel != null) {
            return new LiCaiQuanArticleDto(licaiquanArticleModel);
        }
        return new LiCaiQuanArticleDto();
    }

    public Map<String, String> getAllComments(long articleId) {
        Map<String, String> existedComments = new HashMap<>();
        if (redisWrapperClient.hexists(articleCommentRedisKey, String.valueOf(articleId))) {
            existedComments = (Map<String, String>) redisWrapperClient.hgetSeri(articleCommentRedisKey, String.valueOf(articleId));
        }
        return existedComments;
    }
}
