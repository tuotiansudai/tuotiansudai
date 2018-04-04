package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.WeChatUserInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeChatUserInfoMapper {

    void initCharset();

    void create(WeChatUserInfoModel weChatUserInfoModel);

    void update(WeChatUserInfoModel weChatUserInfoModel);

    WeChatUserInfoModel findByOpenId(@Param(value = "openId") String openId);
}
