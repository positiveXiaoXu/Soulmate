package com.xugm.dubbo;

import com.xugm.model.mongo.Movement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test() {

        int page = 1,pagesize = 10;
        Long uid = null;
        Integer state = 0;

//        Query query = new Query().skip((page - 1) * pagesize).limit(pagesize)
//                .with(Sort.by(Sort.Order.desc("created")));

        Query query =  Query.query(Criteria.where("state").is(state)).skip((page - 1) * pagesize).limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));

//        if(uid != null) {
//            query.addCriteria(Criteria.where("userId").is(uid));
//        }
//        if(state != null) {
//            query.addCriteria(Criteria.where("state").is(state));
//        }
        List<Movement> list = mongoTemplate.find(query, Movement.class);
        long count = mongoTemplate.count(query, Movement.class);


        System.out.println(count);
    }
}
