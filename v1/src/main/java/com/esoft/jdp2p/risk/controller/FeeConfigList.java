package com.esoft.jdp2p.risk.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.risk.model.FeeConfig;

@Component
@Scope(ScopeType.VIEW)
public class FeeConfigList extends EntityQuery<FeeConfig>{

}
