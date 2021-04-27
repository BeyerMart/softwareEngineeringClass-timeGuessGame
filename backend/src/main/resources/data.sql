-- USERS
INSERT INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'admin.istrator@gmail.com', 'administ', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_ADMIN');
INSERT INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'man.ager@gmail.com', 'manager', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_MANAGER');
INSERT INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'susik@gmail.com', 'susiK', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'hansp@gmail.com', 'hansP', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'johannG@gmail.com', 'johannG', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'jamesJBaites@gmail.com', 'jamesJB', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'lisa.ritter@hotmail.com', 'lisaRitt', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'makusFass@gmx.net', 'makkiF', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');

-- TOPIC
INSERT INTO topic (created_at, name, updated_at, user_id) VALUES (CURRENT_TIMESTAMP(), 'Biologie', CURRENT_TIMESTAMP(), (SELECT id from user WHERE username='administ'));
INSERT INTO topic (created_at, name, updated_at, user_id) VALUES (CURRENT_TIMESTAMP(), 'Geographie', CURRENT_TIMESTAMP(), (SELECT id from user WHERE username='administ'));
INSERT INTO topic (created_at, name, updated_at, user_id) VALUES (CURRENT_TIMESTAMP(), 'Geschichte', CURRENT_TIMESTAMP(), (SELECT id from user WHERE username='manager'));

-- TERM
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (20, 10, CURRENT_TIMESTAMP(), 'Herz', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Biologie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (15, 5, CURRENT_TIMESTAMP(), 'Bauch', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Biologie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (12, 3, CURRENT_TIMESTAMP(), 'Augenlid', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Biologie'));

INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (40, 35, CURRENT_TIMESTAMP(), 'Afrika', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (30, 20, CURRENT_TIMESTAMP(), 'Wien', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (20, 5, CURRENT_TIMESTAMP(), 'Österreich', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));

INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (20, 12, CURRENT_TIMESTAMP(), 'Angela Merkel', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (59, 15, CURRENT_TIMESTAMP(), 'Cäsar', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (30, 20, CURRENT_TIMESTAMP(), 'Andreas Hofer', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));

-- GAME
INSERT INTO game (created_at, name, updated_at, winning_team_id, topic_id) VALUES (CURRENT_TIMESTAMP(), 'Game 1', CURRENT_TIMESTAMP(), NULL, (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO game (created_at, name, updated_at, winning_team_id, topic_id) VALUES (CURRENT_TIMESTAMP(), 'Game 2', CURRENT_TIMESTAMP(), NULL, (SELECT id FROM topic WHERE name='Biologie'));
INSERT INTO game (created_at, name, updated_at, winning_team_id, topic_id) VALUES (CURRENT_TIMESTAMP(), 'Game 3', CURRENT_TIMESTAMP(), NULL, (SELECT id FROM topic WHERE name='Geographie'));

-- TEAM
INSERT INTO team (id, name, points, created_at, updated_at, game_id) VALUES (1, 'Ball of Duty', 16, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (SELECT id FROM game WHERE name='Game 1'));
INSERT INTO team (id, name, points, created_at, updated_at, game_id) VALUES (2, 'Win or Booze', 12, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (SELECT id FROM game WHERE name='Game 2'));
INSERT INTO team (id, name, points, created_at, updated_at, game_id) VALUES (3, 'Goal Digger', 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (SELECT id FROM game WHERE name='Game 3'));

-- UPDATE GAME
UPDATE game SET winning_team_id = (SELECT id FROM team WHERE name='Ball of Duty') WHERE name='Game 1';
UPDATE game SET winning_team_id = (SELECT id FROM team WHERE name='Win or Booze') WHERE name='Game 2';
UPDATE game SET winning_team_id = (SELECT id FROM team WHERE name='Goal Digger') WHERE name='Game 3';


-- USER TEAM
INSERT INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Ball of Duty'), (SELECT id FROM user WHERE username='hansP'));
INSERT INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Ball of Duty'), (SELECT id FROM user WHERE username='susiK'));
INSERT INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Win or Booze'), (SELECT id FROM user WHERE username='johannG'));
INSERT INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Win or Booze'), (SELECT id FROM user WHERE username='makkiF'));
INSERT INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Goal Digger'), (SELECT id FROM user WHERE username='lisaRitt'));
INSERT INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Goal Digger'), (SELECT id FROM user WHERE username='jamesJB'));
