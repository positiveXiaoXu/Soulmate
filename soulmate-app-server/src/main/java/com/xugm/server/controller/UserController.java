package com.xugm.server.controller;

import com.xugm.server.service.UserInfoService;
import com.xugm.model.domain.UserInfo;
import com.xugm.server.interceptor.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 保存用户信息
     *   UserInfo
     *   请求头中携带token
     */
    @PostMapping("/loginReginfo")
    public ResponseEntity loginReginfo(@RequestBody UserInfo userInfo,
                                       @RequestHeader("Authorization") String token) {
        //2、向userinfo中设置用户id

        userInfo.setId(UserHolder.getUserId());
        //3、调用service
        userInfoService.save(userInfo);
        return ResponseEntity.ok(null);
    }

    /**
     * 上传用户头像
     */
    @PostMapping("/loginReginfo/head")
    public ResponseEntity head(MultipartFile headPhoto,@RequestHeader("Authorization") String token) throws IOException {
        //2、向userinfo中设置用户id
        //3、调用service
        userInfoService.updateHead(headPhoto,UserHolder.getUserId());
        return ResponseEntity.ok(null);
    }
}
