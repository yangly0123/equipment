package com.zhonghezhihui.iorg.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 组织ID
     */
    private Integer organizationId;

    private String name;

    private Integer role;

    /**
     * 员工编号
     */
    private String employeeNo;

    private String email;

    private String photo;

    /**
     * 1 女， 2 男, 0 未知
     */
    private Integer gender;


    /**
     * 电话，仅10.1寸去向牌门禁机有效
     */
    private String phone;


    /**
     *  账户余额（单位分）
     */
    private Integer balance;

    /**
     * 补助金额（单位分）
     */
    private Integer allowance;

    /**
     * 积分（美消费1元积1分）
     */
    private Integer points;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Integer deleted;

}
