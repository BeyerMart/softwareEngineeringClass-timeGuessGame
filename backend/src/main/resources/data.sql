drop database db_g6t3; #only run if it already exists
create database db_g6t3;
use db_g6t3;

create table Users
(
    userId int primary key not null auto_increment,
    userName varchar(100),
    userPassword varchar(100),
    enabled boolean,
    firstName varchar(100) not null,
    lastName varchar(100) not null,
    email varchar(100),
    userRoles enum ('USER', 'GASTNUTZER', 'SPIELEVERWALTER', 'ADMIN'),
    phoneNr varchar(100),
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    teamID int,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId)
);


create table TimeflipDice
(
    diceID int primary key not null auto_increment,
    diceEAN  varchar(100),
    hardwareStatus enum ('DAMAGED', 'BEINGREPAIRED', 'READYTOUSE', 'LOST'),
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    enabled boolean,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId)

);


create table Room
(
    roomID int primary key not null auto_increment,
    roomName varchar(100),
    diceId int,
    hostId int,
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    enabled boolean,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId)

);


create table Team
(
    teamId int primary key not null auto_increment,
    points int,
    name varchar(100),
    teamDescription varchar(100),
    teamLeader int,
    teamParent int,
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId),
    foreign key (teamLeader) references Users(userId),
    foreign key (teamParent) references room(roomID)
);


create table Game
(
    gameId int primary key not null auto_increment,
    diceId int,
    winningTeamId int,
    name varchar(100),
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId)
);


create table Topic
(
    topicId int primary key not null auto_increment,
    name varchar(100),
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId)
);


create table Term
(
    termId int primary key not null auto_increment,
    name varchar(100),
    correctGuesses int,
    appearances int,
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId)
);


create table UserDiceCorrelation
(
    corrID int primary key not null auto_increment,
    diceID  int,
    userID int,
    validFrom Date,
    validTo Date,
    foreign key (userID) references Users(userId),
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    enabled boolean,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId),
    foreign key (userID) references Users(userID),
    foreign key (diceID) references TimeflipDice(diceID)
);


create table RaspberryPI
(
    raspID int primary key not null,
    raspEAN  varchar(100),
    hardwareState enum ('DAMAGED', 'BEINGREPAIRED', 'READYTOUSE', 'LOST'),
    roomID int,
    foreign key (roomID) references Room(roomID),
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    enabled boolean,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId)
);


create table DiceField
(
    diceFieldID int primary key not null auto_increment,
    diceFieldIdentity  varchar(100),
    diceFieldDescribtion varchar(100),
    diceFieldName varchar(100),
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    enabled boolean,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId)
);


create table DiceFieldCorrelation
(
    fieldCorrID int primary key not null auto_increment,
    diceFieldID  int,
    validFrom Date,
    validTo Date,
    createUser int,
    createDate datetime,
    updateUser int,
    updateDate datetime,
    enabled boolean,
    foreign key (createUser) references Users(userId),
    foreign key (updateUser) references Users(userId),
    foreign key (diceFieldID) references DiceField(diceFieldID)
);

insert into Users values (1, 'Admin', '1234', true, 'Albert', 'Anfang', 'abc@gmx.at', 'ADMIN', '06641234567', null, '2021-03-04 00:00:01', null, null, null);
insert into Users values (2, 'user2', '1234', true, 'Berta', 'Bauer', 'abc@gmx.at', 'SPIELEVERWALTER', '06641234567', 1, '2021-03-04 00:00:01', null, null, 1);
insert into Users values (3, 'user3', '1234', true, 'Christop', 'Columbus', 'abc@gmx.at', 'SPIELEVERWALTER', '06641234567', 1, '2021-03-04 00:00:01', null, null, 2);
insert into Users values (4, 'user4', '1234', true, 'Daniel', 'Düsentrieb', 'abc@gmx.at', 'GASTNUTZER', '06641234567', 1, '2021-03-04 00:00:01', null, null, null);
insert into Users values (5, 'user5', '1234', true, 'Dagobert', 'Duck', 'abc@gmx.at', 'USER', '06641234567', 1, '2021-03-04 00:00:01', null, null, 1);
insert into Users values (6, 'PoohBear', '1234', true, 'Winne', 'Pooh', 'abc@gmx.at', 'USER', '06641234567', 1, '2021-03-04 00:00:01', null, null, 1);
insert into Users values (7, 'ForeverYoung', '1234', true, 'Peter', 'Pan', 'abc@gmx.at', 'USER', '06641234567', 1, '2021-03-04 00:00:01', null, null, 1);
insert into Users values (8, 'Mickey', '1234', true, 'Mickey', 'Mouse', 'abc@gmx.at', 'USER', '06641234567', 1, '2021-03-04 00:00:01', null, null, 2);


