package com.xugm.dubbo.api;

import com.xugm.model.domain.User;

public interface UserApi {

    //根据手机号码查询用户
    User findByMobile(String mobile);

    //保存用户，返回用户id
    Long save(User user);

    //更新
    void update(User user);

    //根据id查询
    User findById(Long userId);

    //根据环信id查询用户
    User findByHuanxin(String huanxinId);
}
