package com.jlt.datasync.biz;

import com.jlt.datasync.domain.JltJobDetail;

import java.util.Date;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
public interface JltJobDetailLogBiz {
    int insertLog(JltJobDetail jltJobDetail, String msg, Date createTime);
}
