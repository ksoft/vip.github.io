package com.jlt.datasync.dto;

import com.jlt.common.enums.DataSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

/**
 * @author Billy.Zhang
 * @date 2018/9/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobWriteParams implements Serializable{
    private CountDownLatch countDownLatch;
    private DataTransferDto dataTransferDto;
    private JdbcTemplate jdbcTemplate;
    private DataSchema dataSchema;
}
