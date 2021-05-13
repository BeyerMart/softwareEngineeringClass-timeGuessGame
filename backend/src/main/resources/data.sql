-- USERS
INSERT IGNORE INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'admin.istrator@gmail.com', 'administ', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_ADMIN');
INSERT IGNORE INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'man.ager@gmail.com', 'manager', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_MANAGER');
INSERT IGNORE INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'susik@gmail.com', 'susiK', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT IGNORE INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'hansp@gmail.com', 'hansP', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT IGNORE INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'johannG@gmail.com', 'johannG', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT IGNORE INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'jamesJBaites@gmail.com', 'jamesJB', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT IGNORE INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'lisa.ritter@hotmail.com', 'lisaRitt', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');
INSERT IGNORE INTO user (created_at, updated_at, email, username, password, role) VALUES(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'makusFass@gmx.net', 'makkiF', '$2a$10$3Z6v.cUNfLxqpgGanZp.VeBMncNoiP5bW4HDNDvsWeAGf.OFzaKYu', 'ROLE_USER');

-- TOPIC
INSERT IGNORE INTO topic (id, created_at, name, updated_at, user_id) VALUES (1,CURRENT_TIMESTAMP(), 'Filme', CURRENT_TIMESTAMP(), (SELECT id from user WHERE username='administ'));
INSERT IGNORE INTO topic (id, created_at, name, updated_at, user_id) VALUES (2,CURRENT_TIMESTAMP(), 'Geographie', CURRENT_TIMESTAMP(), (SELECT id from user WHERE username='administ'));
INSERT IGNORE INTO topic (id, created_at, name, updated_at, user_id) VALUES (3,CURRENT_TIMESTAMP(), 'Geschichte', CURRENT_TIMESTAMP(), (SELECT id from user WHERE username='manager'));

