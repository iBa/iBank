CREATE TABLE `{$regions$}` (
`name` TEXT NOT NULL ,
`loc1` TEXT NOT NULL ,
`loc2` TEXT NOT NULL ,
`onper` TEXT NOT NULL ,
`offper` TEXT NOT NULL
);
CREATE TABLE `{$accounts$}` (
`name` TEXT NOT NULL ,
`balance` BIGINT NOT NULL ,
`owners` TEXT NOT NULL ,
`users` TEXT NOT NULL ,
`onper` TEXT NOT NULL ,
`offper` TEXT NOT NULL
);
CREATE TABLE `{$loan$}` (
`id` INT NOT NULL AUTO_INCREMENT ,
`user` TEXT NOT NULL,
`amount` BIGINT NOT NULL,
`percentage` INT,
`interval` DOUBLE,
`until` INT,
PRIMARY KEY ( `id` ) 
);

