package com.xugm.dubbo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xugm.model.domain.UserInfo;

public interface BlackListApi {

    //分页查询黑名单列表
    IPage<UserInfo> findByUserId(Long userId, int page, int size);

    //取消黑名单用户
    void delete(Long userId, Long blackUserId);
}
