package com.xzzz.项目.电商项目;

/**
 * @author : jasmineXz
 */
public class D_库存扣减 {
    /**

     准备开始扣减库存
        1. 通过分布式锁
            1. 获取到锁
            2. 查询库存
            3. 有库存
            4. 修改库存
            5. 释放锁

            分布式锁也可以采用分段加锁,类似以前的CHASHMAP,但会增加复杂度

        2. 通过数据库CAS
            1. 库存表增加版本号字段 version
            2. 获取库存及版本号
            3. 修改库存时增加 update 库存表 set 库存 = 库存 -1,version = version + 1 where 库存id = 1 and version = version.
            4. 这样若是修改时版本号不一致则where条件查询不到数据,修改失败.

            缺点 : 对数据库压力大,需要不断获取数据,重试.
                   mybatis存在sqlsession缓存.要确保一级缓存不影响查询

        3. 将秒杀商品放入Redis进行库存扣减.


     扣减库存分为两种
     一种是

     */
}
