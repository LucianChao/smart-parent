package com.javaweb.controller;

import com.javaweb.entity.Result;
import com.javaweb.utils.AliOSSProperties;
import com.javaweb.utils.AliyunOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class UploadController {
//    private final String bucketName = "steamedfish-javaweb-tlias";
//    private final String endpoint = "https://oss-cn-hangzhou.aliyuncs.com/";

//    @Value("${aliyun.oss.endpoint}")
//    private String endpoint;//oss上桶对应的域名
//    @Value("${aliyun.oss.bucketname}")
//    private String bucketName;//oss上的桶空间名
    @Autowired
    private AliOSSProperties aliOSSProperties;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("upload")
    public Result upload(MultipartFile file) throws Exception {
        //1.获取原始文件名，截取后缀
        String originalFilename = file.getOriginalFilename();
        log.info("原始文件名：{}",originalFilename);
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //2.调用阿里云OSS工具类，上传文件
        String url = AliyunOSSUtils.upload(aliOSSProperties.getEndpoint(), aliOSSProperties.getBucketName(), file.getBytes(), extName);
        //3.返回图片访问路径
        return Result.success(url);
    }
}
