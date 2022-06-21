package com.xugm.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xugm.model.domain.Log;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface LogMapper extends BaseMapper<Log> {

    @Select("SELECT COUNT(DISTINCT user_id) FROM tb_log WHERE TYPE=#{type} AND log_time=#{logTime}")
    Integer queryByTypeAndLogTime(@Param("type") String type, @Param("logTime") String logTime); //根据操作时间和类型


    @Select("SELECT COUNT(DISTINCT user_id) FROM tb_log WHERE log_time=#{logTime}")
    Integer queryByLogTime(String logTime); //展示记录时间查询


    @Select("SELECT COUNT(DISTINCT user_id)  FROM tb_log WHERE log_time=#{today} AND user_id IN (\n " +
            " SELECT user_id FROM tb_log WHERE TYPE=\"0102\" AND log_time=#{yestoday} \n " +
            ")")
    Integer queryNumRetention1d(@Param("today") String today,@Param("yestoday") String yestoday); //查询次日留存
}
