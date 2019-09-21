package 中间件;

/**
 * @author : jasmineXz
 */
public class Redis {
    /**
     1. Redis数据淘汰策略
     redis 内存数据集大小上升到一定大小的时候，就会施行数据淘汰策略。redis 提供 6种数据淘汰策略:
     voltile-lru      : 从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
     volatile-ttl     : 从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
     volatile-random  : 从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
     allkeys-lru      : 从数据集（server.db[i].dict）中挑选最近最少使用的数据淘汰
     allkeys-random   : 从数据集（server.db[i].dict）中任意选择数据淘汰
     no-enviction（驱逐）: 禁止驱逐数据


     */
}
