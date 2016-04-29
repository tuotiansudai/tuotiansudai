package com.esoft.archer.user.controller;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.VIEW)
public class AreaHome2 extends AreaHome implements Serializable {
	private static final long serialVersionUID = -9028134079103290282L;
}
