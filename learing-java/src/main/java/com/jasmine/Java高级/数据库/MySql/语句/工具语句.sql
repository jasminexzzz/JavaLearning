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

-- 行专列
select a.user_id,group_concat(a.device_id)
from t_project_device_user a
where a.delete_flag = 1
  and a.user_type = 2
group by a.user_id