package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.WeChatHelpInfoModel;
import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
import com.tuotiansudai.activity.repository.model.WeChatUserInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeChatHelpInfoMapper {

    void create(WeChatHelpInfoModel weChatHelpInfoModel);

    WeChatHelpInfoModel findByWeChatUserId(@Param(value = "weChatUserId") long weChatUserId,
                                           @Param(value = "helpId") long helpId);

    List<WeChatUserInfoModel> findInfoByHelpId(@Param(value = "helpId") long helpId);
}
