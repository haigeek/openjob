-- ----------------------------
-- Extend columns for K8s DNS hostname compatibility
-- ----------------------------
ALTER TABLE `server` MODIFY COLUMN `ip` varchar(128) NOT NULL DEFAULT '' COMMENT 'Server ip';
ALTER TABLE `server` MODIFY COLUMN `akka_address` varchar(128) NOT NULL DEFAULT '' COMMENT 'Akka address like `127.0.0.1:25520`';
ALTER TABLE `worker` MODIFY COLUMN `address` varchar(128) NOT NULL DEFAULT '' COMMENT 'Worker address';
