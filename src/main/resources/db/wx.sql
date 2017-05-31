SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `wx` ;
CREATE SCHEMA IF NOT EXISTS `wx` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `wx` ;

DROP TABLE IF EXISTS `wx`.`sys_organization` ;
CREATE TABLE IF NOT EXISTS `wx`.`sys_organization` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(128) NULL,
  `parent_id` INT NULL,
  `priority` INT NULL,
  `create_time` DATETIME,
  `modify_time` DATETIME,
  PRIMARY KEY (`id`),
  INDEX `idx_sys_organization_1` (`parent_id` ASC)
) ENGINE = INNODB;

DROP TABLE IF EXISTS `wx`.`sys_role` ;
CREATE TABLE IF NOT EXISTS `wx`.`sys_role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(256) NULL,
  PRIMARY KEY (`id`)
) ENGINE = INNODB;

DROP TABLE IF EXISTS `wx`.`sys_menu` ;
CREATE TABLE IF NOT EXISTS `wx`.`sys_menu` (
  `id` INT NOT NULL,
  `category` VARCHAR(50) NULL,
  `name` VARCHAR(100) NULL,
  `url` VARCHAR(45) NULL,
  `parent_id` INT NULL,
  `flag` INT NULL COMMENT 'ztree open-flag',
  `target` VARCHAR(32) NULL COMMENT 'ztree target iframe',
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_sys_menu_1` (`category` ASC),
  INDEX `idx_sys_menu_2` (`parent_id` ASC)
) ENGINE = INNODB;

DROP TABLE IF EXISTS `wx`.`sys_menu_class` ;
CREATE TABLE IF NOT EXISTS `wx`.`sys_menu_class` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `menu_id` INT NOT NULL,
  `name` VARCHAR(100) NULL,
  `class_name` VARCHAR(100) NULL COMMENT '对应的模块全类名',
  `method` VARCHAR(50) NULL COMMENT 'Shiro的权限标识，比如：user:view',
  PRIMARY KEY (`id`),
  INDEX `idx_sys_menu_class_1` (`menu_id` ASC),
  CONSTRAINT `fk_sys_menu_class_1`
    FOREIGN KEY (`menu_id`)
    REFERENCES `wx`.`sys_menu` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = INNODB;

