package com.xdm.test;

import com.aliyun.facebody20191230.models.DetectFaceRequest;
import com.aliyun.teaopenapi.models.Config;

public class Sample {


    public static com.aliyun.facebody20191230.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);

        // 访问的域名
        config.endpoint = "facebody.cn-shanghai.aliyuncs.com";
        return new com.aliyun.facebody20191230.Client(config);
    }

    public static void main(String[] args) throws Exception {
        com.aliyun.facebody20191230.Client client =
                Sample.createClient("accessKeyId", "accessKeySecret");
        DetectFaceRequest detectFaceRequest = new DetectFaceRequest();
        // 复制代码运行请自行打印 API 的返回值
        client.detectFace(detectFaceRequest);
    }
}