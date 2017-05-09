package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.WeChatUserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeChatUserMapper {

    int create(WeChatUserModel model);

    WeChatUserModel findByOpenid(@Param(value = "openid") String openid);

    List<WeChatUserModel> findByLoginName(@Param(value = "loginName") String loginName);

    int update(WeChatUserModel model);
}