DROP TABLE IF EXISTS `wx`.`sys_role_permission` ;
CREATE TABLE IF NOT EXISTS `wx`.`sys_role_permission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role_id` INT NOT NULL,
  `menu_id` INT NOT NULL,
  `menu_class_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_sys_role_permission_1` (`role_id` ASC),
  INDEX `idx_sys_role_permission_2` (`menu_id` ASC),
  CONSTRAINT `fk_sys_role_permission_1`
    FOREIGN KEY (`role_id`)
    REFERENCES `wx`.`sys_role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sys_role_permission_2`
    FOREIGN KEY (`menu_id`)
    REFERENCES `wx`.`sys_menu` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sys_role_permission_3`
    FOREIGN KEY (`menu_class_id`)
    REFERENCES `wx`.`sys_menu_class` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = INNODB;

DROP TABLE IF EXISTS `wx`.`sys_user` ;
CREATE TABLE IF NOT EXISTS `wx`.`sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自动增长',
  `uid` VARCHAR(30) NULL COMMENT '用户ID',
  `username` VARCHAR(32) NOT NULL COMMENT '用户帐号，登录',
  `password` VARCHAR(64) NULL COMMENT '密码',
  `salt` VARCHAR(45) NULL COMMENT '加密散列数据',
  `realname` VARCHAR(600) NULL COMMENT '姓名',
  `gender` INT NULL DEFAULT 0 COMMENT '性别 0 - 男 ，1 - 女',
  `photo` VARCHAR(300) NULL COMMENT '用户头像',
  `phone` VARCHAR(32) NULL COMMENT '手机号',
  `country_code` VARCHAR(8) NULL COMMENT '号码对应的国家码，如中国：0086',
  `nationality` VARCHAR(30) NULL COMMENT '国籍',
  `individual_id` VARCHAR(32) NULL COMMENT '身份证',
  `email` VARCHAR(100) NULL COMMENT '邮箱',
  `address` VARCHAR(300) NULL COMMENT '地址',
  `self_intro` VARCHAR(1000) NULL COMMENT '自我介绍',
  `is_vip` INT NULL DEFAULT 0 COMMENT '是否VIP， 0-否，1-是',
  `status` INT NULL DEFAULT 0 COMMENT '状态：0-enabled; 1-disabled;',
  `utype` INT NULL DEFAULT 0 COMMENT '用户类型：0-系统用户不能删除；1-管理后台用户；2-终端注册用户',
  `org_id` INT NULL COMMENT '所属组织',
  `role_id` INT NULL COMMENT '角色',
  `gps` VARCHAR(60) NULL COMMENT '用户注册时的位置(经度，纬度)，经纬度用逗号隔开',
  `longitude` DOUBLE NULL DEFAULT 0.0 COMMENT '用户最后的经度',
  `latitude` DOUBLE NULL DEFAULT 0.0 COMMENT '用户最后的纬度',
  `ip` VARCHAR(45) NULL COMMENT '操作的IP',
  `user_agent` VARCHAR(200) NULL COMMENT '终端设备信息',
  `last_login`  DATETIME NULL COMMENT '最后登录时间',
  `create_time` DATETIME,
  `modify_time` DATETIME,
  PRIMARY KEY (`id`),
  INDEX `idx_sys_user_1` (`uid` ASC),
  INDEX `idx_sys_user_2` (`username` ASC),
  INDEX `idx_sys_user_3` (`phone` ASC),
  INDEX `idx_sys_user_4` (`email` ASC),
  INDEX `idx_sys_user_5` (`org_id` ASC),
  INDEX `idx_sys_user_6` (`role_id` ASC),
  INDEX `idx_sys_user_7` (`longitude` ASC),
  INDEX `idx_sys_user_8` (`latitude` ASC),
  CONSTRAINT `fk_sys_user_1`
    FOREIGN KEY (`org_id`)
    REFERENCES `wx`.`sys_organization` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sys_user_2`
    FOREIGN KEY (`role_id`)
    REFERENCES `wx`.`sys_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB
COMMENT = '用户表';

DROP TABLE IF EXISTS `wx`.`sys_log` ;
CREATE TABLE IF NOT EXISTS `wx`.`sys_log` (
  `id` BIGINT NULL AUTO_INCREMENT COMMENT '主键，自动增长',
  `username` VARCHAR(32) NULL COMMENT '操作用户名',
  `ip` VARCHAR(45) NULL COMMENT '操作的IP',
  `user_agent` VARCHAR(200) NULL COMMENT '终端设备信息',
  `message` VARCHAR(256) NULL COMMENT '日志信息',
  `create_time` DATETIME NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_sys_log_1` (`username` ASC),
  INDEX `idx_sys_log_2` (`ip` ASC)
) ENGINE = INNODB;


-- data initalization
USE `wx`;
INSERT INTO `sys_user` (`id`, `uid`, `username`, `password`, `salt`, `realname`, `gender`, `photo`, `phone`, `country_code`, `nationality`, `individual_id`, `email`, `address`, `self_intro`, `is_vip`, `status`, `utype`, `org_id`, `role_id`, `gps`, `longitude`, `latitude`, `user_agent`, `last_login`, `create_time`, `modify_time`) VALUES('1','10001','admin','f0850817aee6fcd981ec4578314ee3bc8afdc61c','f40eaf1c6ec3efaf','','0',NULL,NULL,'0086',NULL,NULL,'xiewe9@163.com',NULL,'创建者','1','0','0',NULL,NULL,NULL,'0','0',NULL,NULL,NULL,NULL);
COMMIT;




