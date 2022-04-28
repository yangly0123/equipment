package com.zhonghezhihui.iorg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.entity.WhitelistEntity;
import com.zhonghezhihui.iorg.init.InitData;
import com.zhonghezhihui.iorg.service.WhiteListService;
import com.zhonghezhihui.iorg.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class WhiteListServiceImpl implements WhiteListService {

    protected static final Logger logger = LoggerFactory.getLogger(WhiteListServiceImpl.class);

    @Autowired
    InitData initData;

    @Override
    public List<WhitelistEntity> queryBySendDeviceId(String deviceId) {
        List<WhitelistEntity> whitelistEntityList = new ArrayList<>();
        for (WhitelistEntity whitelistEntity : initData.whitelistEntityList) {
            if (whitelistEntity.getDevId().equals(deviceId) && whitelistEntity.getStatus() == 0 && whitelistEntity.getAction() == 0) {
                whitelistEntityList.add(whitelistEntity);
            }
        }
        return whitelistEntityList;
    }

    @Override
    public List<WhitelistEntity> queryByRemoveDeviceId(String deviceId) {
        List<WhitelistEntity> whitelistEntityList = new ArrayList<>();
        for (WhitelistEntity whitelistEntity : initData.removeWhiteEntityList) {
            if (whitelistEntity.getDevId().equals(deviceId) && whitelistEntity.getStatus()!=2 && whitelistEntity.getAction() == 2) {
                whitelistEntityList.add(whitelistEntity);
            }
        }
        return whitelistEntityList;
    }

    @Override
    public WhitelistEntity queryByRemoveId(Integer id) {
        for (WhitelistEntity whitelistEntity : initData.removeWhiteEntityList) {
            if (whitelistEntity.getId().intValue() == id.intValue()) {
                return whitelistEntity;
            }
        }
        return null;
    }

    @Override
    public void updateSendWhiteById(WhitelistEntity whitelistEntity) {
        for (WhitelistEntity whitelistEntity1 : initData.whitelistEntityList) {
            if (whitelistEntity.getId().equals(whitelistEntity1.getId())) {
                if (whitelistEntity.getResultCode() != null) {
                    whitelistEntity1.setResultCode(whitelistEntity.getResultCode());
                }
                if (whitelistEntity.getStatus() != null) {
                    whitelistEntity1.setStatus(whitelistEntity.getStatus());
                }
                if (!StringUtils.isEmpty(whitelistEntity.getLikeId())) {
                    whitelistEntity1.setLikeId(whitelistEntity.getLikeId());
                }
                if (!StringUtils.isEmpty(whitelistEntity.getUrl())) {
                    whitelistEntity1.setUrl(whitelistEntity.getUrl());
                }
            }
        }
    }

    @Override
    public void updateRemoveWhiteById(WhitelistEntity whitelistEntity) {
        for (WhitelistEntity whitelistEntity1 : initData.removeWhiteEntityList) {
            if (whitelistEntity.getId().equals(whitelistEntity1.getId())) {
                if (whitelistEntity.getResultCode() != null) {
                    whitelistEntity1.setResultCode(whitelistEntity.getResultCode());
                }
                whitelistEntity1.setAction(whitelistEntity.getAction());
                whitelistEntity1.setStatus(whitelistEntity.getStatus());
            }
        }
    }

    @Override
    public JSONObject sendWhiteList(String devId, String transactionId) {
        logger.warn("hearbeat add whitelist start ==========================: ");
        JSONObject results = new JSONObject();
        List<WhitelistEntity> whitelistEntityList = queryBySendDeviceId(devId);
        if (CollectionUtils.isEmpty(whitelistEntityList)) {
            return null;
        }

        logger.warn("hearbeat add whitelist list ==========================: " + JSON.toJSONString(whitelistEntityList));

        String api = GlobalConstant.SM_ADD_PERSON;
        String time = ServerUtils.getTimestampSeconds().toString();
        String noncestr = ServerUtils.getRandomString(32).toLowerCase();
        String base = time + noncestr + GlobalConstant.CARD_PWD;
        results.put("api", api);
        results.put("time", time);
        results.put("noncestr", noncestr);
        results.put("interval", GlobalConstant.interval);
        results.put("sign", ServerUtils.computeMd5Sign(base));
        results.put("transaction_id", transactionId);
        List<HashMap<Object, Object>> whitelistMaps = new ArrayList<>();
        int size = whitelistEntityList.size();
        for (int i = 0; i < size; i++) {
            HashMap<Object, Object> whitelistMap = new HashMap<>();
            WhitelistEntity whitelistEntity = whitelistEntityList.get(i);
            whitelistMap.put("rec_id", whitelistEntity.getId().toString());
            whitelistMap.put("account_id", whitelistEntity.getAccountId().toString());
            whitelistMap.put("emp_id", whitelistEntity.getEmpId().toString());
            whitelistMap.put("emp_fname", whitelistEntity.getEmpFname());
            whitelistMap.put("depart_name", whitelistEntity.getDepartName());
            whitelistMap.put("job_name", whitelistEntity.getJobName());
            whitelistMap.put("tel", whitelistEntity.getTel());
            whitelistMap.put("sex", whitelistEntity.getSex());
            whitelistMap.put("birth_date", whitelistEntity.getBirthDate());
            whitelistMap.put("valid_date", whitelistEntity.getValidDate());
            whitelistMap.put("level_id", whitelistEntity.getLevelId().toString());
            whitelistMap.put("card_sn", whitelistEntity.getCardSn());
            whitelistMap.put("door_right", whitelistEntity.getDoorRight());
            whitelistMap.put("url", whitelistEntity.getUrl());
            whitelistMap.put("groups", whitelistEntity.getGroups().toString());
            whitelistMap.put("access_pwd", whitelistEntity.getAccessPwd());
            whitelistMap.put("state", whitelistEntity.getState().toString());
            whitelistMap.put("twins", whitelistEntity.getTwins());
            whitelistMap.put("retain_photo", whitelistEntity.getRetainPhoto().toString());
            whitelistMaps.add(whitelistMap);
        }
        results.put("whitelist", whitelistMaps);
        logger.warn("hearbeat add whitelist results ==========================: " + JSON.toJSONString(results));
        logger.warn("hearbeat add whitelist success ==========================");
        return results;
    }


    @Override
    public JSONObject removeWhiteList(String devId, String transactionId) {
        logger.warn("hearbeat remove whitelist start ==========================: ");
        JSONObject results = new JSONObject();

        List<WhitelistEntity> removeList = queryByRemoveDeviceId(devId);
        if (removeList == null) return null;
        if (removeList.size() == 0) return null;

        logger.warn("hearbeat remove whitelist list ==========================: " + JSON.toJSONString(removeList));

        String api = "delperson";
        String time = ServerUtils.getTimestampSeconds().toString();
        String noncestr = ServerUtils.getRandomString(32).toLowerCase();
        String base = time + noncestr + GlobalConstant.CARD_PWD;
        results.put("api", api);
        results.put("time", time);
        results.put("noncestr", noncestr);
        results.put("interval", GlobalConstant.interval);
        results.put("sign", ServerUtils.computeMd5Sign(base));
        results.put("transaction_id", transactionId);
        ArrayList whitelists = new ArrayList<>();
        int size = removeList.size();
        for (int i = 0; i < size; i++) {
            WhitelistEntity whitelistEntity = removeList.get(i);
            HashMap<Object, Object> whitelist = new HashMap<>();
            whitelist.put("rec_id", whitelistEntity.getId().toString());
            whitelist.put("account_id", whitelistEntity.getAccountId().toString());
            whitelist.put("emp_id", whitelistEntity.getEmpId().toString());
            whitelist.put("only_del_photo", 0);
            whitelists.add(whitelist);

        }
        results.put("whitelist", whitelists);
        logger.warn("hearbeat remove whitelist results ==========================: " + JSON.toJSONString(results));
        logger.warn("hearbeat remove whitelist success ==========================");
        return results;
    }

    @Override
    public void addSendPerson() {
       for (WhitelistEntity whitelistEntity : initData.whitelistEntityList) {
           whitelistEntity.setStatus(0);
           whitelistEntity.setAction(0);
       }
    }
}
