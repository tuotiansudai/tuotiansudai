package com.tuotiansudai.smswrapper;

import java.util.List;

@FunctionalInterface
public interface ReplaceStrategyFunction {

    String replace(String source, List<String> params);
}
