package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
import com.tuotiansudai.activity.repository.model.WeChatHelpType;
import com.tuotiansudai.activity.repository.model.WeChatHelpView;
import com.tuotiansudai.activity.repository.model.WeChatUserInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeChatHelpMapper {

    void create(WeChatHelpModel weChatHelpModel);

    void update(WeChatHelpModel weChatHelpModel);

    void updateHelpUserCount(@Param(value = "id") long id);

    int getHelpUserCount(@Param(value = "id") long id);

    WeChatHelpModel findById(@Param(value = "id") long id);

    WeChatHelpModel lockById(@Param(value = "id") long id);

    List<WeChatHelpModel> findByUserAndHelpType(@Param(value = "loginName") String loginName,
                                                @Param(value = "openId") String openId,
                                                @Param(value = "helpType") WeChatHelpType helpType);

    List<WeChatHelpModel> findByLoanId(@Param(value = "loanId") long loanId);

    List<WeChatHelpView> findByKeyWord(@Param(value = "keyWord") String keyWord,
                                       @Param(value = "minInvest") Long minInvest,
                                       @Param(value = "maxInvest") Long maxInvest,
                                       @Param(value = "helpType") WeChatHelpType helpType);
}
