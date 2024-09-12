package com.robot.controller.api;

import com.robot.entity.domain.JsonResult;
import com.robot.service.MQTTService;
import com.robot.service.ZLMediaKitService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author songzonglin
 * @create 2024/9/11 17:18
 */


@Api(tags = "视频流管理")
@RequestMapping("/api/video")
@RestController
@Slf4j
public class VideoStreamController {


    @Resource
    private ZLMediaKitService zlMediaKitService;

    @Resource
    private MQTTService mqttService;

    @GetMapping("/record")
    public JsonResult getMediaServerInfo() {
        //notify


        //getVideoLink


        return JsonResult.success();
    }


}
