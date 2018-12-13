package com.jlt.common.enums;

/**
 * @author Billy.Zhang
 * @date 2018/10/25
 */
public enum DataReadSource {
    DB("DB", "数据库"), REDIS("REDIS", "REDIS"), EXT("EXT", "EXT");

    DataReadSource(String key, String value) {
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
