package com.zhonghezhihui.iorg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.entity.UserEntity;
import com.zhonghezhihui.iorg.init.InitData;
import com.zhonghezhihui.iorg.service.InfoQueryService;
import com.zhonghezhihui.iorg.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class InfoQueryServiceImpl implements InfoQueryService {

    protected static final Logger logger = LoggerFactory.getLogger(InfoQueryServiceImpl.class);

    @Autowired
    InitData initData;

    @Override
    public JSONObject infoQuery(JSONObject data) {
        try {
            String api = data.getString("api");
            String devId = data.getString("dev_id");
            String transaction_id = data.getString("transaction_id");
            String serialno = data.getString("serialno");

            JSONObject params = data.getJSONObject("params");
            String payCode = params.getString("pay_code");
            String queryMode = params.getString("query_mode");
            Integer accountId = Integer.parseInt(payCode, 10);

            Long time = ServerUtils.getTimestampSeconds();
            String noncestr = ServerUtils.getRandomString(32);
            String baseStr = time + noncestr + GlobalConstant.CARD_PWD;
            String sign = ServerUtils.computeMd5Sign(baseStr);
            JSONObject res = new JSONObject();
            res.put("api", api);
            res.put("query_code", "0");
            res.put("interval", GlobalConstant.interval);
            res.put("time", time.toString());
            res.put("sign", sign);
            res.put("transaction_id", transaction_id);
            res.put("result_msg", "");
            res.put("noncestr", noncestr);
            res.put("tts", "");
            res.put("result_code", "0");

            logger.warn("infoquery query user accountId: =======================" + accountId);
            UserEntity userEntity = initData.userEntity;
            Integer balance = userEntity.getBalance();
            Integer allowance = userEntity.getAllowance();
            Integer amount = balance + allowance;
            logger.warn("infoquery user balance: ======================" + balance);
            logger.warn("infoquery user allowance: ======================" + allowance);
            logger.warn("infoquery user amount: ======================" + amount);

            ArrayList<JSONObject> result = new ArrayList<>();
            //query_mode  1 表示消费机更正操作查询， 0 表示正常查询
            if (queryMode.equals("1")) {
                Integer Corrections = GlobalConstant.CORRECTIONS;
                String dataBaseStr = Corrections.toString() + amount.toString() + time + noncestr + GlobalConstant.CARD_PWD;
                String dataSign = ServerUtils.computeMd5Sign(dataBaseStr);

                JSONObject resData = new JSONObject();
                resData.put("Corrections", Corrections);
                resData.put("balance", amount);
                resData.put("sign", dataSign);
                result.add(resData);

            } else {
                String dataBaseStr = accountId.toString() + amount.toString() + time + noncestr + GlobalConstant.CARD_PWD;
                String dataSign = ServerUtils.computeMd5Sign(dataBaseStr);
                JSONObject resData = new JSONObject();
                resData.put("account_id", accountId);
                resData.put("balance", amount);
                resData.put("sign", dataSign);
                result.add(resData);
                }
            res.put("result", result);
            logger.warn("infoquery return result =================================: " + JSON.toJSONString(res));
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
