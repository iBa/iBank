CREATE TABLE `{$regions$}` (
`name` VARCHAR(30) NOT NULL ,
`loc1` VARCHAR(1000) NOT NULL ,
`loc2` VARCHAR(1000) NOT NULL ,
`onper` VARCHAR(30) NOT NULL ,
`offper` VARCHAR(30) NOT NULL
);
CREATE TABLE `{$accounts$}` (
`name` VARCHAR(50) NOT NULL ,
`balance` BIGINT NOT NULL ,
`owners` TEXT NOT NULL ,
`users` TEXT NOT NULL ,
`onper` VARCHAR(30) NOT NULL ,
`offper` VARCHAR(30) NOT NULL
);
CREATE TABLE `{$loan$}` (
`id` INT NOT NULL AUTO_INCREMENT ,
`user` VARCHAR(30) NOT NULL,
`amount` BIGINT NOT NULL,
`percentage` INT,
`interval` INT,
`until` INT,
`mD` INT,
PRIMARY KEY ( `id` ) 
);

