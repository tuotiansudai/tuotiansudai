package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
import com.tuotiansudai.activity.repository.model.WeChatHelpType;
import com.tuotiansudai.activity.repository.model.WeChatUserInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeChatHelpMapper {

    void create(WeChatHelpModel weChatHelpModel);

    void update(WeChatHelpModel weChatHelpModel);

    WeChatHelpModel findById(@Param(value = "id") long id);

    WeChatHelpModel lockById(@Param(value = "id") long id);

    List<WeChatHelpModel> findByUserAndHelpType(@Param(value = "loginName") String loginName,
                                                @Param(value = "openId") String openId,
                                                @Param(value = "helpType") WeChatHelpType helpType);
}
