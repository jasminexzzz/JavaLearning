package com.jasmine.Java高级.数据库.MySql.优化;

/**
 * @author : jasmineXz
 */
public class 记一系列优化 {
    /**
     表字段
     id	    int	        11
     name	varchar	    30
     age	tinyint	    100
     idcard	int	        100
     flag	tinyint	    2
     addr	varchar	    200
     data	timestamp	6

     数据: 500W条
     <为了清晰后续字段用星号代替>
     ──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
     <单语句普通查询>

     1. 在未建任何索引的情况下,全表搜索
         语句 : select * from users a;
         耗时 : 26.054s

     2. 声明字段名 :
         语句 : select * from users a
         耗时 : 25.790s

     3. 未建索引的情况下随机根据id查询
         语句 : select * from users a where a.id = 97543
         耗时 : 4.582s

     4. 未建索引的情况下随机根据name查询
         语句 : select * from users a where a.name = 'name987282'
         耗时 : 4.076s

     5. 为Id创建主键索引
         语句 : alter table users add primary key ( `id` );
         耗时 : 40.076s    数据很多的情况下创建索引需要时间.

     6. 建索引的情况下随机根据id查询
         语句 : select * from users a where a.id = 97543
         耗时 : 0.052s

     7. 为name创建普通索引
         语句 : alter table users add index index_name( `name` );
         耗时 : 30s

     8. 未建索引的情况下随机根据name查询
         语句 : select * from users a where a.name = 'name987282'
         耗时 : 0.043s

     可以看到,对查询条件增加索引来优化是没有问题的,那么如何优化limit语句呢?
     ──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
     <limit分页>

     1.从开始进行分页
         语句 : select * from users a limit 0,10;
         耗时 : 0.049s

     2. 从大量数据中间或末尾进行分页
         语句 : select * from users a limit 4500000,10;
         耗时 : 3.750s

     3. 可以增加带有索引的查询条件来优化语句
         语句 : select * from users a where a.id > '4500000' limit 10;
         耗时 : 0.040s

        但通常,id不是连贯的,其中可能会删除数据等,所以在查询下一页时,可以将本页最后一条主键数据或含有索引的数据传入后台,作为查询条件来优化速度.

     4. 也可以使用
         语句 : select a.* from users a join (select b.id from users b order by b.id limit 4500000,10) as tmp on tmp.id=a.id;
         耗时 : 1.108s

     ──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
     <非唯一的查询条件>
     1.从开始进行分页
         语句 : select * from users a where a.data = '2019-09-18 17:58:29.000000';
         耗时 : 4.7s

     2.增加索引
         语句 : alter table users add index index_data( `data` );
         耗时 : 21.3

     3.再次查询
         语句 : select * from users a where a.data = '2019-09-18 17:58:29.000000';
         耗时 : 0.125s

     4. 用In查询多个内容匹配
         语句 : select * from users a where a.data in ('2019-09-18 17:58:29.000000','2019-09-18 17:58:30.000000');
         耗时 : 0.225s

     5. 用Or查询多个内容匹配
         语句 : select * from users a where a.data = '2019-09-18 17:58:29.000000' or a.data ='2019-09-18 17:58:30.000000';
         耗时 : 0.225s
        在查询列有索引的情况下,in和or没有太大差别,如果没有所以,in的效率更高



     */
}
