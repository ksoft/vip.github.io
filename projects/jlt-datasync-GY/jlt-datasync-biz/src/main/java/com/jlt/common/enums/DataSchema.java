package com.jlt.common.enums;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
public enum DataSchema {
    SAAS("saas", "saas库"), UPM("upm", "upm库"),PMS("pms","pms库");

    DataSchema(String key, String value) {
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
