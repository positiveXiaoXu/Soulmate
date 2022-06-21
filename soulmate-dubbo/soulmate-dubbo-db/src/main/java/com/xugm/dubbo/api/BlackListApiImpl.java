package com.xugm.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xugm.dubbo.mappers.BlackListMapper;
import com.xugm.dubbo.mappers.UserInfoMapper;
import com.xugm.model.domain.BlackList;
import com.xugm.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class BlackListApiImpl implements BlackListApi{

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private BlackListMapper blackListMapper;

    @Override
    public IPage<UserInfo> findByUserId(Long userId, int page, int size) {
        //1、构建分页参数对象Page
        Page pages = new Page(page,size);
        //2、调用方法分页（自定义编写 分页参数Page，sql条件参数）
        return userInfoMapper.findBlackList(pages,userId);
    }

    @Override
    public void delete(Long userId, Long blackUserId) {
        QueryWrapper<BlackList> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);
        qw.eq("black_user_id",blackUserId);
        blackListMapper.delete(qw);
    }
}
