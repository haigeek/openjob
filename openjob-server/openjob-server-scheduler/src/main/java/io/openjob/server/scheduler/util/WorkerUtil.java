package io.openjob.server.scheduler.util;

import io.openjob.common.util.DateUtil;
import io.openjob.server.common.ClusterContext;
import io.openjob.server.common.dto.WorkerDTO;
import io.openjob.server.repository.constant.WorkerStatusEnum;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author stelin swoft@qq.com
 * @since 1.0.0
 */
public class WorkerUtil {

    /**
     * Worker heartbeat offline threshold (seconds).
     */
    private static final Long OFFLINE_THRESHOLD = 20L;

    /**
     * Select one worker by appid.
     *
     * @param appId        appId
     * @param failoverList failover list.
     * @return WorkerDTO
     */
    public static WorkerDTO selectWorkerByAppId(Long appId, Set<String> failoverList) {
        List<WorkerDTO> workers = ClusterContext.getWorkersByAppId(appId);
        if (CollectionUtils.isEmpty(workers)) {
            return null;
        }

        long now = DateUtil.timestamp();
        long heartbeatDeadline = now - OFFLINE_THRESHOLD;

        // Remove failover and offline workers.
        List<WorkerDTO> availableWorkers = workers.stream()
                .filter((w) -> WorkerStatusEnum.ONLINE.getStatus().equals(w.getStatus()))
                .filter((w) -> Objects.nonNull(w.getLastHeartbeatTime()) && w.getLastHeartbeatTime() > heartbeatDeadline)
                .filter((w) -> !failoverList.contains(w.getAddress()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(availableWorkers)) {
            return null;
        }

        int index = ThreadLocalRandom.current().nextInt(availableWorkers.size());
        return availableWorkers.get(index);
    }
}
