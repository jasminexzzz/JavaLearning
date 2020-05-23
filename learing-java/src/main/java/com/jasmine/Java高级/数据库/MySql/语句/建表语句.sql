-- 删除表
DROP TABLE base_menu;

-- 创建表
CREATE TABLE IF NOT EXISTS `base_menu`(
   `menu_id`    TINYINT      UNSIGNED AUTO_INCREMENT COMMENT 'ID' , -- 自增
   `menu_name`  VARCHAR(100) NOT NULL COMMENT '名称',
   `level`      TINYINT      NOT NULL COMMENT '级别',
   `order`      TINYINT      NOT NULL COMMENT '排序',
   `states`     TINYINT      NOT NULL COMMENT '状态 0-关闭 1-启用',
   PRIMARY KEY (`menu_id`)
)