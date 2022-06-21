package com.xugm.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xugm.dubbo.mappers.QuestionMapper;
import com.xugm.model.domain.Question;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class QuestionApiImpl  implements QuestionApi{

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Question findByUserId(Long userId) {
        QueryWrapper<Question> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);
        return questionMapper.selectOne(qw);
    }

    @Override
    public void save(Question question) {
        questionMapper.insert(question);
    }

    @Override
    public void update(Question question) {
        questionMapper.updateById(question);
    }
}
