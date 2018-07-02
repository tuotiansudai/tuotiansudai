package com.tuotiansudai.fudian.controller;

import com.tuotiansudai.fudian.dto.request.RegisterRoleType;

import java.beans.PropertyEditorSupport;

public class RegisterRoleTypePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text){
        setValue(RegisterRoleType.valueOf(text.toUpperCase()));
    }
}