package com.zhonghezhihui.iorg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.controller.HeartBeatController;
import com.zhonghezhihui.iorg.service.RecordsService;
import com.zhonghezhihui.iorg.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordsServiceImpl implements RecordsService {

    protected static final Logger logger = LoggerFactory.getLogger(RecordsServiceImpl.class);

    @Override
    public JSONObject handleRecords(JSONObject data) {
        logger.warn("hearbeat handle records start ==========================: ");
        JSONArray records = data.getJSONArray("records");
        if (records != null && records.size() > 0) {
            List<JSONObject> list = records.toJavaList(JSONObject.class);
            List<String> ids = new ArrayList<>(list.size());
            for (JSONObject jsonObject : list) {
                String devRecordId = jsonObject.getString("id");
                ids.add(devRecordId);
            }
            logger.warn("hearbeat return records ids ==========================: " + JSON.toJSONString(ids));
            String time = ServerUtils.getTimestampSeconds().toString();
            String noncestr = ServerUtils.getRandomString(32).toLowerCase();
            String transactionId = data.getString("transaction_id");
            JSONObject result = new JSONObject();
            result.put("api", GlobalConstant.SM_HEARTBEAT_API);
            result.put("time", time);
            result.put("noncestr", noncestr);
            String base = time + noncestr + GlobalConstant.CARD_PWD;
            result.put("sign", ServerUtils.computeMd5Sign(base));
            result.put("interval", GlobalConstant.interval);
            result.put("transaction_id", transactionId);
            result.put("records", ids);
            return result;
        }
        return null;
    }
}