insert into TimeflipDice values (1,'ean1','DAMAGED',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into TimeflipDice values (2,'ean2','BEINGREPAIRED',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into TimeflipDice values (3,'ean3','READYTOUSE',2,'2020-07-07 06:00:01',2,'2020-07-07 06:00:01',true);
insert into TimeflipDice values (4,'ean4','LOST',2,'2020-07-07 06:00:01',2,'2020-07-07 06:00:01',true);
insert into TimeflipDice values (5,'ean5','LOST',2,'2020-07-07 06:00:01',2,'2020-07-07 06:00:01',true);


insert into UserDiceCorrelation values (1,1,1,'2020-07-07 06:00:01','2029-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into UserDiceCorrelation values (2,2,2,'2020-07-07 06:00:01','2029-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into UserDiceCorrelation values (3,3,3,'2020-07-07 06:00:01','2029-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into UserDiceCorrelation values (4,4,4,'2020-07-07 06:00:01','2029-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into UserDiceCorrelation values (5,5,5,'2020-07-07 06:00:01','2029-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);


insert into DiceField values(1,'EINS','EIN PUNKT','EINS',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(2,'ZWEI','ZWEI PUNKTE','ZWEI',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(3,'DREI','DREI PUNKTE','EINS',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(4,'VIER','VIER PUNKTE','VIER',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(5,'FUENF','FUENF PUNKTE','FUENF',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(6,'SECHS','SECHS PUNKTE','SECHS',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(7,'SIEBEN','SIEBEN PUNKTE','SIEBEN',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(8,'ACHT','ACHT PUNKTE','ACHT',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(9,'NEUN','NEUN PUNKTE','NEUN',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(10,'ZEHN','ZEHN PUNKTE','ZEHN',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(11,'ELF','ELF PUNKT','ELF',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
insert into DiceField values(12,'ZWOELF','ZWOELF PUNKT','ZWOELF',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);


#insert into DiceFieldCorrelation values(1,1,'2020-07-07 06:00:01','2020-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
#insert into DiceFieldCorrelation values(2,2,'2020-07-07 06:00:01','2020-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
#insert into DiceFieldCorrelation values(3,3,'2020-07-07 06:00:01','2020-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
#insert into DiceFieldCorrelation values(4,4,'2020-07-07 06:00:01','2020-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
#insert into DiceFieldCorrelation values(5,5,'2020-07-07 06:00:01','2020-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
#insert into DiceFieldCorrelation values(6,6,'2020-07-07 06:00:01','2020-07-07 06:00:01',1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);


#insert into RaspberryPI values(1,'EAN_R1','DAMAGED',1,1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
#insert into RaspberryPI values(2,'EAN_R2','BEINGREPAIRED',1,1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
#insert into RaspberryPI values(3,'EAN_R3','READYTOUSE',1,1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
#insert into RaspberryPI values(4,'EAN_R4','DAMAGED',1,1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);
#insert into RaspberryPI values(5,'EAN_R5','LOST',1,1,'2020-07-07 06:00:01',1,'2020-07-07 06:00:01',true);


insert into Room values(1, 'testroom1', 1, 2, 2, '2021-03-04 00:00:01', null, null, true);
insert into Room values(2, 'testroom2', 2, 3, 3, '2021-03-04 00:00:01', null, null, true);


insert into TEAM values(1, 0, 'testteam1', 'winners', 2, 1, 2, '2021-03-04 00:00:01', null, null);
insert into TEAM values(2, 0, 'testteam2', 'losers', 3, 1, 3, '2021-03-04 00:00:01', null, null);


insert into GAME values(1, 1, 1, 'testgame1', 2, '2021-03-04 00:00:01', null, null);


insert into TOPIC values(1, 'Geographie', 1, '2021-03-04 00:00:01', null, null);

insert into TERM values(1, 'Land sieht aus wie Stiefel', null, null, 1, '2021-03-04 00:00:01', null, null);
insert into TERM values(2, 'Herkunftsland von Andreas Hofer', null, null, 1, '2021-03-04 00:00:01', null, null);



