CREATE TABLE `movies` (
    `id` binary(16) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `description` varchar(2000) DEFAULT NULL,
    `duration_in_minutes` int NOT NULL,
    `genre` varchar(255) NOT NULL,
    `language` varchar(255) NOT NULL,
    `poster_url` varchar(255) DEFAULT NULL,
    `release_date` date DEFAULT NULL,
    `status` enum('ENDED','NOW_SHOWING','UPCOMING') NOT NULL,
    `title` varchar(255) NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;