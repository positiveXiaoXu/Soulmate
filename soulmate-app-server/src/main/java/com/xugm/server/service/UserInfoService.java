package com.xugm.server.service;

import com.xugm.autoconfig.template.AipFaceTemplate;
import com.xugm.autoconfig.template.OssTemplate;
import com.xugm.dubbo.api.UserInfoApi;
import com.xugm.model.domain.UserInfo;
import com.xugm.model.vo.ErrorResult;
import com.xugm.model.vo.UserInfoVo;
import com.xugm.server.exception.BusinessException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserInfoService {

    @DubboReference
    private UserInfoApi userInfoApi;

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private AipFaceTemplate aipFaceTemplate;

    public void save(UserInfo userInfo) {
        userInfoApi.save(userInfo);
    }

    //更新用户头像
    public void updateHead(MultipartFile headPhoto, Long id) throws IOException {
        //1、将图片上传到阿里云oss
        String imageUrl = ossTemplate.upload(headPhoto.getOriginalFilename(), headPhoto.getInputStream());
        //2、调用百度云判断是否包含人脸
        boolean detect = aipFaceTemplate.detect(imageUrl);
        //2.1 如果不包含人脸，抛出异常
        if(!detect) {
            throw new BusinessException(ErrorResult.faceError());
        }else{
            //2.2 包含人脸，调用API更新
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setAvatar(imageUrl);
            userInfoApi.update(userInfo);
        }
    }

    //根据id查寻
    public UserInfoVo findById(Long id) {
        UserInfo userInfo = userInfoApi.findById(id);

        UserInfoVo vo = new UserInfoVo();

        BeanUtils.copyProperties(userInfo,vo); //copy同名同类型的属性

        if(userInfo.getAge() != null) {
            vo.setAge(userInfo.getAge().toString());
        }

        return vo;
    }

    //更新
    public void update(UserInfo userInfo) {
        userInfoApi.update(userInfo);
    }
}
