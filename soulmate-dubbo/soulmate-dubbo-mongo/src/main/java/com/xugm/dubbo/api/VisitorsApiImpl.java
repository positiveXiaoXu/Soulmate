package com.xugm.dubbo.api;

import com.xugm.model.mongo.Visitors;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@DubboService
public class VisitorsApiImpl implements VisitorsApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存访客数据
     *  对于同一个用户，一天之内只能保存一次访客数据
     */
    public void save(Visitors visitors) {
        //1、查询访客数据
        Query query = Query.query(Criteria.where("userId").is(visitors.getUserId())
                .and("visitorUserId").is(visitors.getVisitorUserId())
                .and("visitDate").is(visitors.getVisitDate()));
        //2、不存在，保存
        if(!mongoTemplate.exists(query,Visitors.class)) {
            mongoTemplate.save(visitors);
        }
    }

    //查询首页访客列表
    public List<Visitors> queryMyVisitors(Long date, Long userId) {
        Criteria criteria = Criteria.where("userId").is(userId);
        if(date != null) {
            criteria.and("date").gt(date);
        }
        Query query = Query.query(criteria).limit(5).with(Sort.by(Sort.Order.desc("date")));
        return mongoTemplate.find(query,Visitors.class);
    }
}
