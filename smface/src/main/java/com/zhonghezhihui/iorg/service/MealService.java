package com.zhonghezhihui.iorg.service;

import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.entity.CanOrderEntity;
import com.zhonghezhihui.iorg.entity.CanOrderProductEntity;

import java.util.List;

public interface MealService {

    JSONObject takeMeal(JSONObject data);

    JSONObject takeLok(JSONObject data);

    void updateMealOrder(CanOrderEntity canOrderEntity);

    List<CanOrderProductEntity> queryByOrderId(Integer orderId);
}
