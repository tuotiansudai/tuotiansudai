package com.tuotiansudai.fudian.controller;

import com.tuotiansudai.fudian.dto.request.BankUserRole;

import java.beans.PropertyEditorSupport;

public class RegisterRoleTypePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text){
        setValue(BankUserRole.valueOf(text.toUpperCase()));
    }
}