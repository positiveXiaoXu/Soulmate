package com.xugm.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xugm.dubbo.mappers.UserInfoMapper;
import com.tanhua.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@DubboService
public class UserInfoApiImpl implements  UserInfoApi {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public UserInfo findById(Long id) {
        return userInfoMapper.selectById(id);
    }

    @Override
    public Map<Long, UserInfo> findByIds(List<Long> userIds, UserInfo info) {
        QueryWrapper qw = new QueryWrapper();
        //1、用户id列表
        qw.in("id",userIds);
        //2、添加筛选条件
        if(info != null) {
            if(info.getAge() != null) {
                qw.lt("age",info.getAge());
            }
            if(!StringUtils.isEmpty(info.getGender())) {
                qw.eq("gender",info.getGender());
            }
            if(!StringUtils.isEmpty(info.getNickname())) {
               qw.like("nickname",info.getNickname());
            }
        }
        List<UserInfo> list = userInfoMapper.selectList(qw);
        Map<Long, UserInfo> map = CollUtil.fieldValueMap(list, "id");
        return map;
    }

    @Override
    public IPage findAll(Integer page, Integer pagesize) {
        return userInfoMapper.selectPage(new Page<UserInfo>(page,pagesize),null);
    }
}
