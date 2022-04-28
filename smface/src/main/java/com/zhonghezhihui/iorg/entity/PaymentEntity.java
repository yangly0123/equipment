package com.zhonghezhihui.iorg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int CHANNEL_1 = 1; //消费机
    public static final int CHANNEL_2 = 2; //订餐

    public static final int PAYTYPE_1 = 1; //余额
    public static final int PAYTYPE_2 = 2; //工行APP扫码

    public static final int DEVTYPE_1 = 1; //松美人脸消费机

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 组织ID
     */
    private Integer organizationId;

    private Integer userId;

    /**
     * 原价 单位分
     */
    private Integer originalCost;


    /**
     * 减免金额 单位分
     */
    private Integer reduceCost;

    /**
     * 金额 单位分
     */
    private Integer amount;

    /**
     * 使用余额、扣减账户余额
     */
    private Integer useBalance;

    /**
     * 使用补助
     */
    private Integer useAllowance;

    /**
     * 当前时间账户余额
     */
    private Integer balance;

    /**
     * 当前时间账户补助
     */
    private Integer allowance;

    /**
     * 积分 1元1分
     */
    private Integer point;

    /**
     * 通道 1: 消费机，2: 订餐
     */
    private Integer channel;

    /**
     * 付款方式 1: 余额  2: 工行APP扫码
     */
    private Integer payType;

    /**
     * 消费机机号
     */
    private Integer devType;

    /**
     * 消费机型号 1: 松美人脸消费机
     */
    private String devId;

    private Integer orderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

}
