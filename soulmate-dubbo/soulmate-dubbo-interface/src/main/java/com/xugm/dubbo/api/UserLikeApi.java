package com.xugm.dubbo.api;

public interface UserLikeApi {

    //保存或者更新
    Boolean saveOrUpdate(Long userId, Long likeUserId, boolean isLike);
}
