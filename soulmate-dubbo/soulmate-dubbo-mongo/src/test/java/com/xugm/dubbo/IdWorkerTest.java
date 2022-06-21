package com.xugm.dubbo;

import com.xugm.dubbo.utils.IdWorker;
import com.xugm.model.mongo.RecommendUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IdWorkerTest {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test() {
        Criteria criteria = Criteria.where("toUserId").is(106).and("userId").nin(1, 2, 3,4,5,6,7,8);
        //1、创建统计对象，设置统计参数
        TypedAggregation aggregation = Aggregation.newAggregation(RecommendUser.class,
                Aggregation.match(criteria),
                Aggregation.sample(10));
        //2、调用mongoTemplate方法统计
        AggregationResults<RecommendUser> results = mongoTemplate.aggregate(aggregation, RecommendUser.class);
        //3、获取统计结果
        List<RecommendUser> list = results.getMappedResults();
        list.forEach(System.out::println);
    }
}
