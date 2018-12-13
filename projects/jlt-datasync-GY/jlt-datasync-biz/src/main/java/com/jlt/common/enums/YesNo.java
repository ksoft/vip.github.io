package com.jlt.common.enums;

/**
 * @author Billy.Zhang
 * @date 2018/9/17
 */
public enum YesNo {
    YES("Y","y","是"), NO("N","n","否");

    YesNo(String upperKey,String lowerKey,String value) {
        this.upperKey = upperKey;
        this.lowerKey = lowerKey;
        this.value = value;
    }

    private final String upperKey;
    private final String lowerKey;
    private final String value;

    public String getUpperKey() {
        return this.upperKey;
    }

    public String getLowerKey(){return  this.lowerKey;}

    public String getValue() {
        return this.value;
    }
}
