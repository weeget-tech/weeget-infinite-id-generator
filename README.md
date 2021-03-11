### ID生成器 weeget-infinite-id-generator
---
基于百度UidGenerator实现

UidGenerator是Java实现的, 基于Snowflake算法的唯一ID生成器。UidGenerator以组件形式工作在应用项目中, 支持自定义workerId位数和初始化策略, 从而适用于docker等虚拟化环境下实例自动重启、漂移等场景。 在实现上, UidGenerator通过借用未来时间来解决sequence天然存在的并发限制; 采用RingBuffer来缓存已生成的UID, 并行化UID的生产和消费, 同时对CacheLine补齐，避免了由RingBuffer带来的硬件级「伪共享」问题. 最终单机QPS可达600万。

依赖版本：Java8及以上版本, MySQL(内置WorkerID分配器, 启动阶段通过DB进行分配; 如自定义实现, 则DB非必选依赖）

---
###### 实现参考 https://github.com/baidu/uid-generator


###### 使用方法

* http调用
```
1) 获取单个id

  http://127.0.0.1:9990/getNextId

2) 批量获取id 

  http://127.0.0.1:9990/getNextIds?num=100  num目前取值限制 0< num <=100


3）解析id内容

  http://127.0.0.1:9990/parseId?id=xxxxxxxxxxxxxxxxxx(具体的id值)
    

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