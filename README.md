### ID生成器 weeget-infinite-id-generator
---

###### 实现参考 https://github.com/baidu/uid-generator


###### 使用方法

* http调用
```
1) 获取单个id

  http://127.0.0.1:9999/getNextId

2) 批量获取id 

  http://127.0.0.1:9999/getNextIds?num=100  num目前取值限制 0< num <=100


3）解析id内容

  http://127.0.0.1:9999/parseId?id=xxxxxxxxxxxxxxxxxx(具体的id值)
    

```

* Feign调用
``` 
1) 引入feign客户端包
        <dependency>
            <groupId>cn.weeget</groupId>
            <artifactId>weeget-infinite-id-generator-feign</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>

2） 获取id

    @Resource
    private IdFeignClient idFeignClient;
    
     // 单个id
    Long id = idFeignClient.getNextId();

    // 批量获取id
    List<Long> ids = idFeignClient.getNextIds(10);

    // 解析id
    // 解析后的内容：{"UID":"5212172199309377540","timestamp":"2021-03-10 17:16:16","workerId":"4","sequence":"4"}
    
    String parse = idFeignClient.parseId(id);
             

```