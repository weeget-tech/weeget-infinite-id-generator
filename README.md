### ID生成器 weeget-infinite-id-generator
---

###### 实现请参考 https://github.com/baidu/uid-generator


Snowflake算法
-------------

目前本项目Snowflake算法的配置如下：

* sign(1bit)  
  固定1bit符号标识，即生成的UID为正数。

* delta seconds (31 bits)  
  当前时间，相对于时间基点"2010-01-01"的增量值，单位：秒，最多可支持约69年

* worker id (19 bits)  
  机器id，最多可支持约52w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃。

* sequence (13 bits)   
  每秒下的并发序列，13 bits可支持每秒8192个并发。


###### 压测数据
```

1) 配置：2C4G 1个pod

   获取单个ID       TPS:4300+  RT:51ms 
   批量获取10个ID   TPS:3900+  RT:51ms

2) 配置：2C4G 5个pod
   
   获取单个ID       TPS:20000+  RT:40ms 
   批量获取10个ID   TPS:19000+  RT:46ms


```

###### ID生成策略
       
  策略1： 预先生成，放到缓存中 （超高性能）
       
  策略2： 实时生成
       
       
  一般使用策略1即可，像需要根据订单号解析订单生成时间的业务场景，可以使用策略2

###### 使用方法






* http调用（内网）
```
 
 测试、财务、集成、生产环境使用：http://weeget-infinite-id-generator-svc.inspire:9999
 
 预发布环境使用：http://weeget-infinite-id-generator-svc.gray-inspire-v2:9999
 

1. 获取id 

   1) 获取单个id（预先生成，从缓存中取）：  

   /getNextId

   2) 批量获取id （预先生成，从缓存中取）

   /getNextIds?num=10  

   num目前取值限制 0< num <=100



   3) 获取单个id（实时生成）：  

   /getRealtimeId

   4) 批量获取id（实时生成）

    /getRealtimeIds?num=10  

    num目前取值限制 0< num <=100


2. 解析id内容

  /parseId?id=xxxxxxxxxxxxxxxxxx(具体的id值)
   
  // 解析后的内容格式：{"UID":"5212172199309377540","timestamp":"2021-03-10 17:16:16","workerId":"4","sequence":"4"}
      

```

* Feign调用
``` 
1. 引入feign客户端包
        <dependency>
            <groupId>cn.weeget</groupId>
            <artifactId>weeget-infinite-id-generator-feign</artifactId>
            <version>2.0.0</version>
        </dependency>

2. 获取id

    @Resource
    private IdFeignClient idFeignClient;
    

    1) 获取单个id（预先生成，从缓存中取）
    Long id = idFeignClient.getNextId();

    2）批量获取id（预先生成，从缓存中取）
    List<Long> ids = idFeignClient.getNextIds(10);



    3）单个id（实时生成）
    Long id = idFeignClient.getRealtimeId();
   
    4）批量获取id（实时生成）
    List<Long> ids = idFeignClient.getRealtimeIds(10);

3. 解析id

    // 解析id
    String parse = idFeignClient.parseId(id);

    // 解析后的内容格式：{"UID":"5212172199309377540","timestamp":"2021-03-10 17:16:16","workerId":"4","sequence":"4"}
        
             

```