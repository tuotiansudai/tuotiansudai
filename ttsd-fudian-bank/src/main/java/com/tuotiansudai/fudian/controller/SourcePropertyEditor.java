package com.tuotiansudai.fudian.controller;

import com.tuotiansudai.fudian.dto.request.Source;

import java.beans.PropertyEditorSupport;

public class SourcePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text){
        setValue(Source.valueOf(text.toUpperCase()));
    }
}