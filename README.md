### ID生成器 weeget-infinite-id-generator
---

###### 实现请参考 https://github.com/baidu/uid-generator


Snowflake算法
-------------

目前本项目Snowflake的配置如下：

* sign(1bit)  
  固定1bit符号标识，即生成的UID为正数。

* delta seconds (31 bits)  
  当前时间，相对于时间基点"2010-01-01"的增量值，单位：秒，最多可支持约69年

* worker id (19 bits)  
  机器id，最多可支持约52w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。

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

###### 使用方法

* http调用
```
1) 获取单个id

  http://127.0.0.1:9999/getNextId

2) 批量获取id 

  http://127.0.0.1:9999/getNextIds?num=100  num目前取值限制 0< num <=100


3）解析id内容

  http://127.0.0.1:9999/parseId?id=xxxxxxxxxxxxxxxxxx(具体的id值)
   
  // 解析后的内容格式：{"UID":"5212172199309377540","timestamp":"2021-03-10 17:16:16","workerId":"4","sequence":"4"}
      

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
    String parse = idFeignClient.parseId(id);

    // 解析后的内容格式：{"UID":"5212172199309377540","timestamp":"2021-03-10 17:16:16","workerId":"4","sequence":"4"}
        
             

```