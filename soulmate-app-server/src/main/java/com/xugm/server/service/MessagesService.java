package com.xugm.server.service;

import cn.hutool.core.collection.CollUtil;
import com.xugm.autoconfig.template.HuanXinTemplate;
import com.xugm.commons.utils.Constants;
import com.xugm.dubbo.api.FriendApi;
import com.xugm.dubbo.api.UserApi;
import com.xugm.dubbo.api.UserInfoApi;
import com.xugm.model.domain.User;
import com.xugm.model.domain.UserInfo;
import com.xugm.model.mongo.Friend;
import com.xugm.model.vo.ContactVo;
import com.xugm.model.vo.ErrorResult;
import com.xugm.model.vo.PageResult;
import com.xugm.model.vo.UserInfoVo;
import com.xugm.server.exception.BusinessException;
import com.xugm.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MessagesService {

    @DubboReference
    private UserApi userApi;

    @DubboReference
    private UserInfoApi userInfoApi;

    @DubboReference
    private FriendApi friendApi;

    @Autowired
    private HuanXinTemplate huanXinTemplate;

    /**
     * 根据环信id查询用户详情
     */
    public UserInfoVo findUserInfoByHuanxin(String huanxinId) {
        //1、根据环信id查询用户
        User user = userApi.findByHuanxin(huanxinId);
        //2、根据用户id查询用户详情
        UserInfo userInfo = userInfoApi.findById(user.getId());
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,vo); //copy同名同类型的属性
        if(userInfo.getAge() != null) {
            vo.setAge(userInfo.getAge().toString());
        }
        return vo;
    }

    //添加好友关系
    public void contacts(Long friendId) {
        //1、将好友关系注册到环信
        Boolean aBoolean = huanXinTemplate.addContact(Constants.HX_USER_PREFIX + UserHolder.getUserId(),
                Constants.HX_USER_PREFIX + friendId);
        if(!aBoolean) {
            throw new BusinessException(ErrorResult.error());
        }
        //2、如果注册成功，记录好友关系到mongodb
        friendApi.save(UserHolder.getUserId(),friendId);
    }

    //分页查询联系人列表
    public PageResult findFriends(Integer page, Integer pagesize, String keyword) {
        //1、调用API查询当前用户的好友数据 -- List<Friend>
        List<Friend> list = friendApi.findByUserId(UserHolder.getUserId(),page,pagesize);
        if(CollUtil.isEmpty(list)) {
            return new PageResult();
        }
        //2、提取数据列表中的好友id
        List<Long> userIds = CollUtil.getFieldValues(list, "friendId", Long.class);
        //3、调用UserInfoAPI查询好友的用户详情
        UserInfo info = new UserInfo();
        info.setNickname(keyword);
        Map<Long, UserInfo> map = userInfoApi.findByIds(userIds, info);
        //4、构造VO对象
        List<ContactVo> vos = new ArrayList<>();
        for (Friend friend : list) {
            UserInfo userInfo = map.get(friend.getFriendId());
            if(userInfo != null) {
                ContactVo vo = ContactVo.init(userInfo);
                vos.add(vo);
            }
        }
        return new PageResult(page,pagesize,0l,vos);
    }
}
