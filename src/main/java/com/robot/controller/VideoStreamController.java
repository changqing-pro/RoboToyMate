package com.robot.controller;

import com.robot.entity.domain.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songzonglin
 * @create 2024/9/11 17:18
 */


@Api(tags = "视频流管理")
@RequestMapping("/api")
@RestController
@Slf4j
public class VideoStreamController {

    @ApiOperation(value = "test")
    @GetMapping("/test")
    public JsonResult getInfo() {
        return JsonResult.success("11");
    }
}
