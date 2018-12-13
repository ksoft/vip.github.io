package com.jlt.datasync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/9/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataWriteDto implements Serializable{
    private String deleteSql;
    private String writeSql;
    private List<Object[]> params =new ArrayList<>();
}
