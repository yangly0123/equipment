package com.zhonghezhihui.iorg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.service.HeartBeatService;
import com.zhonghezhihui.iorg.service.WhiteListService;
import com.zhonghezhihui.iorg.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeartBeatServiceImpl implements HeartBeatService {

    protected static final Logger logger = LoggerFactory.getLogger(HeartBeatServiceImpl.class);

    @Autowired
    WhiteListService whiteListService;

    @Override
    public JSONObject handleHeartbeat(JSONObject data) {
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
        return result;
    }


}
