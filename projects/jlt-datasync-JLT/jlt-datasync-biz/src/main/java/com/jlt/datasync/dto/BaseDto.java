package com.jlt.datasync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDto implements Serializable{
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 部门id
     */
    private Long departmentId;
    /**
     * id
     */
    private Long id;
    /**
     * rec_ver 版本
     */
    private Integer recVer;
    /**
     * creator
     */
    private String creator;
    /**
     * create_name
     */
    private String createName;
    /**
     * create_time
     */
    private Date createTime=new Date();
    /**
     * modifier
     */
    private String modifier;
    /**
     * modify_name
     */
    private String modifyName;
    /**
     * modify_time
     */
    private Date modifyTime=new Date();
    /**
     * 删除标识(0:未删除,1:已删除)
     */
    private Integer recStatus;
}
