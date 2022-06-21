package com.xugm.dubbo.api;

import com.xugm.model.mongo.RecommendUser;
import com.xugm.model.vo.PageResult;

import java.util.List;

public interface RecommendUserApi {

    RecommendUser queryWithMaxScore(Long toUserId);

    //分页查询
    PageResult queryRecommendUserList(Integer page, Integer pagesize, Long toUserId);

    //根据操作人id和查看的用户id，查询两者的推荐数据
    RecommendUser queryByUserId(Long userId, Long userId1);

    //探花-查询推荐用户列表
    List<RecommendUser> queryCardsList(Long userId, int count);
}
