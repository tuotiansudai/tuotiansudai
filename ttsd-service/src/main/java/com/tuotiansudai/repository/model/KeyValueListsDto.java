package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.List;

public class KeyValueListsDto implements Serializable{

    private String key;

    private List<KeyValueModel> keyValueModels;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<KeyValueModel> getKeyValueModels() {
        return keyValueModels;
    }

    public void setKeyValueModels(List<KeyValueModel> keyValueModels) {
        this.keyValueModels = keyValueModels;
    }

}
