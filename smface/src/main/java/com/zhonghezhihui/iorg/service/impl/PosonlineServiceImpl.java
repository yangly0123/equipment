package com.zhonghezhihui.iorg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.entity.UserEntity;
import com.zhonghezhihui.iorg.init.InitData;
import com.zhonghezhihui.iorg.service.PosonlineService;
import com.zhonghezhihui.iorg.service.UserService;
import com.zhonghezhihui.iorg.util.DigestUtil;
import com.zhonghezhihui.iorg.util.HttpClientUtil;
import com.zhonghezhihui.iorg.util.Result;
import com.zhonghezhihui.iorg.util.ServerUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PosonlineServiceImpl implements PosonlineService {

    protected static final Logger logger = LoggerFactory.getLogger(PosonlineServiceImpl.class);

    @Autowired
    InitData initData;

    @Autowired
    UserService userService;

    @Override
    public JSONObject posonline(JSONObject data) {
        JSONObject res = new JSONObject();
        ArrayList<JSONObject> msg = new ArrayList<>();
        try {
            String api = data.getString("api");
            String transaction_id = data.getString("transaction_id");
            String devId = data.getString("dev_id");
            String serialno = data.getString("serialno");
            JSONObject params = data.getJSONObject("params");
            String payMode = params.getString("pay_mode");
            String signTime = params.getString("sign_time");
            String consumeMode = params.getString("consume_mode");
            String consume_money = params.getString("amount");
            Integer consumeMoney = Integer.parseInt(consume_money, 10);
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

            Integer accountId = 0;
            //pay_mode 0 人脸  1云卡（算法需要时提供）  2卡序列号  3取餐码（算法需要时提供）
            // 扫码消费(不是二维码取餐)
            if (payMode.equals("1")) {
                handleScancode(res, params, msg, consumeMoney, devId, accountId, noncestr, tradeId, time);
            } else {
                //刷脸消费
                String payCode = params.getString("pay_code");
                accountId = Integer.parseInt(payCode, 10);
            }

            // 处理时段消费逻辑
            String settings = initData.deviceEntityList.get(0).getSettings();
            if (StringUtils.isNotEmpty(settings)) {
               handleDeviceSettings();
            }

            Integer empId = accountId;
            int point = consumeMoney / 100;
            logger.warn("iorg-smface update user point ================================ add point: " + point);
            logger.warn("iorg-smface update user allowance =================================");
            logger.warn("iorg-smface update user balance ================================= ");
            UserEntity userEntity = initData.userEntity;
            userEntity.setAllowance(userEntity.getAllowance() - consumeMoney);
            userEntity.setBalance(userEntity.getBalance() - 0);
            userEntity.setPoints(userEntity.getPoints() + point);
            userService.updateById(empId, 0, consumeMoney, point);
            logger.warn("iorg-smface add user consume record detailed ======================================");
            logger.warn("iorg-smface add user pay record detailed ======================================");

            String userName = initData.userEntity.getName();
            String dataBaseStr = accountId.toString() + empId.toString() + userName + consumeMoney.toString() + tradeId + time
                    + noncestr + GlobalConstant.CARD_PWD;
            String dataSign = ServerUtils.computeMd5Sign(dataBaseStr);
            ArrayList<JSONObject> result = new ArrayList<>();
            JSONObject resData = new JSONObject();
            resData.put("account_id", accountId);
            resData.put("emp_id", empId.toString());
            resData.put("emp_fname", userName);
            resData.put("consume_money", consume_money);
            resData.put("trade_id", tradeId);
            resData.put("sign", dataSign);
            result.add(resData);
            res.put("result", result);
            JSONObject line1 = new JSONObject();
            line1.put("line", "姓名:" + userName);
            JSONObject line2 = new JSONObject();
            line2.put("line", "消费:" + ServerUtils.computeMoney(consumeMoney) + "元");
            JSONObject line3 = new JSONObject();
            line3.put("line", "余额:" + ServerUtils.computeMoney(initData.userEntity.getAllowance()) + "元");
            msg.add(line1);
            msg.add(line2);
            msg.add(line3);
            res.put("result_code", "0");
            res.put("result_msg", "消费成功");
            res.put("msg", msg);
            res.put("tts", "消费:" + ServerUtils.computeMoney(consumeMoney) + "元");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            if (res.getString("result_code") == null) {
                JSONObject line = new JSONObject();
                line.put("line", "消费失败");
                msg.add(line);
                res.put("result_code", "1");
                res.put("result_msg", "消费失败");
                res.put("msg", msg);
                res.put("tts", "消费失败");
            }
            return res;
        }
    }

    private void handleDeviceSettings(){

    }

    private JSONObject handleScancode(JSONObject res, JSONObject params, ArrayList<JSONObject> msg, Integer consumeMoney
            , String devId, Integer accountId, String noncestr, String tradeId, Long time) throws Exception {
        String qrCode = params.getString("qr_code");
        logger.warn("iorg-smface real posonline qrCode =============================: " + qrCode);
        if (qrCode.length() == 19) {
            JSONObject scanParams = new JSONObject();
            scanParams.put("amount", consumeMoney);
            scanParams.put("qrCode", qrCode);
            scanParams.put("devId", devId);
            String url = GlobalConstant.API_HOST + "/icbc/qrcode/pay";
            String s = HttpClientUtil.doPostJson(url, scanParams.toString());
            Result qrcodePayRes = JSON.parseObject(s, Result.class);
            System.out.println("qrcodePayRes = " + qrcodePayRes);
            Integer code = qrcodePayRes.getCode();
            if (code.equals(0)) {
                JSONObject data1 = (JSONObject) qrcodePayRes.getData();
                accountId = data1.getInteger("userId");
                Integer returnCode = data1.getInteger("returnCode");
                Integer payStatus = data1.getInteger("payStatus");
                String returnMsg = data1.getString("returnMsg");
                if (!returnCode.equals(0) || !payStatus.equals(1)) {
                    res.put("result_code", "1");
                    res.put("result_msg", returnMsg);
                    JSONObject line1 = new JSONObject();
                    line1.put("line", "消费失败");
                    JSONObject line2 = new JSONObject();
                    line2.put("line", returnMsg);
                    msg.add(line1);
                    msg.add(line2);
                    res.put("msg", msg);
                    res.put("tts", "消费失败");
                    throw new Exception();
                }


                int point = consumeMoney / 100;
                logger.warn("iorg-smface update user point ================================ add point: " + point);
                logger.warn("iorg-smface update user balance ================================ consume money: " + consumeMoney);
                logger.warn("iorg-smface update user allowance ================================ allowance: " + consumeMoney);

                logger.warn("iorg-smface add user consume record detailed ======================================");
                logger.warn("iorg-smface add user pay record detailed ======================================");

                String userName = initData.userEntity.getName();
                String dataBaseStr = accountId.toString() + accountId.toString() + userName + consumeMoney.toString() + tradeId + time
                        + noncestr + GlobalConstant.CARD_PWD;
                String dataSign = ServerUtils.computeMd5Sign(dataBaseStr);
                ArrayList<JSONObject> result = new ArrayList<>();
                JSONObject resData = new JSONObject();
                resData.put("account_id", accountId);
                resData.put("emp_id", accountId.toString());
                resData.put("emp_fname", userName);
                resData.put("consume_money", consumeMoney);
                resData.put("trade_id", tradeId);
                resData.put("sign", dataSign);
                result.add(resData);
                res.put("result", result);
                JSONObject line1 = new JSONObject();
                line1.put("line", "姓名:" + userName);
                JSONObject line2 = new JSONObject();
                line2.put("line", "付款方式: 工行APP扫码");
                JSONObject line3 = new JSONObject();
                line3.put("line", "消费:" + ServerUtils.computeMoney(consumeMoney) + "元");
                msg.add(line1);
                msg.add(line2);
                msg.add(line3);
                res.put("result_code", "0");
                res.put("result_msg", "消费成功");
                res.put("msg", msg);
                res.put("tts", "消费:" + ServerUtils.computeMoney(consumeMoney) + "元");
                return res;
            } else {
                String returnMsg = qrcodePayRes.getMessage();
                res.put("result_code", "1");
                res.put("result_msg", returnMsg);
                JSONObject line1 = new JSONObject();
                line1.put("line", "消费失败");
                JSONObject line2 = new JSONObject();
                line2.put("line", returnMsg);
                msg.add(line1);
                msg.add(line2);
                res.put("msg", msg);
                res.put("tts", "消费失败");
                throw new Exception();
            }
        } else {
            JSONObject jsonObject = JSON.parseObject(qrCode, JSONObject.class);
            if (!DigestUtil.validateDigestHttp(jsonObject)) {
                JSONObject line = new JSONObject();
                line.put("line", "无效的付款码");
                msg.add(line);
                res.put("result_code", "1");
                res.put("result_msg", "无效的付款码");
                res.put("msg", msg);
                res.put("tts", "无效的付款码");
                throw new Exception();
            }
            accountId = jsonObject.getInteger("payCode");
            long expireIn = jsonObject.getLong("expireIn");
            long timestamp = jsonObject.getLong("timestamp");
            long expireTime = timestamp + expireIn;
            long timestampSeconds = ServerUtils.getTimestampSeconds();
            if (expireTime < timestampSeconds) {
                JSONObject line = new JSONObject();
                line.put("line", "付款码已过期");
                res.put("result_msg", "付款码已过期");
                msg.add(line);
                res.put("result_code", "1");
                res.put("msg", msg);
                res.put("tts", "付款码已过期");
                throw new Exception();
            }
        }
        return res;
    }
}
