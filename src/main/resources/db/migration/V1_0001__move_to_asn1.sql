CREATE TABLE IF NOT EXISTS `mq_events` (
   `sequenceNumber` bigint NOT NULL,
   `topic` varchar(50) NOT NULL,
   `data`  VARBINARY(5000) NULL,
   PRIMARY KEY (`sequenceNumber`,`topic`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;