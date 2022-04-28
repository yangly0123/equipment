package com.zhonghezhihui.iorg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.entity.PaymentEntity;
import com.zhonghezhihui.iorg.entity.UserEntity;
import com.zhonghezhihui.iorg.init.InitData;
import com.zhonghezhihui.iorg.service.CorrectonlineService;
import com.zhonghezhihui.iorg.service.UserService;
import com.zhonghezhihui.iorg.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CorrectonlineImpl implements CorrectonlineService {

    protected static final Logger logger = LoggerFactory.getLogger(CorrectonlineImpl.class);

    @Autowired
    InitData initData;

    @Autowired
    UserService userService;

    @Override
    public JSONObject correctonline(JSONObject data) {
        JSONObject res = new JSONObject();
        ArrayList<JSONObject> msg = new ArrayList<>();
        try {
            String api = data.getString("api");
            String transaction_id = data.getString("transaction_id");
            String devId = data.getString("dev_id");
            String serialno = data.getString("serialno");
            JSONObject params = data.getJSONObject("params");
            String payMode = params.getString("pay_mode");
            Integer accountId = Integer.parseInt(params.getString("pay_code"), 10);
            String signTime = params.getString("sign_time");
            String tradeId = params.getString("trade_id");
            Long time = ServerUtils.getTimestampSeconds();
            String noncestr = ServerUtils.getRandomString(32);
            String baseStr = time + noncestr + GlobalConstant.CARD_PWD;
            String sign = ServerUtils.computeMd5Sign(baseStr);

            res.put("api", api);
            res.put("query_code", "0");
            res.put("interval", GlobalConstant.interval);
            res.put("transaction_id", transaction_id);
            res.put("noncestr", noncestr);
            res.put("time", time.toString());
            res.put("sign", sign);

            logger.warn("correctonline query latest recordentity ==============================");
            logger.warn("correctonline query latest paymentEntity ==============================");

            PaymentEntity paymentEntity = initData.paymentEntity;
            Integer useBalance = paymentEntity.getUseBalance();
            Integer useAllowance = paymentEntity.getUseAllowance();
            Integer amount = paymentEntity.getAmount();
            Integer point = paymentEntity.getPoint();
            logger.warn("correctonline payment entity ==================================: " + JSON.toJSONString(paymentEntity));
            UserEntity userEntity = userService.queryById(accountId);
            logger.warn("correctonline user entity ==================================: " + JSON.toJSONString(userEntity));
            userEntity.setPoints(userEntity.getPoints() - point);
            if (!paymentEntity.getAmount().equals(useBalance + useAllowance)) {
                userEntity.setBalance(userEntity.getBalance() + paymentEntity.getAmount());
            } else {
                userEntity.setBalance(userEntity.getBalance() + useBalance);
                userEntity.setAllowance(userEntity.getAllowance() + useAllowance);
            }
            userEntity.setPoints(userEntity.getPoints() - point);
            // 退款到个人账户
            userService.updateById(userEntity.getId(), userEntity.getBalance(), userEntity.getAllowance(), userEntity.getPoints());
            Integer total = userEntity.getBalance() + userEntity.getAllowance();
            Integer consumeMoney =  paymentEntity.getUseAllowance() + paymentEntity.getUseBalance();
            String dataBaseStr = accountId.toString() + accountId.toString() + accountId.toString() + consumeMoney.toString() + time + noncestr + GlobalConstant.CARD_PWD;
            String dataSign = ServerUtils.computeMd5Sign(dataBaseStr);
            ArrayList<JSONObject> result = new ArrayList<>();
            JSONObject resData = new JSONObject();
            resData.put("account_id", accountId);
            resData.put("emp_id", accountId);
            resData.put("emp_fname", accountId);
            resData.put("consume_money", consumeMoney.toString());
            resData.put("sign", dataSign);
            result.add(resData);
            res.put("result", result);
            res.put("result_code", "0");
            res.put("tts", "退款" + ServerUtils.computeMoney(consumeMoney) + "元");
            JSONObject line1 = new JSONObject();
            line1.put("line", "退款" + ServerUtils.computeMoney(consumeMoney) + "元");
            msg.add(line1);
            JSONObject line2 = new JSONObject();
            line2.put("line", "余额" + ServerUtils.computeMoney(total) + "元");
            msg.add(line2);
            res.put("msg", msg);
            logger.warn("correctonline user entity ==================================: " + JSON.toJSONString(userEntity));
            logger.warn("松美消费机 Correctonline success ===============================");
            return res;
        } catch (Exception e) {
            logger.error("松美消费机 Correctonline failed ===============================");
            e.printStackTrace();
            if (res.getString("result_code") == null) {
                JSONObject line = new JSONObject();
                line.put("line", "更正失败");
                msg.add(line);
                res.put("result_code", "1");
                res.put("result_msg", "更正失败");
                res.put("msg", msg);
                res.put("tts", "更正失败");
            }
            return res;
        }
    }
}
