package com.xugm.dubbo.utils;

import com.xugm.model.mongo.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class IdWorker {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Long getNextId(String collName) {
        Query query = new Query(Criteria.where("collName").is(collName));

        Update update = new Update();
        update.inc("seqId", 1);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);

        Sequence sequence = mongoTemplate.findAndModify(query, update, options, Sequence.class);
        return sequence.getSeqId();
    }
}
