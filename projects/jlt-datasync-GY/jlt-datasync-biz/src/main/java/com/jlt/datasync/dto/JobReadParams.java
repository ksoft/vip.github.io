package com.jlt.datasync.dto;

import com.jlt.common.enums.DataSchema;
import com.jlt.datasync.domain.JltJobDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Billy.Zhang
 * @date 2018/9/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobReadParams implements Serializable {
    private CountDownLatch countDownLatch;
    private DataTransferDto dataTransferDto;
    private JdbcTemplate jdbcTemplate;
    private JdbcTemplate logJdbcTemplate;
    private DataSchema dataSchema;
    private List<JltJobDetail> jobDetailList = new ArrayList<>();
    private Date lastExecSuccessTime;
}
