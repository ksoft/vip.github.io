package com.jlt.common.baidu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/10/13
 */
@Data
@NoArgsConstructor
public class RouteMatrix implements Serializable{
    private Integer status;
    private String message;
    private List<DataResult> result;
}
