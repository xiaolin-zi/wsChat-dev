package com.lxg.wschat.controller;

import com.lxg.wschat.service.OssService;
import com.lxg.wschat.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @auther xiaolin
 */

@Api(tags="阿里云图片上传")
@RestController
@RequestMapping("/oss")
//@CrossOrigin
public class ImgController {

    @Autowired
    private OssService ossService;

    @ApiOperation(value = "文件上传")
    @PostMapping("uploadOssFile")
    public R uploadOssFile(@ApiParam("文件") MultipartFile file){

        //获取上传的文件 MultipartFile
        //返回上传oss的路径
        String url = ossService.uploadFileAvatar(file);
        if (url == null){
            return R.error().message("文件上传失败");
        }
        return R.ok().data("url",url).message("文件上传成功");
    }

}
