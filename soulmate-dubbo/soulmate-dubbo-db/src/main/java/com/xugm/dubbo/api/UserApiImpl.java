package com.xugm.dubbo.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xugm.dubbo.mappers.UserMapper;
import com.xugm.model.domain.User;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class UserApiImpl  implements UserApi{

    @Autowired
    private UserMapper userMapper;

    //根据手机号码查询用户
    public User findByMobile(String mobile) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("mobile",mobile);
        return userMapper.selectOne(qw);
    }

    @Override
    public Long save(User user) {
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public void update(User user) {
        userMapper.updateById(user);
    }

    @Override
    public User findById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User findByHuanxin(String huanxinId) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("hx_user",huanxinId);
        return userMapper.selectOne(qw);
    }
}
