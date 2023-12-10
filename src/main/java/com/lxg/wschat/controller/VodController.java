package com.lxg.wschat.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.lxg.wschat.service.VodService;
import com.lxg.wschat.utils.ConstantVodUtils;
import com.lxg.wschat.utils.InitVodClient;
import com.lxg.wschat.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @auther xiaolin
 */
@Api(tags="阿里云视频上传")
@RestController
@RequestMapping("/vod")
//@CrossOrigin
public class VodController {

    @Autowired
    VodService vodService;

    //上传视频到阿里云
    @ApiOperation(value = "上传视频到阿里云")
    @PostMapping("/uploadAliyunVideo")
    public R uploadAliyunVideo(@ApiParam("文件") MultipartFile file){
        //返回上传视频id
        String videoId = vodService.uploadVideoAly(file);
        return R.ok().data("videoId",videoId).message("文件上传成功");
    }

    //根据视频id删除阿里云视频
    @ApiOperation(value = "根据视频id删除阿里云视频")
    @DeleteMapping("/removeAlyVideo/{id}")
    public R removeAlyVideo(@ApiParam("视频id") @PathVariable String  id){
        try{
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request设置视频id
            request.setVideoIds(id);
            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);
            return R.ok();
        }catch(Exception e){
            e.printStackTrace();
            return R.error().message("删除视频失败");
        }
    }

    //删除多个阿里云视频的方法
    //参数多个视频id  List videoIdList
    @ApiOperation(value = "删除多个阿里云视频")
    @DeleteMapping("delete-batch")
    public R deleteBatch(@ApiParam("视频id列表") @RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeMoreAlyVideo(videoIdList);
        return R.ok();
    }

    //根据视频id获取视频凭证
    @ApiOperation(value = "根据视频id获取视频凭证")
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@ApiParam("视频id") @PathVariable String id){
        String playAuth = vodService.getPlayAuth(id);
        return R.ok().data("playAuth",playAuth).message("获取凭证成功");
    }

}
