package com.tuotiansudai.api.controller;

import com.tuotiansudai.enums.BankCallbackType;

import java.beans.PropertyEditorSupport;

public class MobileAppBankCallbackTypePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text){
        setValue(BankCallbackType.valueOf(text.toUpperCase()));
    }
}