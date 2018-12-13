package com.jlt.common.enums;

/**
 * @author Billy.Zhang
 * @date 2018/9/17
 */
public enum JobType {
    READ("R", "读数据"), WRITE("W", "写数据");

    JobType(String key, String value) {
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
