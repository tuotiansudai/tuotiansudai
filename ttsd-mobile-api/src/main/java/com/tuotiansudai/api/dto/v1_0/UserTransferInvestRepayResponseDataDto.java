package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.AmountConverter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserTransferInvestRepayResponseDataDto extends BaseResponseDataDto {

    private List<TransferInvestRepayDataDto> investRepays = new ArrayList<>();

    public List<TransferInvestRepayDataDto> getInvestRepays() {
        return investRepays;
    }

    public void setInvestRepays(List<TransferInvestRepayDataDto> investRepays) {
        this.investRepays = investRepays;
    }
}
