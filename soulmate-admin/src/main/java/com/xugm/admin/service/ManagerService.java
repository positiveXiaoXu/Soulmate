package com.xugm.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xugm.commons.utils.Constants;
import com.xugm.dubbo.api.MovementApi;
import com.xugm.dubbo.api.UserInfoApi;
import com.xugm.dubbo.api.VideoApi;
import com.xugm.model.domain.UserInfo;
import com.xugm.model.mongo.Movement;
import com.xugm.model.vo.MovementsVo;
import com.xugm.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ManagerService {

    @DubboReference
    private UserInfoApi userInfoApi;

    @DubboReference
    private VideoApi videoApi;

    @DubboReference
    private MovementApi movementApi;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //用户列表
    public PageResult findAllUsers(Integer page, Integer pagesize) {
        IPage<UserInfo> iPage = userInfoApi.findAll(page, pagesize);
        List<UserInfo> list = iPage.getRecords();
        for (UserInfo userInfo : list) {
            String key = Constants.USER_FREEZE + userInfo.getId();
            if(redisTemplate.hasKey(key)) {
                userInfo.setUserStatus("2");
            }
        }
        return new PageResult(page, pagesize, iPage.getTotal(), iPage.getRecords());
    }

    //根据id查询
    public UserInfo findUserById(Long userId) {
        UserInfo userInfo = userInfoApi.findById(userId);
        //查询redis中的冻结状态
        String key = Constants.USER_FREEZE + userId;
        if(redisTemplate.hasKey(key)) {
            userInfo.setUserStatus("2");
        }
        return userInfo;
    }

    //查询指定用户发布的所有视频列表
    public PageResult findAllVideos(Integer page, Integer pagesize, Long uid) {
        return videoApi.findByUserId(page, pagesize, uid);
    }

    //查询动态
    public PageResult findAllMovements(Integer page, Integer pagesize, Long uid, Integer state) {
        //1、调用API查询数据 ：Movment对象
        PageResult result = movementApi.findByUserId(uid,state,page,pagesize);
        //2、解析PageResult，获取Movment对象列表
        List<Movement> items = (List<Movement>) result.getItems();
        //3、一个Movment对象转化为一个Vo
        if(CollUtil.isEmpty(items)) {
            return new PageResult();
        }
        List<Long> userIds = CollUtil.getFieldValues(items, "userId", Long.class);
        Map<Long, UserInfo> map = userInfoApi.findByIds(userIds, null);
        List<MovementsVo> vos = new ArrayList<>();
        for (Movement movement : items) {
            UserInfo userInfo = map.get(movement.getUserId());
            if(userInfo != null) {
                MovementsVo vo = MovementsVo.init(userInfo, movement);
                vos.add(vo);
            }
        }
        //4、构造返回值
        result.setItems(vos);
        return result;
    }

    //用户冻结
    public Map userFreeze(Map params) {
        //1、构造key
        String userId = params.get("userId").toString();
        String key = Constants.USER_FREEZE + userId;
        //2、构造失效时间
        Integer freezingTime = Integer.valueOf(params.get("freezingTime").toString()); //冻结时间，1为冻结3天，2为冻结7天，3为永久冻结
        int days = 0;
        if(freezingTime == 1) {
            days = 3;
        }else if(freezingTime == 2) {
            days = 7;
        }
        //3、将数据存入redis
        String value = JSON.toJSONString(params);
        if(days>0) {
            redisTemplate.opsForValue().set(key,value,days, TimeUnit.MINUTES);
        }else {
            redisTemplate.opsForValue().set(key,value);
        }
        Map retMap = new HashMap();
        retMap.put("message","冻结成功");
        return retMap;
    }

    //用户解冻
    public Map userUnfreeze(Map params) {
        String userId = params.get("userId").toString();
        String key = Constants.USER_FREEZE + userId;
        //删除redis数据
        redisTemplate.delete(key);
        Map retMap = new HashMap();
        retMap.put("message","解冻成功");
        return retMap;
    }
}