package com.xdm.test;

import com.xugm.dubbo.api.UserInfoApi;
import com.xugm.model.domain.UserInfo;
import com.xugm.server.AppServerApplication;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class UserInfoApiTest {

    @DubboReference
    private UserInfoApi userInfoApi;

    @Test
    public void test() {
        List ids = new ArrayList();
        ids.add(1l);
        ids.add(2l);
        ids.add(3l);
        ids.add(4l);
        UserInfo userInfo = new UserInfo();
        userInfo.setAge(35);
        Map map = userInfoApi.findByIds(ids, userInfo);
        map.forEach((k,v) -> System.out.println(k+"--"+v));
    }
}
