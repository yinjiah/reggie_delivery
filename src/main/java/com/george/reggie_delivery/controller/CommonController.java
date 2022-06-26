package com.george.reggie_delivery.controller;

import com.george.reggie_delivery.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author George
 * @Date 2022-06-20-14:56
 * @Description TODO 文件上传下载
 * @Version 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/common")
public class CommonController {

    @Value("${image.basedir}")
    private String baseDir;

    /**
     * @author George
     * @date 15:14 2022/6/20
     * @param file
     * @return com.george.reggie_delivery.common.R<java.lang.String>
     * @description 图片上传
     */
    @PostMapping("/upload")
    public R<String> uploadImage(MultipartFile file){
        File imgDir = new File(baseDir);
        if(!imgDir.exists()){
            imgDir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName =UUID.randomUUID().toString() + suffix ;
        try {
            file.transferTo(new File(baseDir+newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(newFileName);
    }

    /**
     * @author George
     * @date 15:25 2022/6/20
     * @param name
     * @param response
     * @return void
     * @description 图片下载
     */
    @GetMapping("/download")
    public void downloadImage(String name, HttpServletResponse response){
        try (FileInputStream fileInputStream = new FileInputStream(baseDir+name);
                ServletOutputStream os = response.getOutputStream()) {
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while( (len=fileInputStream.read(bytes)) != -1){
                os.write(bytes,0,len);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
