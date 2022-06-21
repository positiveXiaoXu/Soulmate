package com.xugm.dubbo.api;

import java.util.List;

public interface UserLocationApi {

    //更新地理位置
    Boolean updateLocation(Long userId, Double longitude, Double latitude, String address);

    //查询附近的人的所有用户id
    List<Long> queryNearUser(Long userId, Double metre);
}
