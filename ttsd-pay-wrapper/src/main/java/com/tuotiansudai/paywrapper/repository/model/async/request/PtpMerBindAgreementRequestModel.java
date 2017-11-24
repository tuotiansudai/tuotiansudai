package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.repository.model.AgreementType;

import java.text.MessageFormat;
import java.util.Map;

public class PtpMerBindAgreementRequestModel extends BaseAsyncRequestModel {

    private String userId;

    private AgreementType userBindAgreementList;

    public PtpMerBindAgreementRequestModel() {
    }

    public PtpMerBindAgreementRequestModel(String userId, AgreementDto dto) {
        super(dto.getSource(), getService(dto));

        AsyncUmPayService asyncUmPayService = getService(dto);
        this.service = asyncUmPayService.getServiceName();
        this.userId = userId;
        this.setNotifyUrl(MessageFormat.format("{0}/{1}", PAY_CALLBACK_BACK_HOST, asyncUmPayService.getNotifyCallbackPath()));
        if (dto.isNoPasswordInvest()) {
            this.userBindAgreementList = AgreementType.ZTBB0G00;
        } else if (dto.isFastPay()) {
            this.userBindAgreementList = AgreementType.ZKJP0700;
        } else if (dto.isAutoRepay()) {
            this.userBindAgreementList = AgreementType.ZHKB0H01;
        }
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", this.getRetUrl());
        payRequestData.put("notify_url", this.getNotifyUrl());
        payRequestData.put("user_id", this.userId);
        payRequestData.put("user_bind_agreement_list", this.userBindAgreementList.name());
        return payRequestData;
    }

    private static AsyncUmPayService getService(AgreementDto dto) {
        if (dto.isNoPasswordInvest()) {
            return AsyncUmPayService.NO_PASSWORD_INVEST_PTP_MER_BIND_AGREEMENT;
        }
        if (dto.isAutoRepay()) {
            return AsyncUmPayService.AUTO_REPAY_PTP_MER_BIND_AGREEMENT;
        }
        if (dto.isFastPay()) {
            return AsyncUmPayService.FAST_PAY_MER_BIND_AGREEMENT;
        }

        return AsyncUmPayService.FAST_PAY_MER_BIND_AGREEMENT;
    }

}
