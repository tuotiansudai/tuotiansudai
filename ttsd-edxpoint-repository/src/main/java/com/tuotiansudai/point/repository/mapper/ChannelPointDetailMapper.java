package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.ChannelPointDetailModel;
import com.tuotiansudai.point.repository.model.ChannelPointModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelPointDetailMapper {
    void create(ChannelPointDetailModel channelPointDetailModel);

    ChannelPointDetailModel findById(long id);

}
