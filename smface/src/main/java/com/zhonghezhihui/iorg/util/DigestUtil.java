package com.zhonghezhihui.iorg.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

@Slf4j
public class DigestUtil implements Serializable {
    public static final String UTF8 = "UTF-8";
    private static final String KEY = "YzJlYzA0ZjFlMzkxNmFhZjY0MmM1ZmNjOTc3MjAxMDk=";

    private static class SingletonHolder {
        private static DigestUtil digestUtil = new DigestUtil();
    }

    private DigestUtil() {
    }

    public static DigestUtil getInstance() {
        return SingletonHolder.digestUtil;
    }

    /**
     * base64
     *
     * @param md5
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] md5) throws Exception {
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(md5);
    }

    /**
     * 摘要生成
     *
     * @param data
     *            请求数据
     *            签名秘钥(KEY)
     *            编码格式 UTF-8
     * @return 摘要
     * @throws Exception
     */
    public static String digest(JSONObject data) throws Exception {
        String nonceStr = createNonceStr(data);
        return encryptBASE64(DigestUtils.md5DigestAsHex((nonceStr + KEY).getBytes(UTF8)).getBytes(UTF8));
    }

    public static boolean validateDigestHttp(JSONObject data) {
        try {
            if (data == null) return false;
            String sign = data.getString("sign");
            if (StringUtils.isEmpty(sign)) return false;
            String digestTemp = digest(data).trim();
            log.debug("digestTemp = " + digestTemp);
            if (sign.trim().equals(digestTemp)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 组合签名源字符串
     * @param data
     * @return
     */
    public static String createNonceStr (JSONObject data) {
        List<String> list = JsonUtils.getAllKey(data);
        List<String> strings = ListUtils.listSort(list, false);
        String nonceStr = "";
        for (int i = 0; i < strings.size(); i++) {
            String key = strings.get(i);
            if (key.equals("sign")) continue;
            if (data.get(key) instanceof String) {
                nonceStr = nonceStr + data.getString(key);
            } else if (data.get(key) instanceof Integer) {
                nonceStr = nonceStr + data.getInteger(key).toString();
            } else if (data.get(key) instanceof Long) {
                nonceStr = nonceStr + data.getLong(key).toString();
            }
        }
        return nonceStr;
    }
}
