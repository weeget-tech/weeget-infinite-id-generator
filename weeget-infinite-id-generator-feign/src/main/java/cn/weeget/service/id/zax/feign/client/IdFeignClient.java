package cn.weeget.service.id.zax.feign.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "weeget-infinite-id-generator")
public interface IdFeignClient {

    @PostMapping("/getNextId")
    Long getNextId();

    @PostMapping("/getNextIds")
    List<Long> getNextIds(@RequestParam(value = "num") int num);


    @PostMapping("/getRealtimeId")
    Long getRealtimeId();

    @PostMapping("/getRealtimeIds")
    List<Long> getRealtimeIds(@RequestParam(value = "num") int num);


    @PostMapping("/parseId")
    String parseId(@RequestParam(value = "id") long id);

}
