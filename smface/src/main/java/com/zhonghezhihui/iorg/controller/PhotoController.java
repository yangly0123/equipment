package com.zhonghezhihui.iorg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.service.WhiteListService;
import com.zhonghezhihui.iorg.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class PhotoController {

    protected static final Logger logger = LoggerFactory.getLogger(PhotoController.class);

    @Autowired
    WhiteListService whiteListService;

    /**
     * @param data
     * @return
     */
    @PostMapping("takephoto")
    public JSONObject takephoto(@RequestBody JSONObject data) {
        try {
            logger.warn("松美消费机 iorg-smface receive equipment upload  person face image, start ============================");
            String account_id = data.getString("account_id");
            Integer accountId = Integer.parseInt(account_id, 10);
            logger.warn("松美消费机 iorg-smface receive equipment upload  person face image user id ============================ :" + accountId);
            String base64Str = data.getString("photo");
            String imgName = ServerUtils.getCustomerUuid() + ".jpeg";
            String imgFilePath = GlobalConstant.IMAGE_PROFILE + imgName;
            logger.warn("松美消费机 iorg-smface receive equipment upload  person face image, imgFilePath ============================ :" + imgFilePath);
            boolean b = ServerUtils.GenerateImage(base64Str, imgFilePath);
            if (!b) throw new Exception("保存图片失败！");
            String url = imgName;
            JSONObject result = new JSONObject();
            Long time = ServerUtils.getTimestampSeconds();
            String noncestr = ServerUtils.getRandomString(32);
            String baseStr = time + noncestr + GlobalConstant.CARD_PWD;
            String sign = ServerUtils.computeMd5Sign(baseStr);

            logger.warn("松美消费机 iorg-smface update user face url =============================: " + GlobalConstant.API_HOST + url);

            result.put("api", "takephoto");
            result.put("time", time.toString());
            result.put("noncestr", noncestr);
            result.put("interval", GlobalConstant.interval);
            result.put("sign", sign);
            result.put("result_code", "0");
            result.put("result_msg", "OK");

            logger.warn("松美消费机 iorg-smface receive equipment upload  person face image success, result  ============================ :" + JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            logger.warn("松美消费机 iorg-smface receive equipment upload  person face image failed ============================");
            e.printStackTrace();
            return null;
        }

    }
}
