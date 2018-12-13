package com.jlt.datasync.biz;

import com.jlt.datasync.domain.JltJobDetail;
import com.jlt.datasync.dto.DataTransferDto;
import com.jlt.datasync.dto.DataWriteDto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
public interface DataHandlerBiz {

    Future<DataTransferDto> readData(JltJobDetail jltJobDetail, DataTransferDto dataTransferDto, String lastExecSuccessTime, Date now);

    Future<DataWriteDto> writeData(JltJobDetail jltJobDetail, Map<String, List<Map<String, Object>>> dataMap,Date now,DataWriteDto dataWriteDto);

    DataWriteDto getDataWriteDto(JltJobDetail jltJobDetail, Map.Entry<String, Object> entry);
}
