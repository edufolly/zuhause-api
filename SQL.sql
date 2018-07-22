CREATE DATABASE `zuhause`
    CHARACTER SET = utf8
    COLLATE = utf8_bin;

USE `zuhause`;

CREATE TABLE `pairs` ( 
    `id` Int( 11 ) AUTO_INCREMENT NOT NULL,
    `tab` VarChar( 50 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `key` VarChar( 50 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `value` VarChar( 50 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
    `when` Timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ( `id` ) )
CHARACTER SET = utf8
COLLATE = utf8_bin
ENGINE = InnoDB;

CREATE INDEX `KEY_TAB` USING BTREE ON `pairs`( `tab`, `key` );

CREATE INDEX `KEY_WHEN` USING BTREE ON `pairs`( `when`, `tab`, `key` );

