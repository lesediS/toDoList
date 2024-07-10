DROP SCHEMA IF EXISTS tododb;
CREATE SCHEMA tododb;

CREATE TABLE tododb.`user`(
userId INT NOT NULL AUTO_INCREMENT,
firstname VARCHAR(45) NOT NULL,
surname VARCHAR(45) NOT NULL,
username VARCHAR(45) NOT NULL,
`password` VARCHAR(45) NOT NULL,
PRIMARY KEY (userId)
);

CREATE TABLE tododb.task(
taskId INT NOT NULL AUTO_INCREMENT,
userId INT,
dateToBeDone VARCHAR(45) NOT NULL,
`description` VARCHAR(45) NOT NULL,
task VARCHAR(45) NOT NULL,
FOREIGN KEY (userId) REFERENCES `user`(userId),
PRIMARY KEY (taskId)
);

select * from tododb.task;
select * from tododb.`user`;