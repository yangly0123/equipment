package com.zhonghezhihui.iorg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.entity.CanOrderEntity;
import com.zhonghezhihui.iorg.entity.CanOrderProductEntity;
import com.zhonghezhihui.iorg.entity.UserEntity;
import com.zhonghezhihui.iorg.init.InitData;
import com.zhonghezhihui.iorg.service.MealService;
import com.zhonghezhihui.iorg.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    protected static final Logger logger = LoggerFactory.getLogger(MealServiceImpl.class);

    @Autowired
    InitData initData;

    @Override
    public JSONObject takeMeal(JSONObject data) {
        LocalDateTime date = LocalDateTime.now();
        JSONObject res = new JSONObject();
        String api = data.getString("api");
        String devId = data.getString("dev_id");
        String serialno = data.getString("serialno");
        String transaction_id = data.getString("transaction_id");
        String build_time = data.getString("build_time");

        JSONObject params = data.getJSONObject("params");
        String payMode = params.getString("pay_mode");
        String payCode = params.getString("pay_code");
//        String qr_code = params.getString("qr_code");
        String tradeId = params.getString("trade_id");
        String queryMode = params.getString("query_mode");

        Long time = ServerUtils.getTimestampSeconds();
        String noncestr = ServerUtils.getRandomString(32);
        String baseStr = time + noncestr + GlobalConstant.CARD_PWD;
        String sign = ServerUtils.computeMd5Sign(baseStr);

        res.put("api", api);
        res.put("time", time.toString());
        res.put("noncestr", noncestr);
        res.put("interval", GlobalConstant.interval);
        res.put("sign", sign);
        res.put("transaction_id", transaction_id);
        res.put("build_time", build_time);
        ArrayList<Object> msgs = new ArrayList<>();
        ArrayList<Object> detail = new ArrayList<>();
        try {
            Integer userId = null;
            userId = Integer.parseInt(payCode, 10);
            UserEntity userEntity = initData.userEntity;
            String name = userEntity.getName();
            Integer balance = userEntity.getBalance();
            Integer allowance = userEntity.getAllowance();
            Integer total = balance + allowance;
            res.put("emp_fname", name);
            res.put("title", "智慧食堂");
            res.put("balance", total.toString());

            CanOrderEntity canOrderEntity = initData.canOrderEntity;
            logger.warn("real takemeal CanOrderEntity ==============================: " + JSON.toJSONString(canOrderEntity));
            Integer status = canOrderEntity.getStatus();
            if (status.equals(1)) {
                res.put("result_code", "1");
                res.put("result_msg", "已取餐");
                res.put("tts", "已取餐");

            } else {
                res.put("result_code", "0");
                res.put("result_msg", "查询成功");
                res.put("tts", "查询成功");
            }
            List<CanOrderProductEntity> list = queryByOrderId(canOrderEntity.getId());
            logger.warn("real takemeal CanOrderProductEntity list ==============================: " + JSON.toJSONString(list));
            int size = list.size();
            for (int i = 0; i < size; i++) {
                CanOrderProductEntity canOrderProductEntity = list.get(i);
                String productName = canOrderProductEntity.getName();
                Integer num = canOrderProductEntity.getNum();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("menu", productName);
                jsonObject.put("amount", num);
                detail.add(jsonObject);
                HashMap<Object, Object> msg = new HashMap<>();
                msg.put("line", productName + " * " +  num);
                msgs.add(msg);
            }
            res.put("msg", msgs);
            res.put("detail", detail);
            res.put("result", new ArrayList<>());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            res.put("result", new ArrayList<>());
            logger.warn("handleTakemeal = " + res);
            return res;
        }
    }

    @Override
    public JSONObject takeLok(JSONObject data) {
        LocalDateTime date = LocalDateTime.now();
        JSONObject res = new JSONObject();

        String api = data.getString("api");
        String devId = data.getString("dev_id");
        String serialno = data.getString("serialno");
        String transaction_id = data.getString("transaction_id");
        String build_time = data.getString("build_time");

        JSONObject params = data.getJSONObject("params");
        String payMode = params.getString("pay_mode");
        String payCode = params.getString("pay_code");
        String tradeId = params.getString("trade_id");
        Long time = ServerUtils.getTimestampSeconds();
        String noncestr = ServerUtils.getRandomString(32);
        String baseStr = time + noncestr + GlobalConstant.CARD_PWD;
        String sign = ServerUtils.computeMd5Sign(baseStr);

        res.put("api", api);
        res.put("time", time.toString());
        res.put("noncestr", noncestr);
        res.put("interval", GlobalConstant.interval);
        res.put("sign", sign);
        res.put("transaction_id", transaction_id);
        res.put("build_time", build_time);
        ArrayList<Object> msgs = new ArrayList<>();
        ArrayList<Object> detail = new ArrayList<>();
        try {
            UserEntity userEntity = initData.userEntity;
            String name = userEntity.getName();
            Integer balance = userEntity.getBalance();
            Integer allowance = userEntity.getAllowance();
            res.put("emp_fname", name);
            res.put("title", "智慧食堂");
            res.put("balance", balance + allowance);

            CanOrderEntity canOrderEntity = initData.canOrderEntity;

            Integer status = canOrderEntity.getStatus();
            if (status.equals(1)) {
                res.put("result_code", "1");
                res.put("result_msg", "已取餐");
                res.put("tts", "已取餐");
                HashMap<Object, Object> msg2 = new HashMap<>();
                msg2.put("line", "已取餐");
                msgs.add(msg2);
                logger.info("real takelok  The order has been taken =================================== ");
            } else {
                canOrderEntity.setStatus(1);
                canOrderEntity.setUpdateTime(LocalDateTime.now());
                updateMealOrder(canOrderEntity);

                res.put("result_code", "0");
                res.put("result_msg", "取餐成功");
                res.put("tts", "取餐成功");
                List<CanOrderProductEntity> list = initData.canOrderProductEntities;
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    CanOrderProductEntity canOrderProductEntity = list.get(i);
                    String productName = canOrderProductEntity.getName();
                    Integer num = canOrderProductEntity.getNum();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("menu", productName);
                    jsonObject.put("amount", num);
                    detail.add(jsonObject);
                }
            }

            res.put("msg", msgs);
            res.put("detail", detail);
            res.put("result", new ArrayList<>());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            res.put("result", new ArrayList<>());
            return res;
        }
    }


    @Override
    public void updateMealOrder(CanOrderEntity canOrderEntity) {
        initData.canOrderEntity.setStatus(canOrderEntity.getStatus());
        initData.canOrderEntity.setUpdateTime(canOrderEntity.getUpdateTime());
        logger.info("real takelok update canOrderEntity status == 1 =================================== ");
    }

    @Override
    public List<CanOrderProductEntity> queryByOrderId(Integer orderId) {
        return initData.canOrderProductEntities;
    }
}
