package com.xugm.recommend.listener;

import com.alibaba.fastjson.JSON;
import com.xugm.model.mongo.Video;
import com.xugm.model.mongo.VideoScore;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RecommendVideoListener {

    /**
     * 获取动态的日志消息
     * 转化评分
     * 构造评分对象，存入MongoDB
     */

    @Autowired
    private MongoTemplate mongoTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(
                    value = "xugm.video.queue",
                    durable = "true"
            ),
            exchange = @Exchange(
                    value = "xugm.log.exchange",
                    type = ExchangeTypes.TOPIC
            ),
            key="log.video"
    ))
    public void recommend(String message) {
        Map map = JSON.parseObject(message, Map.class);
        //1、解析数据
        Long userId = Long.valueOf(map.get("userId").toString());
        String type = (String) map.get("type");
        String logTime = (String) map.get("logTime");
        String videoId = (String) map.get("busId");
        //2、构造MovementScore，设置评分
        Video video = mongoTemplate.findById(videoId, Video.class);
        if(video != null) {
            VideoScore vs = new VideoScore();
            vs.setUserId(userId);
            vs.setVideoId(video.getVid());
            vs.setDate(System.currentTimeMillis());
            vs.setScore(getScore(type));
            mongoTemplate.save(vs);
        }
    }

    private static Double getScore(String type) {
        //0301为发小视频，0302为小视频点赞，0303为小视频取消点赞，0304为小视频评论
        Double score = 0d;
        switch (type) {
            case "0301":
                score=2d;
                break;
            case "0302":
                score=5d;
                break;
            case "0303":
                score = -5d;
                break;
            case "0304":
                score = 10d;
                break;
            default:
                break;
        }
        return score;
    }}
