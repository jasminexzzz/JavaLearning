-- 查询数据库大小
select
table_schema as '数据库',
sum(table_rows) as '记录数',
sum(truncate(data_length/1024/1024, 2)) as '数据容量(MB)',
sum(truncate(index_length/1024/1024, 2)) as '索引容量(MB)'
from information_schema.tables
where table_schema='myjavastudy';

-- 查询数据库中的表的大小
select
table_schema as '数据库',
table_name as '表名',
table_rows as '记录数',
truncate(data_length/1024/1024, 2) as '数据容量(MB)',
truncate(index_length/1024/1024, 2) as '索引容量(MB)'
from information_schema.tables
where table_schema='myjavastudy'
order by data_length desc, index_length desc;

-- 行转列
select a.user_id,group_concat(a.device_id)
from t_project_device_user a
where a.delete_flag = 1
  and a.user_type = 2
group by a.user_id

/*
判断某个字段是否在某个字符串中
    如 ids 字段保存了 id : '1,2,3,4'
       判断id 3 是否包含在字符串中使用 find_in_set 来查询

如下例子 : 相同租户中, [1] 用户所拥有的角色
      t_user                     t_role
|user_id|tenant_id|       |id|tenant_id|user_ids|
|   1   |    1    |       | 1|    1    |  1,2   |
|   2   |    1    |       | 2|    1    |    2   |
|   3   |    2    |       | 3|    2    |    3   |
|   4   |    2    |       | 4|    2    |  3,4   |
 */
   select u.user_id,r.id,r.user_ids
     from t_user u
left join t_role r on r.tenant_id = u.tenant_id
    where find_in_set(1,r.user_ids)

