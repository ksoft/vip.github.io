package com.jlt.datasync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTransferDto implements Serializable {
    private String startTime;
    private Long tenantId;
    Map<String, List<Map<String, Object>>> saasDataMap = new HashMap<>();
    Map<String, List<Map<String, Object>>> upmDataMap = new HashMap<>();
    Map<String, List<Map<String, Object>>> pmsDataMap = new HashMap<>();
}
