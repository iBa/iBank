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
