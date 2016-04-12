package com.esoft.core.jsf.tag;

import com.esoft.core.util.SpringBeanUtil;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.bankcard.service.BankCardService;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.*;
import java.io.IOException;
import java.util.List;

public class BankCardBoundValidator extends TagHandler {

    private final TagAttribute userId;

    @Override
    public void apply(FaceletContext faceletContext, UIComponent uiComponent) throws IOException {
        if (this.userId == null){
            this.nextHandler.apply(faceletContext, uiComponent);
        }

        String userId = this.userId.getValue(faceletContext);

        BankCardService bankCardService = (BankCardService) SpringBeanUtil.getBeanByName("bankCardService");
        List<BankCard> boundBankCards = bankCardService.getBoundBankCardsByUserId(userId);
        if (boundBankCards.isEmpty()) {
            this.nextHandler.apply(faceletContext, uiComponent);
        }

    }

    public BankCardBoundValidator(ComponentConfig componentConfig) {
        super(componentConfig);
        this.userId = this.getRequiredAttribute("userId");
        if (this.userId == null)
            throw new TagAttributeException(this.userId, "The `userId` attribute has to be specified for validate bank card bound!");
    }
}
