ALTER TABLE `users`
    ADD COLUMN `token` VARCHAR(255) DEFAULT '',
    ADD INDEX (`token`);


