package com.zhonghezhihui.iorg.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.entity.WhitelistEntity;
import com.zhonghezhihui.iorg.service.WhiteListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/request")
public class WhiteListController {

    protected static final Logger logger = LoggerFactory.getLogger(WhiteListController.class);

    @Autowired
    WhiteListService whiteListService;

    /**
     * 设备主动上报下发名单保存的状态结果，方便服务器对数据库做标记, 主要用于标记
     * @param data
     * @return
     */
    @PostMapping("addperson")
    public JSONObject addperson(@RequestBody JSONObject data) {
        try {
            logger.warn("松美消费机 iorg-smface addperson start ============================");
            String devId = data.getString("dev_id");
            String transactionId = data.getString("transaction_id");

            JSONArray whitelists = data.getJSONArray("whitelist");
            List<JSONObject> list = whitelists.toJavaList(JSONObject.class);
            for (int i = 0; i < list.size(); i++) {
                JSONObject item = list.get(i);
                int resultCode = Integer.parseInt(item.getString("result_code"), 10);
                String likeId = item.getString("like_id");

                Integer id = Integer.parseInt(item.getString("rec_id"), 10);
                WhitelistEntity whitelistEntity = new WhitelistEntity();
                whitelistEntity.setId(id);
                whitelistEntity.setResultCode(resultCode);
                if (likeId != null) whitelistEntity.setLikeId(likeId);
                whitelistEntity.setStatus(1);
                whiteListService.updateSendWhiteById(whitelistEntity);
            }
            //可以接着继续下发人员名单
            JSONObject result = whiteListService.sendWhiteList(devId, transactionId);
            return result;
        } catch (Exception e) {
            logger.error("addperson: " + e.getMessage());
            return null;
        }
    }


    @PostMapping("delperson")
    public JSONObject delperson(@RequestBody JSONObject data) {
        try {
            logger.warn("松美消费机 iorg-smface delperson start ============================");
            String devId = data.getString("dev_id");
            String transactionId = data.getString("transaction_id");
            JSONArray whitelists = data.getJSONArray("whitelist");
            List<JSONObject> list = whitelists.toJavaList(JSONObject.class);
            for (int i = 0; i < list.size(); i++) {
                JSONObject item = list.get(i);
                Integer resultCode = Integer.parseInt(item.getString("result_code"), 10);
                Integer id = Integer.parseInt(item.getString("rec_id"), 10);
                WhitelistEntity whitelist = whiteListService.queryByRemoveId(id);
                if (whitelist == null) continue;
                whitelist.setResultCode(resultCode);
                whitelist.setStatus(2);
                whitelist.setAction(2);
                whiteListService.updateRemoveWhiteById(whitelist);
            }
            JSONObject result = whiteListService.removeWhiteList(devId, transactionId);
            GlobalConstant.WHITELIST_REMOVE = 0;
            return result;
        } catch (Exception e) {
            logger.error("delperson: " + e.getMessage());
            return null;
        }
    }
}
