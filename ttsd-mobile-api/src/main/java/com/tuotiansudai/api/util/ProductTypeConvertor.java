package com.tuotiansudai.api.util;

import com.tuotiansudai.repository.model.ProductType;

public class ProductTypeConvertor {

    public static ProductType stringConvertTo(String s) {
        switch (s) {
            case "SYL":
                return ProductType._30;
            case "WYX":
                return ProductType._90;
            case "JYF":
                return ProductType._180;
        }

        return null;
    }
}
