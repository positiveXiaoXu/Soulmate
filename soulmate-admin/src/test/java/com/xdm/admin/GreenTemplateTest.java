package com.xdm.admin;


import com.xugm.autoconfig.template.AliyunGreenTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GreenTemplateTest {

    @Autowired
    private AliyunGreenTemplate template;

    @Test
    public void test() throws Exception {
        //Map<String, String> map = template.greenTextScan("本校小额贷款，安全、快捷、方便、无抵押，随机随贷，当天放款，上门服务");
        List<String> list = new ArrayList<>();
        list.add("http://images.china.cn/site1000/2018-03/17/dfd4002e-f965-4e7c-9e04-6b72c601d952.jpg");
        Map<String, String> map = template.imageScan(list);
        map.forEach((k,v)-> System.out.println(k+"--"+v));
    }

}