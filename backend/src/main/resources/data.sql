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
INSERT INTO topic (created_at, name, updated_at, user_id) VALUES (CURRENT_TIMESTAMP(), 'Filme', CURRENT_TIMESTAMP(), (SELECT id from user WHERE username='administ'));
INSERT INTO topic (created_at, name, updated_at, user_id) VALUES (CURRENT_TIMESTAMP(), 'Geographie', CURRENT_TIMESTAMP(), (SELECT id from user WHERE username='administ'));
INSERT INTO topic (created_at, name, updated_at, user_id) VALUES (CURRENT_TIMESTAMP(), 'Geschichte', CURRENT_TIMESTAMP(), (SELECT id from user WHERE username='manager'));

-- TERM
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Titanic', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Ghostbusters', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Batman', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Shrek', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Pulp Fiction', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Fight Club', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'The Matrix', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Kevin allein zu Haus', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Toy Story', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Inception', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'König der Löwen', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Charlie und die Schokoladenfabrik', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Iron Man', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Spiderman', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Django', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Kung-Fu Panda', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'X-Men', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Life of Pi', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Herr der Ringe', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Forrest Gump', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Der Terminator', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Harry Potter', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Sherlock Holmes', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Alice im Wunderland', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Sie nannten ihn Mücke', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Der Da Vinci Code', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Robinson Crusoe', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Twilight', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Star Wars', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Interstellar', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Filme'));

INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Asien', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Afrika', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Nordamerika', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Südamerika', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Antarktis', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Europa', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Australien', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Österreich', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Wien', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Deutschland', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Berlin', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Belgien', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Brüssel', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Dänemark', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Kopenhagen', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Italien', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Rom', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Polen', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Warschau', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Spanien', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Madrid', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Türkei', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Ankara', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Vereinigtes Königreich', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'London', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'China', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'USA', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Japan', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Mexiko', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Mittelmeer', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geographie'));

INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Steinzeit', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Antike', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Mittelalter', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Dreißig Jähriger Krieg', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Romantik', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Französische Revolution', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Erster Weltkrieg', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Weimarer Republik', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Nachkriegszeit', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Zweiter Weltkrieg', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Kalter Krieg', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Pearl Harbor', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Römisches Reich', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Arabischer Frühling', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Kubakrise', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Berliner Mauer', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Terroranschläge am 11. September 2001', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'NATO', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Cäsar', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Alexander der Große', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'DDR', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'UDSSR', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Gladiatorenkampf', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Donald Trump', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Industrialisierung', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Karl Marx', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Ost-West-Konflikt', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Saddam Hussein', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Muammar al-Gaddafi', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Mao Zedong', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Benito Mussolini', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
INSERT INTO term (appearances, correct_guesses, created_at, name, updated_at, topic_id) VALUES (0, 0, CURRENT_TIMESTAMP(), 'Josef Stalin', CURRENT_TIMESTAMP(), (SELECT id FROM topic WHERE name='Geschichte'));
-- GAME
INSERT INTO game (created_at, name, updated_at, winning_team_id, topic_id, max_points) VALUES (CURRENT_TIMESTAMP(), 'Game 1', CURRENT_TIMESTAMP(), NULL, (SELECT id FROM topic WHERE name='Geschichte'),0);
INSERT INTO game (created_at, name, updated_at, winning_team_id, topic_id, max_points) VALUES (CURRENT_TIMESTAMP(), 'Game 2', CURRENT_TIMESTAMP(), NULL, (SELECT id FROM topic WHERE name='Filme'),0);
INSERT INTO game (created_at, name, updated_at, winning_team_id, topic_id, max_points) VALUES (CURRENT_TIMESTAMP(), 'Game 3', CURRENT_TIMESTAMP(), NULL, (SELECT id FROM topic WHERE name='Geographie'),0);

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