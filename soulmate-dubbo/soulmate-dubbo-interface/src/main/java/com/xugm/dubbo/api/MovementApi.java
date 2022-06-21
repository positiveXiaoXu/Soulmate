package com.xugm.dubbo.api;

import com.xugm.model.mongo.Movement;
import com.xugm.model.vo.PageResult;

import java.util.List;

public interface MovementApi {

    //发布动态
    String publish(Movement movement);

    //根据用户id，查询此用户发布的动态数据列表
    PageResult findByUserId(Long userId, Integer page, Integer pagesize);

    //根据用户id，查询用户好友发布的动态列表
    List<Movement> findFriendMovements(Integer page, Integer pagesize, Long userId);

    //根据pid数组查询动态
    List<Movement> findMovementsByPids(List<Long> pids);

    //随机获取多条动态数据
    List<Movement> randomMovements(Integer counts);

    //根据id查询
    Movement findById(String movementId);

    //根据用户id，查询
    PageResult findByUserId(Long uid, Integer state, Integer page, Integer pagesize);

    //更新动态状态
//    void update(String movementId, int state);
}
