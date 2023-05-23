package com.xb.reggie.controller;

import com.xb.reggie.common.R;
import com.xb.reggie.exception.FileEmptyException;
import com.xb.reggie.exception.FileSizeException;
import com.xb.reggie.exception.FileTypeException;
import com.xb.reggie.exception.FileUploadIoException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/common")
@RestController
public class CommonController extends BaseController{
    @Value("${reggie.path}")
    private String path;
    private static final Integer AVATAR_SIZE=10*1024*1024;
    private static final List<String> AVATAR_TYPE=new ArrayList<>();
    static {
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
    }
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        if(file.isEmpty()){
            throw  new FileEmptyException("文件为空!");
        }
        if(file.getSize()>AVATAR_SIZE){
            throw new FileSizeException("文件超过上传大小！");
        }
        if(!AVATAR_TYPE.contains(file.getContentType())){
            throw new FileTypeException("文件上传类型不符！");
        }
        String name=file.getOriginalFilename();
        String fileType=name.substring(name.lastIndexOf("."));
        String fileName= UUID.randomUUID().toString().toUpperCase()+fileType;
        File file2=new File(path);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file1=new File(path,fileName);
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            throw new FileUploadIoException("文件读写异常!");
        }
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream input=new FileInputStream(new File(path,name));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes=new byte[1024];
            int len=0;
            while((len=input.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            input.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
