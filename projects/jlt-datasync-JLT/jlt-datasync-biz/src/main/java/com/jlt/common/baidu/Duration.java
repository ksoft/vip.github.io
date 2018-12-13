package com.jlt.common.baidu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Billy.Zhang
 * @date 2018/10/13
 */
@Data
@NoArgsConstructor
public class Duration implements Serializable {
    private String text;
    private String value;
}
