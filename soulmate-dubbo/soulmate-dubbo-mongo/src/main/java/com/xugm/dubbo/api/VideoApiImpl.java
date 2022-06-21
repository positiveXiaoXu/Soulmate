package com.xugm.dubbo.api;

import com.xugm.dubbo.utils.IdWorker;
import com.xugm.model.mongo.Video;
import com.xugm.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@DubboService
public class VideoApiImpl implements VideoApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IdWorker idWorker;

    @Override
    public String save(Video video) {
        //1、设置属性
        video.setVid(idWorker.getNextId("video"));
        video.setCreated(System.currentTimeMillis());
        //2、调用方法保存对象
        mongoTemplate.save(video);
        //3、返回对象id
        return video.getId().toHexString();
    }

    @Override
    public List<Video> findMovementsByVids(List<Long> vids) {
        Query query = Query.query(Criteria.where("vid").in(vids));
        return mongoTemplate.find(query,Video.class);
    }

    @Override
    public List<Video> queryVideoList(int page, Integer pagesize) {
        Query query = new Query().limit(pagesize).skip((page -1) * pagesize)
                .with(Sort.by(Sort.Order.desc("created")));
        return mongoTemplate.find(query,Video.class);
    }

    @Override
    public PageResult findByUserId(Integer page, Integer pagesize, Long userId) {
        Query query = Query.query(Criteria.where("userId").in(userId));
        long count = mongoTemplate.count(query, Video.class);
        query.limit(pagesize).skip((page -1) * pagesize)
                .with(Sort.by(Sort.Order.desc("created")));
        List<Video> list = mongoTemplate.find(query, Video.class);
        return new PageResult(page,pagesize,count,list);
    }
}
