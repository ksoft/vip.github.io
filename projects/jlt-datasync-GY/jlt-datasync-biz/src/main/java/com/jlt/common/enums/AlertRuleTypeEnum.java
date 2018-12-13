package com.jlt.common.enums;

/**
 * @author Billy.Zhang
 * @date 2018/9/18
 */
public enum AlertRuleTypeEnum {
    PICKUP_WARNING("TAKE_DELIVERY_PRESCRIPTION_RULE_TYPE", "提货时效异常"), ARRIVAL_WARNING("SERVICE_PRESCRIPTION_RULE_TYPE",
            "送达时效异常"), RECEIPT_WARNING("RETURN_PRESCRIPTION_RULE_TYPE", "回单时效异常"), RETURN_WARNING("RETURN_LIST_PRESCRIPTION_RULE_TYPE",
            "返单时效异常"), RETURN_GOODS_WARNING("RETURN_GOODS_PRESCRIPTION_RULE_TYPE", "退货时效异常"), THERMOMETER_RETURN_WARNING(
            "THERMOMETER_RETURN_PRESCRIPTION_RULE_TYPE", "温度计返回时效异常");

    AlertRuleTypeEnum(String key, String value) {
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

    public static String getTypeName(AlertRuleTypeEnum alertRuleTypeEnum){
        String alertRuleTypeStr=null;
        switch (alertRuleTypeEnum){
            case PICKUP_WARNING:alertRuleTypeStr="提货";break;
            case ARRIVAL_WARNING:alertRuleTypeStr="送达";break;
            case RECEIPT_WARNING:alertRuleTypeStr="回单";break;
            case RETURN_WARNING:alertRuleTypeStr="返单";break;
            case RETURN_GOODS_WARNING:
                alertRuleTypeStr = "退货";
                break;
            case THERMOMETER_RETURN_WARNING:
                alertRuleTypeStr = "温度计返回";
                break;
            default:
        }
        return alertRuleTypeStr;
    }
}
