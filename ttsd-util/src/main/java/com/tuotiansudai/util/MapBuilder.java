package com.tuotiansudai.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 不支持并发
 * Created by qduljs2011 on 2018/10/29.
 */
public class MapBuilder<K, V> {
    private Map<K, V> map = new HashMap<>();

    public MapBuilder<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }
}
