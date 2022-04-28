package com.zhonghezhihui.iorg.service;

import com.zhonghezhihui.iorg.entity.UserEntity;

public interface UserService {

    void updateById(Integer id, Integer balance, Integer allowance, Integer point);


    UserEntity queryById(Integer id);
}
