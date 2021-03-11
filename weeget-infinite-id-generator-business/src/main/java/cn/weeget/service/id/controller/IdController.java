package cn.weeget.service.id.controller;

import cn.weeget.service.id.baidu.UidGenerator;
import cn.weeget.util.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class IdController {


    public static final int MAX_NUM = 100;

    public static final int MIN_NUM = 1;


    @Resource
    @Qualifier("defaultUidGenerator")
    private UidGenerator uidGenerator;

    @RequestMapping("/getNextId")
    public Long getNextId() {
        long id = uidGenerator.getUID();
        return id;
    }

    @RequestMapping("/getNextIds")
    public List<Long> getNextIds(@RequestParam int num) {
        if (num < MIN_NUM || num > MAX_NUM) {
            throw new BusinessException("批量获取id数量范围：1~100");
        }
        List<Long> ids = new ArrayList<>(num);
        for (int i=0; i<num; i++) {
            ids.add(uidGenerator.getUID());
        }
        return ids;
    }

    @RequestMapping("/parseId")
    public String parseId(@RequestParam long id) {
        return uidGenerator.parseUID(id);
    }
}
