package io.openjob.server.cluster.service;

import io.openjob.common.request.WorkerJobInstanceTaskLogRequest;
import io.openjob.server.cluster.dto.WorkerJobInstanceTaskLogReqDTO;
import io.openjob.server.cluster.manager.JobInstanceTaskLogManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author stelin swoft@qq.com
 * @since 1.0.0
 */
@Slf4j
@Service
public class JobInstanceTaskLogService {
    private final JobInstanceTaskLogManager jobInstanceTaskLogManager;

    @Autowired
    public JobInstanceTaskLogService(JobInstanceTaskLogManager jobInstanceTaskLogManager) {
        this.jobInstanceTaskLogManager = jobInstanceTaskLogManager;
    }


    /**
     * Handle instance log.
     *
     * @param logReq log request.
     */
    public void handleInstanceTaskLog(WorkerJobInstanceTaskLogRequest logReq) {
        if (Objects.isNull(logReq.getFieldList())) {
            log.warn("Log request fieldList is null");
            return;
        }

        // Manual mapping for nested generic list to avoid Orika nested generics issue
        WorkerJobInstanceTaskLogReqDTO dto = new WorkerJobInstanceTaskLogReqDTO();
        List<List<WorkerJobInstanceTaskLogReqDTO.WorkerJobInstanceTaskLogFieldReqDTO>> targetFieldList =
                logReq.getFieldList().stream()
                        .map(inner -> {
                            if (Objects.isNull(inner)) {
                                return new ArrayList<WorkerJobInstanceTaskLogReqDTO.WorkerJobInstanceTaskLogFieldReqDTO>();
                            }
                            return inner.stream()
                                    .filter(Objects::nonNull)
                                    .map(f -> {
                                        WorkerJobInstanceTaskLogReqDTO.WorkerJobInstanceTaskLogFieldReqDTO fd =
                                                new WorkerJobInstanceTaskLogReqDTO.WorkerJobInstanceTaskLogFieldReqDTO();
                                        fd.setName(f.getName());
                                        fd.setValue(f.getValue());
                                        return fd;
                                    })
                                    .collect(Collectors.toList());
                        })
                        .collect(Collectors.toList());
        dto.setFieldList(targetFieldList);

        this.jobInstanceTaskLogManager.handleInstanceTaskLog(dto);
    }
}
