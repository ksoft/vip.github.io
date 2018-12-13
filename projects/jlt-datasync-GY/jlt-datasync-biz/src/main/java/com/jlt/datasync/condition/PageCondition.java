package com.jlt.datasync.condition;

import lombok.Data;

/**
 * @author Billy.Zhang
 * @date 2018/9/13
 */
@Data
public class PageCondition extends BaseCondition {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer pageNumber;
}
