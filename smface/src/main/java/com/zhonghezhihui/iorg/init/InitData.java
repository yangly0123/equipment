package com.zhonghezhihui.iorg.init;

import com.zhonghezhihui.iorg.config.GlobalConstant;
import com.zhonghezhihui.iorg.entity.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitData {

    public List<WhitelistEntity> whitelistEntityList =new ArrayList<>();

    public List<WhitelistEntity> removeWhiteEntityList = new ArrayList<>();

    public List<DeviceEntity> deviceEntityList = new ArrayList<>();

    public UserEntity userEntity = new UserEntity();

    public PaymentEntity paymentEntity = new PaymentEntity();

    public CanOrderEntity canOrderEntity = new CanOrderEntity();

    public List<CanOrderProductEntity> canOrderProductEntities = new ArrayList<>();

    @PostConstruct
    public void initWhiteList() {
        // 模拟web点击同步按钮同步用户名单， 此处模拟数据库存放数据
        WhitelistEntity whiteEntity1 = produceWhiteEntity();
        whitelistEntityList.add(whiteEntity1);

    }

    @PostConstruct
    public void initUserEntity() {
        userEntity = produceUserEntity();
    }

    @PostConstruct
    public void initPaymentEntity() {
        paymentEntity = producePaymentEntity();
    }

    @PostConstruct
    public void initMealOrder() {
        canOrderEntity = procudeCanOrderEntity();
        CanOrderProductEntity canOrderProductEntity = produceCanOrderProductEntity();
        canOrderProductEntities.add(canOrderProductEntity);
    }

    @PostConstruct
    public void initRemoveWhiteList() {
        WhitelistEntity removeWhiteEntity = produceWhiteEntity();
        removeWhiteEntity.setStatus(0);
        removeWhiteEntity.setAction(2);
        removeWhiteEntityList.add(removeWhiteEntity);

    }

   @PostConstruct
    public void initDeviceList() {
        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.setId(1);
        deviceEntity.setDevId(GlobalConstant.dev_id);
        deviceEntity.setOrganizationId(34);
        deviceEntity.setModel("5");
        deviceEntity.setSerialno("L33C2FAO5L");
        deviceEntity.setSettings("");
        deviceEntity.setReload(0);
        deviceEntity.setLastHeartbeatTime(LocalDateTime.of(2022, Month.APRIL, 3, 1, 1));
        deviceEntityList.add(deviceEntity);
    }


    public WhitelistEntity produceWhiteEntity() {
        WhitelistEntity originWhitelistEntity = new WhitelistEntity();
        originWhitelistEntity.setId(1);
        originWhitelistEntity.setDevId(GlobalConstant.dev_id);
        originWhitelistEntity.setAccountId(511);
        originWhitelistEntity.setEmpId(1);
        originWhitelistEntity.setEmpFname("Tom");
        originWhitelistEntity.setDepartName("");
        originWhitelistEntity.setTel("15010053668");
        originWhitelistEntity.setSex("男");
        originWhitelistEntity.setBirthDate("");
        originWhitelistEntity.setValidDate("2099-01-01");
        originWhitelistEntity.setLevelId(0);
        originWhitelistEntity.setCardSn("");
        originWhitelistEntity.setDoorRight("");
        originWhitelistEntity.setUrl("http://127.0.0.1:5002/image?name=/4c287d75d2874060be012e22d56f6c49.jpeg");
        originWhitelistEntity.setGroups(4L);
        originWhitelistEntity.setAccessPwd("");
        originWhitelistEntity.setState(0);
        originWhitelistEntity.setTwins("");
        originWhitelistEntity.setRetainPhoto(0);
        originWhitelistEntity.setAction(0);
        originWhitelistEntity.setStatus(0);// 0 表示未同步，1表示已同步

        return originWhitelistEntity;
    }


    public UserEntity produceUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(511);
        userEntity.setName("Tom");
        userEntity.setBalance(0);
        userEntity.setAllowance(2000);
        userEntity.setPoints(0);
        return userEntity;
    }

    public PaymentEntity producePaymentEntity() {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setId(1);
        paymentEntity.setOrderId(1);
        paymentEntity.setAllowance(100);
        paymentEntity.setBalance(0);
        paymentEntity.setPoint(10);
        paymentEntity.setAmount(0);
        return paymentEntity;
    }


    public CanOrderEntity procudeCanOrderEntity() {
        CanOrderEntity canOrderEntity = new CanOrderEntity();
        canOrderEntity.setId(1);
        canOrderEntity.setStatus(0);
        return canOrderEntity;
    }

    public CanOrderProductEntity produceCanOrderProductEntity() {
        CanOrderProductEntity canOrderProductEntity = new CanOrderProductEntity();
        canOrderProductEntity.setName("西红柿");
        canOrderProductEntity.setNum(1);
        return canOrderProductEntity;
    }
}
