package com.zhonghezhihui.iorg.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.controller.HeartBeatController;
import com.zhonghezhihui.iorg.entity.DeviceEntity;
import com.zhonghezhihui.iorg.init.InitData;
import com.zhonghezhihui.iorg.service.DeviceService;
import com.zhonghezhihui.iorg.util.JsonUtils;
import com.zhonghezhihui.iorg.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    protected static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    InitData initData;

    @Override
    public DeviceEntity queryByDeviceId(String deviceId, String serialno) {
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        for (DeviceEntity deviceEntity : initData.deviceEntityList) {
            if (deviceId.equals(deviceEntity.getDevId()) && serialno.equals(deviceEntity.getSerialno())) {
                return deviceEntity;
            }
        }
        return null;
    }


    @Override
    public DeviceEntity update(DeviceEntity deviceEntity) {
        for (DeviceEntity deviceEntity1 : initData.deviceEntityList) {
            if (deviceEntity.getDevId().equals(deviceEntity.getDevId()) && deviceEntity1.getSerialno().equals(deviceEntity.getSerialno())) {
                BeanUtils.copyProperties(deviceEntity, deviceEntity1);
                break;
            }
        }
        return deviceEntity;
    }

    @Override
    public void setModel(Integer model) {
        DeviceEntity deviceEntity = initData.deviceEntityList.get(0);
        if (model.intValue() == 0) {
            deviceEntity.setSettings(GlobalConstant.DEVICE_SETTINGS_MODEL_0);
        } else if (model.intValue() == 1) {
            deviceEntity.setSettings(GlobalConstant.DEVICE_SETTINGS_MODEL_1);
        } else if (model.intValue() == 2) {
            deviceEntity.setSettings(GlobalConstant.DEVICE_SETTINGS_MODEL_2);
        }
    }

    @Override
    public JSONObject reloadSettings(DeviceEntity deviceEntity, String transactionId) {
        logger.warn("iorg-smface reload device settings start  =====================================");
        JSONObject results = new JSONObject();
        String settings = deviceEntity.getSettings();
        logger.warn("device settings ===================================== : " + settings);
        JSONObject jsonObject = JsonUtils.jsonToPojo(settings, JSONObject.class);
        JSONArray dev_set = jsonObject.getJSONArray("dev_set");
        JSONArray time_period = jsonObject.getJSONArray("time_period");
        String api = "parameters";
        String time = ServerUtils.getTimestampSeconds().toString();
        String noncestr = ServerUtils.getRandomString(32).toLowerCase();
        String base = time + noncestr +GlobalConstant.CARD_PWD;
        results.put("api", api);
        results.put("time", time);
        results.put("noncestr", noncestr);
        results.put("interval", "10000");
        results.put("sign", ServerUtils.computeMd5Sign(base));
        results.put("transaction_id", transactionId);
        results.put("dev_set", dev_set);
        results.put("time_period", time_period);
        results.put("result_code", "0");
        results.put("result_msg", "OK");
        return results;
    }
}
