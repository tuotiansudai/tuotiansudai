package com.tuotiansudai.fudian.controller;

import com.tuotiansudai.fudian.config.ApiType;

import java.beans.PropertyEditorSupport;

public class CallbackTypePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text){
        setValue(ApiType.valueOf(text.toUpperCase()));
    }
}