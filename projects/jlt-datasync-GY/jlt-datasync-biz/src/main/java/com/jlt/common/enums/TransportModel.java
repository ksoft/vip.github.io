package com.jlt.common.enums;

/**
 * @author Billy.Zhang
 * @date 2018/9/30
 */
public enum  TransportModel {
    TRUNK_LINE_ITEM_CODE("TRUNK_LINE_ITEM_CODE", "干线"),
    CITY_LINE_ITEM_CODE("CITY_LINE_ITEM_CODE", "市内");

    TransportModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private final String key;
    private final String value;

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
