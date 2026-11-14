CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `name` varchar(30) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKn22vnmml6mjq0j6i24cavceas` (`user_id`,`name`),
  CONSTRAINT `FKghuylkwuedgl2qahxjt8g41kb` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `payment_methods` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `name` varchar(30) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhjsmtf5onxwmr25apx4504j1o` (`user_id`,`name`),
  CONSTRAINT `FKin7rtmim3ljrrhh5kxbq27s2v` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `monthly_budgets` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `budget_amount` decimal(38,2) NOT NULL,
  `target_month` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKknvfwhn2t46wgqvt02r8smvp` (`user_id`,`target_month`),
  CONSTRAINT `FKqt35xjpv4pc52dtk2yb7j4sin` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `category_budgets` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `budget_amount` decimal(38,2) NOT NULL,
  `target_month` varchar(255) NOT NULL,
  `category_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK94xfgpyn3t4enqn9vc80mmhp0` (`user_id`,`category_id`,`target_month`),
  KEY `FK6tro3ax8dxgqh2u1e30muhxhy` (`category_id`),
  CONSTRAINT `FK6tro3ax8dxgqh2u1e30muhxhy` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `FK9iog728lu3hwdkmygl30ok16c` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `expenditures` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `amount` decimal(38,2) NOT NULL,
  `expenditure_date` date NOT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `category_id` bigint NOT NULL,
  `payment_method_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb9a1h2yaf4ktv8l7oitfv0cjs` (`category_id`),
  KEY `FK9necha2prjlbh392mbfsbccm8` (`payment_method_id`),
  KEY `idx_expenditures_user_date` (`user_id`,`expenditure_date`),
  CONSTRAINT `FK9a4qydnjx1e17ngb9b6hdxi99` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9necha2prjlbh392mbfsbccm8` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_methods` (`id`),
  CONSTRAINT `FKb9a1h2yaf4ktv8l7oitfv0cjs` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `alert_settings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `monthly_budget_alert_enabled` bit(1) NOT NULL,
  `monthly_budget_alert_threshold` int NOT NULL,
  `remaining_amount_alert_enabled` bit(1) NOT NULL,
  `remaining_amount_threshold` decimal(38,2) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK81f9djxgulfxtbhnd61cd5o4p` (`user_id`),
  CONSTRAINT `FKorgegnkm409abd7efrkffvfne` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `fixed_expense_templates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `amount` decimal(38,2) NOT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `scheduled_day` int NOT NULL,
  `category_id` bigint NOT NULL,
  `payment_method_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsefqofv3naocerk7up3xcsm13` (`category_id`),
  KEY `FKtjbm9fphvbjni21lha5fen7eu` (`payment_method_id`),
  KEY `FK4n77i0vkhu4r0xfyqwgktipap` (`user_id`),
  CONSTRAINT `FK4n77i0vkhu4r0xfyqwgktipap` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKsefqofv3naocerk7up3xcsm13` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `FKtjbm9fphvbjni21lha5fen7eu` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_methods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