-- TERM
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (1, 0, 0, CURRENT_TIMESTAMP(), 'Titanic', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (2, 0, 0, CURRENT_TIMESTAMP(), 'Ghostbusters', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (3, 0, 0, CURRENT_TIMESTAMP(), 'Batman', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (4, 0, 0, CURRENT_TIMESTAMP(), 'Shrek', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (5, 0, 0, CURRENT_TIMESTAMP(), 'Pulp Fiction', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (6, 0, 0, CURRENT_TIMESTAMP(), 'Fight Club', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (7, 0, 0, CURRENT_TIMESTAMP(), 'The Matrix', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (8, 0, 0, CURRENT_TIMESTAMP(), 'Kevin allein zu Haus', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (9, 0, 0, CURRENT_TIMESTAMP(), 'Toy Story', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (10, 0, 0, CURRENT_TIMESTAMP(), 'Inception', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (11, 0, 0, CURRENT_TIMESTAMP(), 'König der Löwen', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (12, 0, 0, CURRENT_TIMESTAMP(), 'Charlie und die Schokoladenfabrik', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (13, 0, 0, CURRENT_TIMESTAMP(), 'Iron Man', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (14, 0, 0, CURRENT_TIMESTAMP(), 'Spiderman', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (15, 0, 0, CURRENT_TIMESTAMP(), 'Django', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (16, 0, 0, CURRENT_TIMESTAMP(), 'Kung-Fu Panda', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (17, 0, 0, CURRENT_TIMESTAMP(), 'X-Men', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (18, 0, 0, CURRENT_TIMESTAMP(), 'Life of Pi', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (19, 0, 0, CURRENT_TIMESTAMP(), 'Herr der Ringe', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (20, 0, 0, CURRENT_TIMESTAMP(), 'Forrest Gump', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (21, 0, 0, CURRENT_TIMESTAMP(), 'Der Terminator', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (22, 0, 0, CURRENT_TIMESTAMP(), 'Harry Potter', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (23, 0, 0, CURRENT_TIMESTAMP(), 'Sherlock Holmes', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (24, 0, 0, CURRENT_TIMESTAMP(), 'Alice im Wunderland', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (25, 0, 0, CURRENT_TIMESTAMP(), 'Sie nannten ihn Mücke', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (26, 0, 0, CURRENT_TIMESTAMP(), 'Der Da Vinci Code', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (27, 0, 0, CURRENT_TIMESTAMP(), 'Robinson Crusoe', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (28, 0, 0, CURRENT_TIMESTAMP(), 'Twilight', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (29, 0, 0, CURRENT_TIMESTAMP(), 'Star Wars', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (30, 0, 0, CURRENT_TIMESTAMP(), 'Interstellar', CURRENT_TIMESTAMP(), 1);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (31, 0, 0, CURRENT_TIMESTAMP(), 'Asien', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (32, 0, 0, CURRENT_TIMESTAMP(), 'Afrika', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (33, 0, 0, CURRENT_TIMESTAMP(), 'Nordamerika', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (34, 0, 0, CURRENT_TIMESTAMP(), 'Südamerika', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (35, 0, 0, CURRENT_TIMESTAMP(), 'Antarktis', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (36, 0, 0, CURRENT_TIMESTAMP(), 'Europa', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (37, 0, 0, CURRENT_TIMESTAMP(), 'Australien', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (38, 0, 0, CURRENT_TIMESTAMP(), 'Österreich', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (39, 0, 0, CURRENT_TIMESTAMP(), 'Wien', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (40, 0, 0, CURRENT_TIMESTAMP(), 'Deutschland', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (41, 0, 0, CURRENT_TIMESTAMP(), 'Berlin', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (42, 0, 0, CURRENT_TIMESTAMP(), 'Belgien', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (43, 0, 0, CURRENT_TIMESTAMP(), 'Brüssel', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (44, 0, 0, CURRENT_TIMESTAMP(), 'Dänemark', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (45, 0, 0, CURRENT_TIMESTAMP(), 'Kopenhagen', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (46, 0, 0, CURRENT_TIMESTAMP(), 'Italien', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (47, 0, 0, CURRENT_TIMESTAMP(), 'Rom', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (48, 0, 0, CURRENT_TIMESTAMP(), 'Polen', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (49, 0, 0, CURRENT_TIMESTAMP(), 'Warschau', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (50, 0, 0, CURRENT_TIMESTAMP(), 'Spanien', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (51, 0, 0, CURRENT_TIMESTAMP(), 'Madrid', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (52, 0, 0, CURRENT_TIMESTAMP(), 'Türkei', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (53, 0, 0, CURRENT_TIMESTAMP(), 'Ankara', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (54, 0, 0, CURRENT_TIMESTAMP(), 'Vereinigtes Königreich', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (55, 0, 0, CURRENT_TIMESTAMP(), 'London', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (56, 0, 0, CURRENT_TIMESTAMP(), 'China', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (57, 0, 0, CURRENT_TIMESTAMP(), 'USA', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (58, 0, 0, CURRENT_TIMESTAMP(), 'Japan', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (59, 0, 0, CURRENT_TIMESTAMP(), 'Mexiko', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (60, 0, 0, CURRENT_TIMESTAMP(), 'Mittelmeer', CURRENT_TIMESTAMP(), 2);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (61, 0, 0, CURRENT_TIMESTAMP(), 'Steinzeit', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (62, 0, 0, CURRENT_TIMESTAMP(), 'Antike', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (63, 0, 0, CURRENT_TIMESTAMP(), 'Mittelalter', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (64, 0, 0, CURRENT_TIMESTAMP(), 'Dreißig Jähriger Krieg', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (65, 0, 0, CURRENT_TIMESTAMP(), 'Romantik', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (66, 0, 0, CURRENT_TIMESTAMP(), 'Französische Revolution', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (67, 0, 0, CURRENT_TIMESTAMP(), 'Erster Weltkrieg', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (68, 0, 0, CURRENT_TIMESTAMP(), 'Weimarer Republik', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (69, 0, 0, CURRENT_TIMESTAMP(), 'Nachkriegszeit', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (70, 0, 0, CURRENT_TIMESTAMP(), 'Zweiter Weltkrieg', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (71, 0, 0, CURRENT_TIMESTAMP(), 'Kalter Krieg', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (72, 0, 0, CURRENT_TIMESTAMP(), 'Pearl Harbor', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (73, 0, 0, CURRENT_TIMESTAMP(), 'Römisches Reich', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (74, 0, 0, CURRENT_TIMESTAMP(), 'Arabischer Frühling', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (75, 0, 0, CURRENT_TIMESTAMP(), 'Kubakrise', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (76, 0, 0, CURRENT_TIMESTAMP(), 'Berliner Mauer', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (77, 0, 0, CURRENT_TIMESTAMP(), 'Terroranschläge am 11. September 2001', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (78, 0, 0, CURRENT_TIMESTAMP(), 'NATO', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (79, 0, 0, CURRENT_TIMESTAMP(), 'Cäsar', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (80, 0, 0, CURRENT_TIMESTAMP(), 'Alexander der Große', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (81, 0, 0, CURRENT_TIMESTAMP(), 'DDR', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (82, 0, 0, CURRENT_TIMESTAMP(), 'UDSSR', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (83, 0, 0, CURRENT_TIMESTAMP(), 'Gladiatorenkampf', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (84, 0, 0, CURRENT_TIMESTAMP(), 'Donald Trump', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (85, 0, 0, CURRENT_TIMESTAMP(), 'Industrialisierung', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (86, 0, 0, CURRENT_TIMESTAMP(), 'Karl Marx', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (87, 0, 0, CURRENT_TIMESTAMP(), 'Ost-West-Konflikt', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (88, 0, 0, CURRENT_TIMESTAMP(), 'Saddam Hussein', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (89, 0, 0, CURRENT_TIMESTAMP(), 'Muammar al-Gaddafi', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (90, 0, 0, CURRENT_TIMESTAMP(), 'Mao Zedong', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (91, 0, 0, CURRENT_TIMESTAMP(), 'Benito Mussolini', CURRENT_TIMESTAMP(), 3);
INSERT IGNORE INTO term (id, appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (92, 0, 0, CURRENT_TIMESTAMP(), 'Josef Stalin', CURRENT_TIMESTAMP(), 3);

-- GAME
INSERT IGNORE INTO game (id, created_at, name, updated_at, winning_team_id, topic_id, max_points) VALUES (1, CURRENT_TIMESTAMP(), 'Game 1', CURRENT_TIMESTAMP(), NULL, (SELECT id FROM topic WHERE name='Geschichte'),0);
INSERT IGNORE INTO game (id, created_at, name, updated_at, winning_team_id, topic_id, max_points) VALUES (2, CURRENT_TIMESTAMP(), 'Game 2', CURRENT_TIMESTAMP(), NULL, (SELECT id FROM topic WHERE name='Filme'),0);
INSERT IGNORE INTO game (id, created_at, name, updated_at, winning_team_id, topic_id, max_points) VALUES (3, CURRENT_TIMESTAMP(), 'Game 3', CURRENT_TIMESTAMP(), NULL, (SELECT id FROM topic WHERE name='Geographie'),0);

-- TEAM
INSERT IGNORE INTO team (id, name, points, created_at, updated_at, game_id) VALUES (1, 'Ball of Duty', 16, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (SELECT id FROM game WHERE name='Game 1'));
INSERT IGNORE INTO team (id, name, points, created_at, updated_at, game_id) VALUES (2, 'Win or Booze', 12, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (SELECT id FROM game WHERE name='Game 2'));
INSERT IGNORE INTO team (id, name, points, created_at, updated_at, game_id) VALUES (3, 'Goal Digger', 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (SELECT id FROM game WHERE name='Game 3'));

-- UPDATE GAME
UPDATE game SET winning_team_id = (SELECT id FROM team WHERE name='Ball of Duty') WHERE name='Game 1';
UPDATE game SET winning_team_id = (SELECT id FROM team WHERE name='Win or Booze') WHERE name='Game 2';
UPDATE game SET winning_team_id = (SELECT id FROM team WHERE name='Goal Digger') WHERE name='Game 3';

-- USER TEAM
INSERT IGNORE INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Ball of Duty'), (SELECT id FROM user WHERE username='hansP'));
INSERT IGNORE INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Ball of Duty'), (SELECT id FROM user WHERE username='susiK'));
INSERT IGNORE INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Win or Booze'), (SELECT id FROM user WHERE username='johannG'));
INSERT IGNORE INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Win or Booze'), (SELECT id FROM user WHERE username='makkiF'));
INSERT IGNORE INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Goal Digger'), (SELECT id FROM user WHERE username='lisaRitt'));
INSERT IGNORE INTO user_team (team_id, user_id) VALUES ((SELECT id FROM team WHERE name='Goal Digger'), (SELECT id FROM user WHERE username='jamesJB'));