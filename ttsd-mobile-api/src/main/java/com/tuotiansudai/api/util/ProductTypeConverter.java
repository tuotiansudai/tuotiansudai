package com.tuotiansudai.api.util;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.ProductType;

public class ProductTypeConverter {

    public static ProductType stringConvertTo(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return null;
        }

        switch (s) {
            case "SYL":
                return ProductType._30;
            case "WYX":
                return ProductType._90;
            default:
                return ProductType._180;
        }
    }
}
