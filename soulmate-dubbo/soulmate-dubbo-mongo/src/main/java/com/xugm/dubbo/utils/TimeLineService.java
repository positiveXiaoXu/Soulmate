package com.xugm.dubbo.utils;

import cn.hutool.core.collection.CollUtil;
import com.xugm.model.mongo.Friend;
import com.xugm.model.mongo.MovementTimeLine;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class TimeLineService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Async //异步执行，原理：底层开一个线程去执行该方法
    public CompletableFuture<String> saveTimeLine(Long userId, ObjectId movementId) {
        //写入好友的时间线表
        try {
            //1、查询当前用户的好友数据
            Criteria criteria = Criteria.where("userId").is(userId);
            Query query = Query.query(criteria);
            List<Friend> friends = mongoTemplate.find(query, Friend.class);
            if (CollUtil.isEmpty(friends)) {
                return CompletableFuture.completedFuture("ok");//返回成功
            }

            //2、循环好友数据，构建时间线数据存入数据库
            for (Friend friend : friends) {
                MovementTimeLine timeLine = new MovementTimeLine();
                timeLine.setMovementId(movementId);
                timeLine.setUserId(friend.getUserId());
                timeLine.setFriendId(friend.getFriendId());
                timeLine.setCreated(System.currentTimeMillis());
                mongoTemplate.save(timeLine);
            }
        } catch (Exception e) {
           e.printStackTrace();
            //TODO 事务回滚问题
            return CompletableFuture.completedFuture("error");
        }

        return CompletableFuture.completedFuture("ok");
    }
}