package com.jlt.datasync.exthandler;

import com.jlt.datasync.domain.JltJobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Billy.Zhang
 * @date 2018/10/26
 */
public abstract class IExtDataReadHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("saasJdbcTemplate")
    protected JdbcTemplate saasJdbcTemplate;
    @Autowired
    @Qualifier("upmJdbcTemplate")
    protected JdbcTemplate upmJdbcTemplate;
    @Autowired
    @Qualifier("pmsJdbcTemplate")
    protected JdbcTemplate pmsJdbcTemplate;

    public IExtDataReadHandler(){}

    public abstract List<Map<String, Object>> process(JltJobDetail jltJobDetail,String lastExecSuccessTime,Date now,String... var1) throws Exception;
}
