package com.zhonghezhihui.iorg.schedule;

import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.entity.DeviceEntity;
import com.zhonghezhihui.iorg.init.InitData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class HeartBeatCheckSchedule {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatCheckSchedule.class);

    @Autowired
    InitData initData;

    @Scheduled(cron="0 0/1 * * * ?")
    public void executeFileDownLoadTask() {

        logger.warn("heart beat check start ==================================");
        // 间隔1分钟,执行任务
        for (DeviceEntity deviceEntity : initData.deviceEntityList) {
            LocalDateTime lastHeartbeatTime = deviceEntity.getLastHeartbeatTime();
            if (LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()
                    - lastHeartbeatTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() > GlobalConstant.TOLERANCE_TIME) {

                logger.error("Heartbeat offline, the device id ===============================:" + deviceEntity.getDevId());
            } else {
                logger.warn("Heartbeat online, the device id ===============================:" + deviceEntity.getDevId());
            }

        }

    }
}
