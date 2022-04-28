package com.zhonghezhihui.iorg.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.service.CorrectonlineService;
import com.zhonghezhihui.iorg.service.InfoQueryService;
import com.zhonghezhihui.iorg.service.MealService;
import com.zhonghezhihui.iorg.service.PosonlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class RealController {

    protected static final Logger logger = LoggerFactory.getLogger(RealController.class);

    @Autowired
    InfoQueryService infoQueryService;

    @Autowired
    PosonlineService posonlineService;

    @Autowired
    CorrectonlineService correctonlineService;

    @Autowired
    MealService mealService;

    @PostMapping("real")
    public JSONObject real(@RequestBody JSONObject data) {
        String api = data.getString("api");
        String time = data.getString("time");
        String noncestr = data.getString("noncestr");
        String sign = data.getString("sign");
        try {
            switch (api) {
                // include balance、allowance money
                case "infoquery":
                    logger.warn("松美消费机 iorg-smface real infoquery start =================================");
                    return infoQueryService.infoQuery(data);
                //Face consumption Click to confirm
                case "posonline":
                    logger.warn("松美消费机 iorg-smface real posonline start =================================");
                    return posonlineService.posonline(data);
                //Correct the last consumption record
                case "correctonline":
                    logger.warn("松美消费机 iorg-smface real correctonline start =================================");
                    return correctonlineService.correctonline(data);
                // Order meals online, brush your face and query the details of ordering dishes
                case "takemeal":
                    logger.warn("松美消费机 iorg-smface real takemeal start =================================");
                    return mealService.takeMeal(data);
                //Order meals online,Click OK to pick up the meal
                case "takemealok":
                    logger.warn("松美消费机 iorg-smface real takemealok start =================================");
                    return mealService.takeLok(data);
                default:
                logger.error("松美消费机 iorg-smface real failed =================================");
                throw new IllegalStateException("松美消费机 iorg-smface real failed, api ======================================: " + api);
            }
        } catch (Exception e) {
            logger.error("real: " + e.getMessage());
            JSONObject res = new JSONObject();
            res.put("api", api);
            res.put("result_code", "1");
            res.put("result_msg", "支付失败");
            return res;
        }
    }

}
