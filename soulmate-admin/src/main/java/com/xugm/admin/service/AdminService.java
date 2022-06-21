package com.xugm.admin.service;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xugm.admin.exception.BusinessException;
import com.xugm.admin.interceptor.AdminHolder;
import com.xugm.admin.mapper.AdminMapper;
import com.xugm.commons.utils.Constants;
import com.xugm.commons.utils.JwtUtils;
import com.xugm.model.domain.Admin;
import com.xugm.model.vo.AdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public Map login(Map map) {
        //1、获取请求参数
        String username = (String )map.get("username");
        String password = (String )map.get("password");
        String verificationCode = (String )map.get("verificationCode");
        String uuid = (String )map.get("uuid");
        //2、检验验证码是否正确
        String key = Constants.CAP_CODE + uuid;
        String value = redisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(value) || !verificationCode.equals(value)) {
            throw  new BusinessException("验证码错误");
        }
        redisTemplate.delete(key);
        //3、根据用户名查询管理员对象 Admin
        QueryWrapper<Admin> qw = new QueryWrapper<Admin>().eq("username",username);
        Admin admin = adminMapper.selectOne(qw);
        //4、判断admin对象是否存在，密码是否一致
        password = SecureUtil.md5(password);
        if(admin == null || !password.equals(admin.getPassword())) {
            throw  new BusinessException("用户名或者密码错误");
        }
        //5、生成token
        Map tokenMap = new HashMap();
        tokenMap.put("id",admin.getId());
        tokenMap.put("username",admin.getUsername());
        String token = JwtUtils.getToken(tokenMap);
        //6、构造返回值
        Map retMap = new HashMap();
        retMap.put("token",token);
        return retMap;
    }

    //获取当前用户的用户资料
    public AdminVo profile() {
        Long id = AdminHolder.getId();
        Admin admin = adminMapper.selectById(id);
        return AdminVo.init(admin);
    }
}
