package com.jlt.common.enums;

/**
 * @author Billy.Zhang
 * @date 2018/9/30
 */
public enum AlarmWarning {
    ALARM("报警"), WARNING("预警");

    AlarmWarning(String value) {
        this.value = value;
    }

    private final String value;


    public String getValue() {
        return this.value;
    }
}
