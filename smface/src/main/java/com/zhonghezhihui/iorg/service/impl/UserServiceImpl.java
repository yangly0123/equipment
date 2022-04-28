package com.zhonghezhihui.iorg.service.impl;

import com.zhonghezhihui.iorg.entity.UserEntity;
import com.zhonghezhihui.iorg.init.InitData;
import com.zhonghezhihui.iorg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    InitData initData;

    @Override
    public void updateById(Integer id, Integer balance, Integer allowance, Integer point) {

    }

    @Override
    public UserEntity queryById(Integer id) {
        return initData.userEntity;
    }
}
