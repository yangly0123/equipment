package com.zhonghezhihui.iorg.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListUtils {

    /**
    * list数据排序
    * @author wuxiaohua
    * @date 2021/8/30 10:35
    * @param paramList
    * @param sortType：true：降序，false：升序
     * @return 
    **/
    public static List<String> listSort(List<String> paramList, boolean sortType){
        // list排序
        Collections.sort(paramList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // 返回相反的compare
                if(sortType) {
                    return o2.compareTo(o1);
                } else {
                    return o1.compareTo(o2);
                }
            }
        });
        return paramList;
    }
}
