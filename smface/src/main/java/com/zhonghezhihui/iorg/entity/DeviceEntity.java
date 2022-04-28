package com.zhonghezhihui.iorg.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 机号，需要填写到设备上
     */
    private String devId;

    /**
     * 组织ID
     */
    private Integer organizationId;

    /**
     * 设备的MAC地址
     */
    private String mac;

    private String ip;

    /**
     * 固件版本号
     */
    private String fireware;

    /**
     * 软件版本号
     */
    private String version;

    /**
     * 机型，1考勤机，2门禁机，5消费机，6充值
     */
    private String model;

    /**
     * 子功能，1去向牌门禁机，2会议人脸门禁，3会议看板
     */
    private String subModel;

    /**
     * 设备序列号，有的机型传出此值
     */
    private String serialno;

    /**
     * 设备时间，unix时间毫秒
     */
    private String time;

    /**
     * 客户标识，默认为空
     */
    private String customer;

    /**
     * 门状态，0开  1关，以下同
     */
    private String doorState;

    /**
     * 当前名单数量
     */
    private Integer personCount;

    /**
     * 当前人脸数量
     */
    private Integer faceCount;

    /**
     * 未上传的记录数量
     */
    private Integer total;

    /**
     * APP编译时间
     */
    private String buildTime;

    /**
     * 钱包扣款模式：0先消费补贴再个人，1仅现金，2仅补贴
     */
    private String walletConsumeMode;

    /**
     * //0="谢谢", 1="播报消费金额", 2="姓名+谢谢",3= "姓名+金额"
     */
    private Integer consumeFinishVoiceType;

    /**
     * 设置
     */
    private String settings;

    /**
     * 是否需要重载设置
     */
    private Integer reload;

    /**
     * 最近一次收到设备心跳时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastHeartbeatTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Integer deleted;

}
