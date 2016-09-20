package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;

public interface MembershipGiveTempService {
    //TODO: 该类是为解决循环引用问题，临时引入的MembershipService的包装类,循环引用问题解决后，需要将相关逻辑迁移至原始类中
    BaseDto<BaseDataDto> approveMembershipGive(long id, String validLoginName);
}
