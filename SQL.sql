CREATE TABLE `pairs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tab` varchar(50) CHARACTER SET utf8 NOT NULL,
  `key` varchar(50) COLLATE utf8_bin NOT NULL,
  `value` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `when` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `KEY_WHEN` (`when`,`tab`,`key`),
  KEY `KEY_TAB` (`tab`,`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
