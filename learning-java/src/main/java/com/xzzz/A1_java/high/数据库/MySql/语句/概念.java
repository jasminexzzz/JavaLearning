package com.xzzz.A1_java.high.数据库.MySql.语句;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**


     ======================== 主外键相关 ========================
     删除主键
     mysql> alter table student drop primary key;

     添加字段并设置为自增主键：
     mysql> alter table student add id int not null auto_increment, add primary key (id);

     将字段修改为自增主键 : 需要先删除当前字段的其余主键
     mysql> alter table user change column userid userid int not null primary key auto_increment;


     */
}
