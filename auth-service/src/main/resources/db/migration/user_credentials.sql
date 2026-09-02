CREATE TABLE `user_credentials` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password_hash` varchar(255) NOT NULL,
    `role` varchar(255) DEFAULT NULL,
    `account_status` varchar(255) DEFAULT NULL,
    `created_at` TIMESTAMP NULL DEFAULT current_timestamp,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB
  AUTO_INCREMENT=3
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;
