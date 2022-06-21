package com.xdm.test;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xugm.server.AppServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class FastDFSTest {

    /**
     * 测试FastDFS的文件上传
     */

    //用于文件上传或者下载
    @Autowired
    private FastFileStorageClient client;

    @Autowired
    private FdfsWebServer webServer;

    @Test
    public void testUpload() throws FileNotFoundException {
        //1、指定文件
        File file = new File("D:\\1.jpg");
        //2、文件上传
        StorePath path = client.uploadFile(new FileInputStream(file), file.length(), "jpg", null);
        //3、拼接请求路径
        String fullPath = path.getFullPath();
        System.out.println(fullPath);  //a/b/abc.jpg;
        String url = webServer.getWebServerUrl() + fullPath;
        System.out.println(url);
    }

}
