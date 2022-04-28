package com.zhonghezhihui.iorg.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.service.DeviceService;
import com.zhonghezhihui.iorg.service.WhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @Autowired
    WhiteListService whiteListService;

    /**
     *
     * @param model  0 单价； 1 定额； 2 时段模式
     * @return
     */
    @GetMapping("/device/set")
    public JSONObject dataSet(@RequestParam Integer model) {
        GlobalConstant.DEVICE_SETTINGS_RELOAD.set(0);
        deviceService.setModel(model);
        JSONObject data = new JSONObject();
        data.put("msg", "dataSet");
        return data;
    }


    @GetMapping("/whitelist/remove")
    public JSONObject whitelistRemove() {
        GlobalConstant.WHITELIST_REMOVE = 1;
        JSONObject data = new JSONObject();
        data.put("msg", "whitelistRemove");
        return data;
    }

    @GetMapping("/whitelist/add")
    public JSONObject whitelistAdd() {
        whiteListService.addSendPerson();
        JSONObject data = new JSONObject();
        data.put("msg", "whitelistAdd");
        return data;
    }
}
