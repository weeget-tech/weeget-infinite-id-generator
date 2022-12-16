package cn.weeget.service.id.controller;


import cn.weeget.util.common.result.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 健康检查
 * </p>
 *
 * @author admin
 * @since 2020-08-14
 */
@RestController
@RequestMapping
public class HealthCheckController {

    @RequestMapping(value = {"/", "/health/check"})
    public JsonResult check() {
        return JsonResult.ok("Hi, id service");
    }

}
