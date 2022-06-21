package com.xdm.test;

import com.xugm.dubbo.api.UserLocationApi;
import com.xugm.server.AppServerApplication;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class TestUserLocationApi {

    @DubboReference
    private UserLocationApi userLocationApi;

    @Test
    public void testUpdateUserLocation() {
        this.userLocationApi.updateLocation(1L, 116.353885,40.065911, "育新地铁站");
        this.userLocationApi.updateLocation(2L, 116.352115,40.067441, "北京石油管理干部学院");
        this.userLocationApi.updateLocation(3L, 116.336438,40.072505, "回龙观医院");
        this.userLocationApi.updateLocation(4L, 116.396797,40.025231, "奥林匹克森林公园");
        this.userLocationApi.updateLocation(5L, 116.323849,40.053723, "小米科技园");
        this.userLocationApi.updateLocation(6L, 116.403963,39.915119, "天安门");
        this.userLocationApi.updateLocation(7L, 116.328103,39.900835, "北京西站");
        this.userLocationApi.updateLocation(8L, 116.609564,40.083812, "北京首都国际机场");
        this.userLocationApi.updateLocation(9L, 116.459958,39.937193, "德云社(三里屯店)");
        this.userLocationApi.updateLocation(10L, 116.333374,40.009645, "清华大学");
        this.userLocationApi.updateLocation(41L, 116.316833,39.998877, "北京大学");
        this.userLocationApi.updateLocation(42L, 117.180115,39.116464, "天津大学(卫津路校区)");
    }
}