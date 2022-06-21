package com.xugm.dubbo.api;

import com.xugm.model.mongo.UserLike;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@DubboService
public class UserLikeApiImpl implements UserLikeApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Boolean saveOrUpdate(Long userId, Long likeUserId, boolean isLike) {
        try {
            //1、查询数据
            Query query = Query.query(Criteria.where("userId").is(userId).and("likeUserId").is(likeUserId));
            UserLike userLike = mongoTemplate.findOne(query, UserLike.class);
            //2、如果不存在，保存
            if(userLike == null) {
                userLike = new UserLike();
                userLike.setUserId(userId);
                userLike.setLikeUserId(likeUserId);
                userLike.setCreated(System.currentTimeMillis());
                userLike.setUpdated(System.currentTimeMillis());
                userLike.setIsLike(isLike);
                mongoTemplate.save(userLike);
            }else {
                //3、更新
                Update update = Update.update("isLike", isLike)
                        .set("updated",System.currentTimeMillis());
                mongoTemplate.updateFirst(query,update,UserLike.class);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
