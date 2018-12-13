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
public class DataResult implements Serializable{
    private Distance distance;
    private Duration duration;
}
