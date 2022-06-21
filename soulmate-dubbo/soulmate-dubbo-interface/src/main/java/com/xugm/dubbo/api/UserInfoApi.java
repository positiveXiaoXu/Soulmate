package com.xugm.dubbo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xugm.model.domain.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoApi {

    public void save(UserInfo userInfo);

    public void update(UserInfo userInfo);

    //根据id查询
    UserInfo findById(Long id);

    /**
     * 批量查询用户详情
     *    返回值：Map<id,UserInfo>
     */
    Map<Long,UserInfo> findByIds(List<Long> userIds,UserInfo info);

    //分页查询
    IPage findAll(Integer page,Integer pagesize);
}
