package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.ChannelPointDetailModel;
import com.tuotiansudai.point.repository.model.ChannelPointModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelPointDetailMapper {
    void create(ChannelPointDetailModel channelPointDetailModel);

    ChannelPointDetailModel findById(long id);

    List<ChannelPointDetailModel> findByPagination(@Param(value = "channelPointId") long channelPointId,
                                                   @Param(value = "channel") String channel,
                                                   @Param(value = "userNameOrMobile") String userNameOrMobile,
                                                   @Param(value = "success") Boolean success,
                                                   @Param(value = "index") int index,
                                                   @Param(value = "pageSize") int pageSize);

    long findCountByPagination(@Param(value = "channelPointId") long channelPointId,
                               @Param(value = "channel") String channel,
                               @Param(value = "userNameOrMobile") String userNameOrMobile,
                               @Param(value = "success") Boolean success);

    List<String> findAllChannel();

    List<ChannelPointDetailModel> findSuccessByChannelPointId(long channelPointId);



}
