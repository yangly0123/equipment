package com.zhonghezhihui.iorg.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
public class CanOrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_0 = 0; //未取餐
    public static final int STATUS_1 = 1; //已取餐
    public static final int STATUS_2 = 2; //已取消

    private Integer id;

    private Integer organizationId;

    private Integer userId;

    private Integer mealId;

    private Integer amount;

    private String mealProducts;

    private String mealDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mealStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mealEnd;

    private String mealEndTime;

    private String mealType;

    private Integer status;

    private String userName;

    private List products;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
