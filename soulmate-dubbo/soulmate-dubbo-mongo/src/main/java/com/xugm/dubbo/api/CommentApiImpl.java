package com.xugm.dubbo.api;

import com.xugm.model.enums.CommentType;
import com.xugm.model.mongo.Comment;
import com.xugm.model.mongo.Movement;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@DubboService
public class CommentApiImpl implements  CommentApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    //发布评论，并获取评论数量
    public Integer save(Comment comment) {
        //1、查询动态
        Movement movement = mongoTemplate.findById(comment.getPublishId(), Movement.class);
        //2、向comment对象设置被评论人属性
        if(movement != null) {
            comment.setPublishUserId(movement.getUserId());
        }
        //3、保存到数据库
        mongoTemplate.save(comment);
        //4、更新动态表中的对应字段
        Query query = Query.query(Criteria.where("id").is(comment.getPublishId()));
        Update update = new Update();
        if(comment.getCommentType() == CommentType.LIKE.getType()) {
            update.inc("likeCount",1);
        }else if (comment.getCommentType() == CommentType.COMMENT.getType()){
            update.inc("commentCount",1);
        }else {
            update.inc("loveCount",1);
        }
        //设置更新参数
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true) ;//获取更新后的最新数据
        Movement modify = mongoTemplate.findAndModify(query, update, options, Movement.class);
        //5、获取最新的评论数量，并返回
        return modify.statisCount(comment.getCommentType() );
    }

    //分页查询
    public List<Comment> findComments(String movementId, CommentType commentType, Integer page, Integer pagesize) {
        //1、构造查询条件
        Query query = Query.query(Criteria.where("publishId").is(new ObjectId(movementId)).and("commentType")
                .is(commentType.getType()))
                .skip((page -1) * pagesize)
                .limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));
        //2、查询并返回
        return mongoTemplate.find(query,Comment.class);
    }

    //判断comment数据是否存在
    public Boolean hasComment(String movementId, Long userId, CommentType commentType) {
        Criteria criteria = Criteria.where("userId").is(userId)
                .and("publishId").is(new ObjectId(movementId))
                .and("commentType").is(commentType.getType());
        Query query = Query.query(criteria);
        return mongoTemplate.exists(query,Comment.class); //判断数据是否存在
    }

    //删除
    public Integer delete(Comment comment) {
        //1、删除Comment表数据
        Criteria criteria = Criteria.where("userId").is(comment.getUserId())
                .and("publishId").is(comment.getPublishId())
                .and("commentType").is(comment.getCommentType());
        Query query = Query.query(criteria);
        mongoTemplate.remove(query,Comment.class);
        //2、修改动态表中的总数量
        Query movementQuery = Query.query(Criteria.where("id").is(comment.getPublishId()));
        Update update = new Update();
        if(comment.getCommentType() == CommentType.LIKE.getType()) {
            update.inc("likeCount",-1);
        }else if (comment.getCommentType() == CommentType.COMMENT.getType()){
            update.inc("commentCount",-1);
        }else {
            update.inc("loveCount",-1);
        }
        //设置更新参数
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true) ;//获取更新后的最新数据
        Movement modify = mongoTemplate.findAndModify(movementQuery, update, options, Movement.class);
        //5、获取最新的评论数量，并返回
        return modify.statisCount(comment.getCommentType() );
    }
}
