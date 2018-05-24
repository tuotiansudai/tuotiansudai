package com.tuotiansudai.web.controller;

import com.tuotiansudai.enums.BankCallbackType;

import java.beans.PropertyEditorSupport;

public class BankCallbackTypePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text){
        setValue(BankCallbackType.valueOf(text.toUpperCase()));
    }
}