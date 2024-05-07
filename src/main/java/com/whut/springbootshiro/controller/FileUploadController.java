package com.whut.springbootshiro.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Constant;
import com.whut.springbootshiro.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;


@RestController
@RequestMapping("file")
public class FileUploadController {

    @Value("${file-upload-path}")
    public String fileUploadPath;



    @RequestMapping("uploadImage")
    public Object uploadImage(@RequestParam("file") MultipartFile image, HttpServletRequest request) {
        //原来的名称
        String originalFilename = image.getOriginalFilename();
        //这个是解析这个文件的后缀名
        String extName = FileUtil.extName(originalFilename);
        String newFilename = DateUtil.format(new Date(), "yyyyMMddHHssmm");
        newFilename += "." + extName;
        //获取upload文件加路径
        String upload = fileUploadPath;
        //文件保存的物理路径
        String filePath = upload + File.separator + newFilename;
        //需要保存的网络路径
        String url = Constant.UPLOAD_FOLDER + "/" + newFilename;
        try {
            image.transferTo(new File(filePath));
            return new Result(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //上传文件路径错误
        return new Result(CodeMsg.UPLOAD_IMG_ERROR);
    }

    @RequestMapping("uploadFile")
    public Object uploadFile(@RequestParam("file") MultipartFile image, HttpServletRequest request) {
        //原来的名称
        String originalFilename = image.getOriginalFilename();
        //这个是解析这个文件的后缀名
        String extName = FileUtil.extName(originalFilename);
        String newFilename = DateUtil.format(new Date(), "yyyyMMddHHssmm");
        newFilename += "." + extName;
        //获取upload文件加路径
        String upload = fileUploadPath;
        //文件保存的物理路径
        String filePath = upload + File.separator + newFilename;
        //需要保存的网络路径
        String url = Constant.UPLOAD_FOLDER + "/" + newFilename;
        try {
            image.transferTo(new File(filePath));
            return new Result(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //上传文件路径错误
        return new Result(CodeMsg.UPLOAD_IMG_ERROR);
    }
}
