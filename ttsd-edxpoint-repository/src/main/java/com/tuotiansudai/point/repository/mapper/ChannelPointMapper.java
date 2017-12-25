package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.ChannelPointModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelPointMapper {
    void create(ChannelPointModel channelPointModel);

    ChannelPointModel findById(long id);

    List<ChannelPointModel> findByPagination(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    long findCountByPagination();

    List<ChannelPointModel> findAll();

    default long findSumTotalCount() {
        List<ChannelPointModel> models = findAll();
        return models.stream().mapToLong(model -> model.getTotalPoint()).sum();
    }

    default long findSumHeadCount() {
        List<ChannelPointModel> models = findAll();
        return models.stream().mapToLong(model -> model.getHeadCount()).sum();
    }


}
