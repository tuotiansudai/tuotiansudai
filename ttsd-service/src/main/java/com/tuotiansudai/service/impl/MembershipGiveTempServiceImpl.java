package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsUserReceiveMembershipDto;
import com.tuotiansudai.membership.dto.MembershipGiveDto;
import com.tuotiansudai.membership.service.ImportService;
import com.tuotiansudai.membership.service.MembershipGiveService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.MembershipGiveTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipGiveTempServiceImpl implements MembershipGiveTempService {

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private MembershipGiveService membershipGiveService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ImportService importService;

    @Override
    public BaseDto<BaseDataDto> approveMembershipGive(long id, String validLoginName) {
        BaseDto<BaseDataDto> baseDto = membershipGiveService.approveMembershipGive(id, validLoginName);
        if (baseDto.getData().getStatus()) {
            MembershipGiveDto membershipGiveDto = membershipGiveService.getMembershipGiveDtoById(id);
            if (membershipGiveDto.isSmsNotify()) {
                List<String> importUsers = importService.getImportStrings(ImportService.redisMembershipGiveReceivers, membershipGiveDto.getId());
                for (String loginName : importUsers) {
                    UserModel userModel = userMapper.findByLoginName(loginName);
                    if (null != userModel) {
                        smsWrapperClient.sendImportUserReceiveMembership(new SmsUserReceiveMembershipDto(userModel.getMobile(), membershipGiveDto.getMembershipLevel()));
                    }
                }
            }
        }
        return baseDto;
    }
}
