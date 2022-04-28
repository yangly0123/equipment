package com.zhonghezhihui.iorg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  smf whitelist
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WhitelistEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 设备ID
     */
    private String devId;

    /**
     * 帐号
     */
    private Integer accountId;

    /**
     * 工号
     */
    private Integer empId;

    /**
     * 姓名
     */
    private String empFname;

    /**
     * 部门
     */
    private String departName;

    /**
     * //职务，仅10.1寸去向牌门禁机有效
     */
    private String jobName;

    /**
     * //电话，仅10.1寸去向牌门禁机有效
     */
    private String tel;

    /**
     * //性别，可为空字符
     */
    private String sex;

    /**
     * //出生日期，可为空字符
     */
    private String birthDate;

    /**
     * //有效日期，必填
     */
    private String validDate;

    /**
     * //级别，整数，必须是整数，必填
     */
    private Integer levelId;

    /**
     * //卡序列号，整数，没有时，用空字符，有卡号时前面不能有0
     */
    private String cardSn;

    /**
     * //门权限, 预留，没有时用空字符
     */
    private String doorRight;

    /**
     * //个人相片的url地址，可为空字符，表示无相片
     */
    private String url;

    /**
     * //组别为第3组，长整型，二进制时从最低位开始为1组，0表示无，1表示有，例如第3组的二进制表示0000100，转换为十进制则是4，第3组的groups为4，必填
     */
    private Long groups;

    /**
     * //个人密码，整数，4位，不足补0，必填
     */
    private String accessPwd;

    /**
     * //整数，状态：0正常  2挂失（只有刷卡时才会判断，人脸不判断，另离线时才判断，在线由平台判断），为整数，不能非整数，必填
     */
    private Integer state;

    /**
     * //固定为空，必填
     */
    private String twins;

    /**
     * //整数，下载个人相片处理方式， 仅url为空时才有效， = 0默认删除相片 、 1保留相片还要识别，必填，无相片时统一此值用0，当url有值时按url的值处理
     */
    private Integer retainPhoto;

    /**
     * 名单处理结果状态
     */
    private Integer resultCode;

    /**
     * 当返回人脸已存在时，这里显示已存在的其它工号，分号隔开
     */
    private String likeId;

    /**
     * 同步状态，0: 未同步 1: 已同步
     */
    private Integer status;

    /**
     * 行为 0:添加 2:删除 4:清空消费机名单
     */
    private Integer action;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
