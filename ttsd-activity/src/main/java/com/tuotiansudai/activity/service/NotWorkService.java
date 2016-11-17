package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.NotWorkMapper;
import com.tuotiansudai.activity.repository.model.NotWorkModel;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class NotWorkService {

    @Autowired
    NotWorkMapper notWorkMapper;

    @Autowired
    UserMapper userMapper;

    final private LocalDateTime activityStartTime = LocalDateTime.of(2016, 12, 1, 0, 0, 0, 0);

    final private LocalDateTime activityEndTime = LocalDateTime.of(2016, 12, 16, 0, 0, 0, 0);

    private interface UpdateModelProducer {
        NotWorkModel createProduce(NotWorkModel notWorkModel);

        NotWorkModel updateProduce(NotWorkModel notWorkModel);
    }

    private BaseDto<BaseDataDto> update(String loginName, UpdateModelProducer updateModelProducer) {
        if (LocalDateTime.now().isBefore(activityStartTime) || LocalDateTime.now().isAfter(activityEndTime)) {
            return new BaseDto<>(new BaseDataDto(false, "非活动时间"));
        }

        NotWorkModel notWorkModel = notWorkMapper.findByLoginName(loginName);
        if (null == notWorkModel) {
            UserModel userModel = userMapper.findByLoginName(loginName);
            if (null != userModel) {
                notWorkModel = new NotWorkModel(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile(), false);
                notWorkModel = updateModelProducer.createProduce(notWorkModel);
                notWorkMapper.create(notWorkModel);
            } else {
                return new BaseDto<>(new BaseDataDto(false, "用户不存在"));
            }
        } else {
            notWorkModel = updateModelProducer.updateProduce(notWorkModel);
            notWorkMapper.update(notWorkModel);
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> userInvest(String loginName, long investAmount) {
        return update(loginName, new UpdateModelProducer() {
            @Override
            public NotWorkModel createProduce(NotWorkModel notWorkModel) {
                notWorkModel.setInvestAmount(notWorkModel.getInvestAmount() + investAmount);
                return notWorkModel;
            }

            @Override
            public NotWorkModel updateProduce(NotWorkModel notWorkModel) {
                notWorkModel.setInvestAmount(investAmount);
                return notWorkModel;
            }
        });
    }

    public BaseDto<BaseDataDto> recommendedRegister(String recommendedLoginName) {
        UserModel userModel = userMapper.findByLoginName(recommendedLoginName);
        return update(userModel.getReferrer(), new UpdateModelProducer() {
            @Override
            public NotWorkModel createProduce(NotWorkModel notWorkModel) {
                notWorkModel.setRecommendedRegisterAmount(1);
                return notWorkModel;
            }

            @Override
            public NotWorkModel updateProduce(NotWorkModel notWorkModel) {
                notWorkModel.setRecommendedRegisterAmount(notWorkModel.getRecommendedRegisterAmount() + 1);
                return notWorkModel;
            }
        });
    }

    public BaseDto<BaseDataDto> recommendedIdentify(String recommendedLoginName) {
        UserModel userModel = userMapper.findByLoginName(recommendedLoginName);
        return update(userModel.getReferrer(), new UpdateModelProducer() {
            @Override
            public NotWorkModel createProduce(NotWorkModel notWorkModel) {
                notWorkModel.setRecommendedIdentifyAmount(1);
                return notWorkModel;
            }

            @Override
            public NotWorkModel updateProduce(NotWorkModel notWorkModel) {
                notWorkModel.setRecommendedIdentifyAmount(notWorkModel.getRecommendedIdentifyAmount() + 1);
                return notWorkModel;
            }
        });
    }

    public BaseDto<BaseDataDto> recommendedInvest(String recommendedLoginName, long investAmount) {
        UserModel userModel = userMapper.findByLoginName(recommendedLoginName);
        if (LocalDateTime.ofInstant(userModel.getRegisterTime().toInstant(), ZoneId.systemDefault()).isBefore(activityStartTime) ||
                LocalDateTime.ofInstant(userModel.getRegisterTime().toInstant(), ZoneId.systemDefault()).isAfter(activityEndTime)) {
            return new BaseDto<>(new BaseDataDto(false, "非活动期间注册用户"));
        }
        String referrerLoginName = userModel.getReferrer();
        if (Strings.isNullOrEmpty(referrerLoginName)) {
            return new BaseDto<>(new BaseDataDto(false, "无推荐人"));
        }
        return update(referrerLoginName, new UpdateModelProducer() {
            @Override
            public NotWorkModel createProduce(NotWorkModel notWorkModel) {
                notWorkModel.setRecommendedInvestAmount(investAmount);
                return notWorkModel;
            }

            @Override
            public NotWorkModel updateProduce(NotWorkModel notWorkModel) {
                notWorkModel.setRecommendedInvestAmount(notWorkModel.getRecommendedInvestAmount() + investAmount);
                return notWorkModel;
            }
        });
    }
}
