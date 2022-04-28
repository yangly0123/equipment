package com.zhonghezhihui.iorg.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义响应结构
 */
public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     * 
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     * Title: jsonToList
     * Description:
     * 
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将json数据转换成对象map
     * Title: jsonToList
     * Description:
     * 
     * @param jsonData
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> convertJSONStringTOMap(String jsonData) {
        if (jsonData == null) {
            return null;
        }
        try {
            Map<String, Object> map = (Map<String, Object>) MAPPER.readValue(jsonData, Map.class);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回 指定的jsonobject格式
     * @author wuxiaohua
     * @param list 数据列表信息
     * @param total 记录总数
     * @return jsonobject
     */
    public static <T> JSONObject getBootstrapTableData(List<T> list, long total) {
        JSONObject results = new JSONObject();
        if (list == null) {
            list = new ArrayList<T>();
        }
        // list 转 json，包含属性为空或null 转化为''
        String str = JSONObject.toJSONString(list, SerializerFeature.WriteMapNullValue).replace("null", "''");
        // 指定row数据
        results.put("rows",JSONArray.parseArray(str));
        // 记录总数
        results.put("total", total);
        return results;
    }

    /**
     * 获取json中所有的key,返回list
     * @author wuxiaohua
     * @date 2021/8/30 10:24
     * @param
     * @return
     **/
    public static List<String> getAllKey(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.isEmpty()) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        jsonObject.forEach((key,value)->{
            list.add(key);
        });
        return list;
    }
}
