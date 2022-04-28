package com.zhonghezhihui.iorg.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.entity.DeviceEntity;
import com.zhonghezhihui.iorg.init.InitData;
import com.zhonghezhihui.iorg.service.DeviceService;
import com.zhonghezhihui.iorg.service.HeartBeatService;
import com.zhonghezhihui.iorg.service.RecordsService;
import com.zhonghezhihui.iorg.service.WhiteListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/request")
public class HeartBeatController {

    protected static final Logger logger = LoggerFactory.getLogger(HeartBeatController.class);

    @Autowired
    HeartBeatService heartBeatService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    WhiteListService whiteListService;

    @Autowired
    RecordsService recordsService;

    @Autowired
    InitData initData;

    /**
     * @param data
     * @return
     */
    @RequestMapping("heartbeat")
    public JSONObject heartbeat(@RequestBody JSONObject data) {
        logger.warn("松美消费机 start heartbeat ======================================");
        try {
            logger.warn("heartbeat received request data ===========================：" + JSON.toJSONString(data));
            String devId = data.getString("dev_id");
            logger.warn("heartbeat dev_id ===========================: " + devId);
            String serialno = data.getString("serialno");
            logger.warn(" heartbeat serialno ===========================: " + serialno);
            String transactionId = data.getString("transaction_id");
            logger.info("query device, the device id : ===============================" + devId);
            DeviceEntity deviceEntity = deviceService.queryByDeviceId(devId, serialno);
            if (deviceEntity != null) {
                deviceEntity.setLastHeartbeatTime(LocalDateTime.now());
                //1. update heart time
                logger.warn("heartbeat update heart time, the time: ====================================" + new Date());
                deviceService.update(deviceEntity);
            }

            //2. send whitelist
            JSONObject sendWhiteListData = whiteListService.sendWhiteList(devId, transactionId);
            if (sendWhiteListData != null) {
                return sendWhiteListData;
            }
            //3. remove whitelist
            if (GlobalConstant.WHITELIST_REMOVE == 1) {
                JSONObject removeWhiteListData = whiteListService.removeWhiteList(devId, transactionId);
                return removeWhiteListData;
            }
            //4. send device settings
            if (initData.deviceEntityList.get(0).getSettings() != "" && GlobalConstant.DEVICE_SETTINGS_RELOAD.get() == 0) {
                JSONObject settings = deviceService.reloadSettings(deviceEntity, transactionId);
                GlobalConstant.DEVICE_SETTINGS_RELOAD.set(1);
                return settings;
            }
            //5. handle records
            JSONObject records = recordsService.handleRecords(data);
            if (records != null) {
                return records;
            }
            // 6. return heartbeat data
            JSONObject back = heartBeatService.handleHeartbeat(data);
            back.put("interval", GlobalConstant.interval);
            logger.warn("松美消费机 heartbeat return result ================================== :" + JSON.toJSONString(back));
            return back;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("松美消费机 heartbeat  failed, cause ==================================:  " + e.getMessage());
            return null;
        }
    }
}
