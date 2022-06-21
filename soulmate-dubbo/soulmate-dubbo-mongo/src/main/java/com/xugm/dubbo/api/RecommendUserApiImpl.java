package com.xugm.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.xugm.model.mongo.RecommendUser;
import com.xugm.model.mongo.UserLike;
import com.xugm.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@DubboService
public class RecommendUserApiImpl  implements RecommendUserApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    //查询今日佳人
    public RecommendUser queryWithMaxScore(Long toUserId) {

        //根据toUserId查询，根据评分score排序，获取第一条
        //构建Criteria
        Criteria criteria = Criteria.where("toUserId").is(toUserId);
        //构建Query对象
        Query query = Query.query(criteria).with(Sort.by(Sort.Order.desc("score")))
                .limit(1);
        //调用mongoTemplate查询

        return mongoTemplate.findOne(query,RecommendUser.class);
    }

    //分页查询
    public PageResult queryRecommendUserList(Integer page, Integer pagesize, Long toUserId) {
        //1、构建Criteria对象
        Criteria criteria = Criteria.where("toUserId").is(toUserId);
        //2、创建Query对象
        Query query = Query.query(criteria).with(Sort.by(Sort.Order.desc("score"))).limit(pagesize)
                .skip((page - 1) * pagesize);
        //3、调用mongoTemplate查询
        List<RecommendUser> list = mongoTemplate.find(query, RecommendUser.class);
        long count = mongoTemplate.count(query, RecommendUser.class);
        //4、构建返回值PageResult
        return  new PageResult(page,pagesize,count,list);
    }

    @Override
    public RecommendUser queryByUserId(Long userId, Long toUserId) {
        Criteria criteria = Criteria.where("toUserId").is(toUserId).and("userId").is(userId);
        Query query = Query.query(criteria);
        RecommendUser user = mongoTemplate.findOne(query, RecommendUser.class);
        if(user == null) {
            user = new RecommendUser();
            user.setUserId(userId);
            user.setToUserId(toUserId);
            //构建缘分值
            user.setScore(95d);
        }
        return user;
    }

    /**
     * 1、排除喜欢，不喜欢的用户
     * 2、随机展示
     * 3、指定数量
     */
    public List<RecommendUser> queryCardsList(Long userId, int counts) {
        //1、查询喜欢不喜欢的用户ID
        List<UserLike> likeList = mongoTemplate.find(Query.query(Criteria.where("userId").is(userId)), UserLike.class);
        List<Long> likeUserIdS = CollUtil.getFieldValues(likeList, "likeUserId", Long.class);
        //2、构造查询推荐用户的条件
        Criteria criteria = Criteria.where("toUserId").is(userId).and("userId").nin(likeUserIdS);
        //3、使用统计函数，随机获取推荐的用户列表
        TypedAggregation<RecommendUser> newAggregation = TypedAggregation.newAggregation(RecommendUser.class,
                Aggregation.match(criteria),//指定查询条件
                Aggregation.sample(counts)
        );
        AggregationResults<RecommendUser> results = mongoTemplate.aggregate(newAggregation, RecommendUser.class);
        //4、构造返回
        return results.getMappedResults();
    }
}
