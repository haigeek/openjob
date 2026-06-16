-- ----------------------------
-- Extend columns for K8s DNS hostname compatibility
-- ----------------------------
ALTER TABLE "server" ALTER COLUMN "ip" TYPE varchar(128);
ALTER TABLE "server" ALTER COLUMN "akka_address" TYPE varchar(128);
ALTER TABLE "worker" ALTER COLUMN "address" TYPE varchar(128);
