package com.zhonghezhihui.iorg.service;

import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.entity.WhitelistEntity;

import java.util.List;

public interface WhiteListService {

    List<WhitelistEntity> queryBySendDeviceId(String deviceId);

    List<WhitelistEntity> queryByRemoveDeviceId(String deviceId);

    WhitelistEntity queryByRemoveId(Integer id);

    void updateSendWhiteById(WhitelistEntity whitelistEntity);

    void updateRemoveWhiteById(WhitelistEntity whitelistEntity);

    JSONObject sendWhiteList(String devId, String transactionId);

    JSONObject removeWhiteList(String devId, String transactionId);

    void addSendPerson();
}
