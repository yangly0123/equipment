package com.zhonghezhihui.iorg.service;

import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.entity.DeviceEntity;

public interface DeviceService {

    DeviceEntity queryByDeviceId(String deviceId, String serialno);

    DeviceEntity update(DeviceEntity deviceEntity);

    void setModel(Integer model);

    JSONObject reloadSettings(DeviceEntity deviceEntity, String transactionId);

}
