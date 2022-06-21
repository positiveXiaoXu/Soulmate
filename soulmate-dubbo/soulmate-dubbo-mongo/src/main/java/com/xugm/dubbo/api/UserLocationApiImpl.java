package com.xugm.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.xugm.model.mongo.UserLocation;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@DubboService
public class UserLocationApiImpl implements UserLocationApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    //更新地理位置
    public Boolean updateLocation(Long userId, Double longitude, Double latitude, String address) {
        try {
            //1、根据用户id查询位置信息
            Query query = Query.query(Criteria.where("userId").is(userId));
            UserLocation location = mongoTemplate.findOne(query, UserLocation.class);
            if(location == null) {
                //2、如果不存在用户位置信息，保存
                location = new UserLocation();
                location.setUserId(userId);
                location.setAddress(address);
                location.setCreated(System.currentTimeMillis());
                location.setUpdated(System.currentTimeMillis());
                location.setLastUpdated(System.currentTimeMillis());
                location.setLocation(new GeoJsonPoint(longitude,latitude));
                mongoTemplate.save(location);
            }else {
                //3、如果存在，更新
                Update update = Update.update("location", new GeoJsonPoint(longitude, latitude))
                        .set("updated", System.currentTimeMillis())
                        .set("lastUpdated", location.getUpdated());
                mongoTemplate.updateFirst(query,update,UserLocation.class);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Long> queryNearUser(Long userId, Double metre) {
        //1、根据用户id，查询用户的位置信息
        Query query = Query.query(Criteria.where("userId").is(userId));
        UserLocation location = mongoTemplate.findOne(query, UserLocation.class);
        if(location == null) {
            return null;
        }
        //2、已当前用户位置绘制原点
        GeoJsonPoint point = location.getLocation();
        //3、绘制半径
        Distance distance = new Distance(metre / 1000, Metrics.KILOMETERS);
        //4、绘制圆形
        Circle circle = new Circle(point, distance);
        //5、查询
        Query locationQuery = Query.query(Criteria.where("location").withinSphere(circle));
        List<UserLocation> list = mongoTemplate.find(locationQuery, UserLocation.class);
        return CollUtil.getFieldValues(list,"userId",Long.class);
    }
}
