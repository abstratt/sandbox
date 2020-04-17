SET search_path TO caloriesdb, public;

INSERT INTO application_user (id, username, password, role) VALUES (nextval('hibernate_sequence'), 'regular01', 'pass', 'Regular');
INSERT INTO application_user (id, username, password, role) VALUES (nextval('hibernate_sequence'), 'regular02', 'pass', 'Regular');
INSERT INTO application_user (id, username, password, role) VALUES (nextval('hibernate_sequence'), 'manager01', 'pass', 'Manager');
INSERT INTO application_user (id, username, password, role) VALUES (nextval('hibernate_sequence'), 'manager02', 'pass', 'Manager');
INSERT INTO application_user (id, username, password, role) VALUES (nextval('hibernate_sequence'), 'admin01', 'pass', 'Administrator');
INSERT INTO application_user (id, username, password, role) VALUES (nextval('hibernate_sequence'), 'admin02', 'pass', 'Administrator');


--INSERT INTO meal (id, description, calories, meal_date, meal_time, user_id) VALUES (nextval('hibernate_sequence'), '2 large pizzas', null, '2017-02-01', '14:34', (SELECT min(id) FROM application_user WHERE role = 'Regular'))