package com.zhonghezhihui.iorg.jmeter;


import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/jmeter")
public class HelloController {


    @RequestMapping("hello")
    public JSONObject hello() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("data", "hello");
        return jsonObject;
    }
}
