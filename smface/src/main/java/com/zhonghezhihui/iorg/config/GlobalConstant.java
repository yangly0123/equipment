package com.zhonghezhihui.iorg.config;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalConstant {


    public static final Integer interval = 1000 * 60 * 1;
    public static final String dev_id = "100";
    public static final String CARD_PWD = "FFFFFFFFFFFF";
    public static final String SM_HEARTBEAT_API = "heartbeat";
    public static final String SM_ADD_PERSON = "addperson";

    public static final Long TOLERANCE_TIME = 1000 * 60 * 3L;
    public static final String IMAGE_PROFILE = "D:\\wenjian\\";
    public static final String API_HOST = "http://localhost:5002";

    public static final AtomicInteger DEVICE_SETTINGS_RELOAD = new AtomicInteger(0); // 0表示不需要同步， 1表示需要同步
    public static Integer WHITELIST_REMOVE = 0;

    public static Integer CORRECTIONS = 100;

    // MODEL 0
    public static final String DEVICE_SETTINGS_MODEL_0 = "{\n" +
            "\t\"dev_set\": [{\n" +
            "\t\t\"key\": \"title\",\n" +
            "\t\t\"value\": \"智慧消费机\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"consume_type\",\n" +
            "\t\t\"value\": \"0\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"qrcode_switch\",\n" +
            "\t\t\"value2\": true,\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"qr_code_pass\",\n" +
            "\t\t\"value2\": true,\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"admin_mend_password1\",\n" +
            "\t\t\"value\": \"1111\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"fix_money\",\n" +
            "\t\t\"value\": 1000,\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}]\n" +
            "}";


    // MODEL 1
    public static final String DEVICE_SETTINGS_MODEL_1 = "{\n" +
            "\t\"dev_set\": [{\n" +
            "\t\t\"key\": \"title\",\n" +
            "\t\t\"value\": \"智慧消费机\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"consume_type\",\n" +
            "\t\t\"value\": \"1\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"qrcode_switch\",\n" +
            "\t\t\"value2\": true,\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"qr_code_pass\",\n" +
            "\t\t\"value2\": true,\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"admin_mend_password1\",\n" +
            "\t\t\"value\": \"1111\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"fix_money\",\n" +
            "\t\t\"value\": 1200,\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}]\n" +
            "}";

    // MODEL 2
    public static final String DEVICE_SETTINGS_MODEL_2 = "{\n" +
            "\t\"dev_set\": [{\n" +
            "\t\t\"key\": \"title\",\n" +
            "\t\t\"value\": \"智慧消费机\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"consume_type\",\n" +
            "\t\t\"value\": \"2\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"qrcode_switch\",\n" +
            "\t\t\"value2\": true,\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"qr_code_pass\",\n" +
            "\t\t\"value2\": true,\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"admin_mend_password1\",\n" +
            "\t\t\"value\": \"1111\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}, {\n" +
            "\t\t\"key\": \"fix_money\",\n" +
            "\t\t\"value\": 1200,\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}],\n" +
            "\t\"time_period\": [{\n" +
            "\t\t\"id\": \"0\",\n" +
            "\t\t\"start_time\": \"07:00:00\",\n" +
            "\t\t\"end_time\": \"09:00:00\",\n" +
            "\t\t\"use_mode\": \"00\",\n" +
            "\t\t\"tmrtype\": \"\",\n" +
            "\t\t\"mark\": \"\",\n" +
            "\t\t\"level\": \"0\",\n" +
            "\t\t\"maxcount\": \"1\",\n" +
            "\t\t\"fixmoney\": 500,\n" +
            "\t\t\"consume_type\": \"1\",\n" +
            "\t\t\"param_id\": \"000000\"\n" +
            "\t}]\n" +
            "}";

}
