CREATE SCHEMA IF NOT EXISTS caloriesdb;

SET search_path TO caloriesdb, public;

CREATE SEQUENCE hibernate_sequence START WITH 1;

CREATE TABLE meal (
    id bigint NOT NULL PRIMARY KEY,
    description varchar NOT NULL,
    calories int,
    meal_date date NOT NULL,
    meal_time time,
    above_required boolean,
    user_id bigint NOT NULL
);

CREATE TABLE application_user (
    id bigint NOT NULL PRIMARY KEY,
    username varchar NOT NULL,
    password varchar NOT NULL,
    role varchar NOT NULL
);

ALTER TABLE meal
    ADD CONSTRAINT meal_user_fk FOREIGN KEY (user_id) REFERENCES application_user(id);

ALTER TABLE application_user
    ADD CONSTRAINT user_username_uk UNIQUE(username);