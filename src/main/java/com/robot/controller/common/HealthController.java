package com.robot.controller.common;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songzonglin
 * @create 2024/9/12 18:34
 */
@Api(tags = "健康检查")
@RequestMapping("/api/common")
@RestController
@Slf4j
public class HealthController {

    @GetMapping("/health/check")
    public String healthCheck() {
        return "Ok";
    }

}
