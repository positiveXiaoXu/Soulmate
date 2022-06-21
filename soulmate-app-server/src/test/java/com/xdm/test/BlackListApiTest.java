package com.xdm.test;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xugm.dubbo.api.BlackListApi;
import com.xugm.model.domain.UserInfo;
import com.xugm.server.AppServerApplication;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class BlackListApiTest {

    @DubboReference
    private BlackListApi blackListApi;

    @Test
    public void testFindByMobile() {
        IPage<UserInfo> page = blackListApi.findByUserId(106l, 2, 2);
        for (UserInfo record : page.getRecords()) {
            System.out.println(record);
        }
    }
}
