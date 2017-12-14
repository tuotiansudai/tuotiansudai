package com.tuotiansudai.point.repository.model;

public enum PointChangingResult {
    NO_ACCOUNT,
    CHANGING_FREQUENTLY,
    CHANGING_FAIL,
    CHANGING_SUCCESS;

    public boolean is_success() {
        return this == CHANGING_SUCCESS;
    }

}
