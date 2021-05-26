-- The first table we create is the categories table
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

-- Inserting data into categories
INSERT INTO categories (name) VALUES ('UNKNOWN');
INSERT INTO categories (name) VALUES ('DOCUMENTARY');
INSERT INTO categories (name) VALUES ('NEWS');
INSERT INTO categories (name) VALUES ('TOPICALITY');
INSERT INTO categories (name) VALUES ('MAGAZINE');
INSERT INTO categories (name) VALUES ('CULTURE_AND_NATURE');
INSERT INTO categories (name) VALUES ('DRAMA');
INSERT INTO categories (name) VALUES ('TV_SERIES');
INSERT INTO categories (name) VALUES ('ENTERTAINMENT');
INSERT INTO categories (name) VALUES ('MUSIC');
INSERT INTO categories (name) VALUES ('KIDS');
INSERT INTO categories (name) VALUES ('REGIONAL_PROGRAMME');
INSERT INTO categories (name) VALUES ('SPORT');
INSERT INTO categories (name) VALUES ('MOVIE');

-- Index on categories
CREATE UNIQUE INDEX categories_category_uindex ON categories (name ASC);



-- Next we create the function types a person can have
CREATE TABLE function_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

-- Inserting data into function_types
INSERT INTO function_types (name) VALUES ('ACTOR');
INSERT INTO function_types (name) VALUES ('PICTURE_ARTIST');
INSERT INTO function_types (name) VALUES ('PICTURE_SOUND_EDIT');
INSERT INTO function_types (name) VALUES ('CASTING');
INSERT INTO function_types (name) VALUES ('COLORGRADING');
INSERT INTO function_types (name) VALUES ('CONDUCTOR');
INSERT INTO function_types (name) VALUES ('DRONE_PILOT');
INSERT INTO function_types (name) VALUES ('DOLL_OPERATOR');
INSERT INTO function_types (name) VALUES ('DOLL_CREATOR');
INSERT INTO function_types (name) VALUES ('NARRATOR');
INSERT INTO function_types (name) VALUES ('CAMERAGUY');
INSERT INTO function_types (name) VALUES ('SOURCE_IDEA');
INSERT INTO function_types (name) VALUES ('SOURCE');
INSERT INTO function_types (name) VALUES ('GRAPHIC_DESIGN');
INSERT INTO function_types (name) VALUES ('SPEAKERS');
INSERT INTO function_types (name) VALUES ('CONDUCTOR_MASTER');
INSERT INTO function_types (name) VALUES ('CUTTER');
INSERT INTO function_types (name) VALUES ('CONCEPT');
INSERT INTO function_types (name) VALUES ('CONSULTANT');
INSERT INTO function_types (name) VALUES ('CHOIR');
INSERT INTO function_types (name) VALUES ('CHOREOGRAPHY');
INSERT INTO function_types (name) VALUES ('SOUND_MASTER');
INSERT INTO function_types (name) VALUES ('SOUND_EDIT');
INSERT INTO function_types (name) VALUES ('CONTRIBUTOR');
INSERT INTO function_types (name) VALUES ('MUSICAL_ARRANGEMENT');
INSERT INTO function_types (name) VALUES ('ORCHESTRA');
INSERT INTO function_types (name) VALUES ('TRANSLATOR');
INSERT INTO function_types (name) VALUES ('PRODUCER');
INSERT INTO function_types (name) VALUES ('PRODUCTION_LEAD');
INSERT INTO function_types (name) VALUES ('PROGRAM_MANAGEMENT');
INSERT INTO function_types (name) VALUES ('EDITORIAL_OFFICE');
INSERT INTO function_types (name) VALUES ('EDITOR');
INSERT INTO function_types (name) VALUES ('REQUISITOR');
INSERT INTO function_types (name) VALUES ('SET_DESIGNER');
INSERT INTO function_types (name) VALUES ('SCRIPTER');
INSERT INTO function_types (name) VALUES ('SPECIAL_EFFECTS');
INSERT INTO function_types (name) VALUES ('SPONSOR');
INSERT INTO function_types (name) VALUES ('ANIMATION');
INSERT INTO function_types (name) VALUES ('TEXTER');
INSERT INTO function_types (name) VALUES ('TEXT_AND_MUSIC');
INSERT INTO function_types (name) VALUES ('UN_HONOURED');
INSERT INTO function_types (name) VALUES ('UNKNOWN');

-- Index on function_types
CREATE UNIQUE INDEX function_types_name_uindex ON function_types (name ASC);



-- Creating the producers table
CREATE TABLE producers (
    id SERIAL PRIMARY KEY,
    company VARCHAR(50) NOT NULL
);

-- Inserting data into producers
INSERT INTO producers (id, company) VALUES (2, 'Nordisk Film');
INSERT INTO producers (id, company) VALUES (3, 'Metronome');
INSERT INTO producers (id, company) VALUES (4, 'TV 2');

-- Setting sequence for producers so the next serial starts from '5'
ALTER SEQUENCE producers_id_seq RESTART WITH 5;



-- Creating the programmes table
CREATE TABLE programmes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    category INTEGER REFERENCES categories(id),
    channel VARCHAR(50) NOT NULL,
    aireddate DATE NOT NULL
);

-- Inserting data into programmes
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (14, 'Stormester', 9, 'TV 2', '2021-05-11');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (25, 'Familien fra Bryggen', 9, 'TV 3', '2019-10-04');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (16, 'Olsen Banden på Sporet', 14, 'TV 2 Charlie', '2017-05-11');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (26, 'Luksusfælden', 9, 'TV 3', '2021-02-12');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (15, 'Olsen Banden i Jylland', 14, 'TV 2 Charlie', '2016-04-29');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (20, 'Olsen Bandens Store Kup', 14, 'TV 2 Charlie', '2018-05-10');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (27, 'Lykkehjulet', 9, 'TV 2 Charlie', '2020-01-17');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (21, 'Politijagt', 9, 'Discovery Plus', '2020-04-15');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (28, 'Natholdet', 9, 'TV 2', '2021-04-22');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (29, 'GO'' Morgen Danmark', 3, 'TV 2', '2021-05-24');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (42, 'Jo Færre Jo Bedre', 9, 'TV 2', '2021-04-29');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (30, 'GO'' Aften Danmark', 3, 'TV 2', '2019-05-09');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (23, 'Ex on the beach', 9, 'Kanal 4', '2018-08-09');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (43, 'Sygeplejeskolen', 8, 'TV 2 Charlie', '2019-08-15');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (31, 'MasterChef', 9, 'TV 3', '2021-05-04');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (24, 'Nybyggerne', 6, 'TV 2', '2015-08-13');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (44, 'Rita', 8, 'TV 2', '2020-07-20');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (18, 'Badehotellet', 9, 'TV 2', '2019-05-03');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (45, 'Anna Pihl', 8, 'TV 2', '2008-03-10');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (32, 'Ultra Snydt', 3, 'DR Ultra', '2019-07-26');
INSERT INTO programmes (id, name, category, channel, aireddate) VALUES (22, 'Klipfiskerne', 9, 'TV 2', '2021-03-11');

-- Setting the sequence for programmes so the next serial starts from '46'
ALTER SEQUENCE programmes_id_seq RESTART WITH 46;



-- Creating the persons table
CREATE TABLE persons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    birthdate DATE,
    description TEXT
);

-- Inserting data into persons
INSERT INTO persons (id, name, birthdate, description) VALUES (97, 'Lars Daniel Terkelsen', '1968-12-10', 'Dansk komponist');
INSERT INTO persons (id, name, birthdate, description) VALUES (98, 'Charlotte Munck', '1969-12-02', 'Dansk skuespillerinde');
INSERT INTO persons (id, name, birthdate, description) VALUES (37, 'Lasse Rimmer', '1985-06-05', 'Han er kendt som stormesteren i stormester. I øvrigt er han komiker.');
INSERT INTO persons (id, name, birthdate, description) VALUES (38, 'Mark Le Fevre', '1984-06-13', 'Kendt som bassemanden');
INSERT INTO persons (id, name, birthdate, description) VALUES (41, 'Mortnen Grundwald', '1993-05-06', 'Kendt som Benny fra Olsen Banden');
INSERT INTO persons (id, name, birthdate, description) VALUES (45, 'Bjørn Watt Boolsen', '1993-05-06', 'Kendt som Bang Johansen fra Olsen Banden');
INSERT INTO persons (id, name, birthdate, description) VALUES (48, 'Helle Virkner', '1925-09-15', 'Dansk Skuespillerinde');
INSERT INTO persons (id, name, birthdate, description) VALUES (49, 'Amalie Dollerup', '1986-04-15', 'Skuespiller');
INSERT INTO persons (id, name, birthdate, description) VALUES (50, 'Lars Ranthe', '1969-08-26', 'Dansk skuespiler');
INSERT INTO persons (id, name, birthdate, description) VALUES (51, 'Vlado Lentz', '1960-05-02', 'Politibetjent');
INSERT INTO persons (id, name, birthdate, description) VALUES (52, 'Henrik Haensel', '1975-03-07', 'Færdsels politibetjent');
INSERT INTO persons (id, name, birthdate, description) VALUES (54, 'Thomas Warberg', '1985-06-11', 'Dansk komiker fra Toftlund i Sønderjylland.');
INSERT INTO persons (id, name, birthdate, description) VALUES (55, 'Nikolaj Stokholm', '1990-01-29', 'Dansk stand-up komiker og talkshow-vært.');
INSERT INTO persons (id, name, birthdate, description) VALUES (58, 'Frederik Edén', '1982-09-18', 'Dansk producent');
INSERT INTO persons (id, name, birthdate, description) VALUES (59, 'Dan Andersen', '1979-09-04', 'Dansk stand-up komiker, musiker og podcaster');
INSERT INTO persons (id, name, birthdate, description) VALUES (60, 'Amanda Mogensen', '1995-11-17', 'Realitystjerne');
INSERT INTO persons (id, name, birthdate, description) VALUES (62, 'Mette Helena Rasmussen', '1983-03-12', 'Boligstylist og indretningsekspert');
INSERT INTO persons (id, name, birthdate, description) VALUES (63, 'Ask Abildgaard', '1978-12-23', 'Tømrer og bygningskonstruktør');
INSERT INTO persons (id, name, birthdate, description) VALUES (64, 'Linse Kessler', '1966-03-31', 'Dansk tv-personlighed, model, skuespiller og forretningskvinde');
INSERT INTO persons (id, name, birthdate, description) VALUES (65, 'Stephanie Karma Salvarli', '1990-04-14', 'Kendt som "Geggo". Dansk tv-personlighed og blogger.');
INSERT INTO persons (id, name, birthdate, description) VALUES (66, 'Mikkel Kessler', '1979-03-01', 'Professionel bokser');
INSERT INTO persons (id, name, birthdate, description) VALUES (67, 'Mette Reissmann', '1963-10-04', 'Erhvervsjurist, tv-vært og politiker');
INSERT INTO persons (id, name, birthdate, description) VALUES (68, 'Carsten Linnemann', '1976-04-30', 'Egendomsmægler');
INSERT INTO persons (id, name, birthdate, description) VALUES (69, 'Ole Koefoed', '1960-05-05', 'Dansk scenograf');
INSERT INTO persons (id, name, birthdate, description) VALUES (70, 'Mikkel Kryger Rasmussen', '1983-03-05', 'Dansk tv-vært');
INSERT INTO persons (id, name, birthdate, description) VALUES (71, 'Anders Breinholt', '1972-11-15', 'Dansk autodidakt journalist, radio- og tv-vært');
INSERT INTO persons (id, name, birthdate, description) VALUES (72, 'Puk Elgård', '1968-02-03', 'Dansk journalist, radio- og TV-vært samt forfatter');
INSERT INTO persons (id, name, birthdate, description) VALUES (61, 'Christian Degn', '1978-08-03', 'Kendt dansk tv vært');
INSERT INTO persons (id, name, birthdate, description) VALUES (73, 'Jes Dorph-Petersen', '1959-03-23', 'Dansk journalist');
INSERT INTO persons (id, name, birthdate, description) VALUES (74, 'Felix Smith', '1976-10-12', 'dansk radio- og tv-vært');
INSERT INTO persons (id, name, birthdate, description) VALUES (75, 'Thomas Castberg', '1975-07-15', 'Kok');
INSERT INTO persons (id, name, birthdate, description) VALUES (76, 'Jakob Mielcke', '1977-03-20', 'Kok');
INSERT INTO persons (id, name, birthdate, description) VALUES (77, 'Mahamad Habane', '1991-06-06', 'Dansk-somalisk standupkomiker');
INSERT INTO persons (id, name, birthdate, description) VALUES (90, 'Steen Langeberg', '1976-10-18', 'Dansk journalist og tv-vært');
INSERT INTO persons (id, name, birthdate, description) VALUES (91, 'Marie Tangaa', '1979-11-12', 'Hende der holder styr på svarene');
INSERT INTO persons (id, name, birthdate, description) VALUES (92, 'Morten Hee Andersen', '1991-05-22', 'Dansk skuespiller');
INSERT INTO persons (id, name, birthdate, description) VALUES (93, 'Molly Maria Blixt Hasselflug-Egelind', '1987-11-20', 'Dansk skuespillerinde');
INSERT INTO persons (id, name, birthdate, description) VALUES (94, 'Mille Dinesen', '1974-03-17', 'Dansk skuespillerinde');
INSERT INTO persons (id, name, birthdate, description) VALUES (95, 'Carsten Bjørnlund', '1973-06-28', 'Dansk skuespiller, uddannet fra Statens Teaterskole i 2002.');
INSERT INTO persons (id, name, birthdate, description) VALUES (96, 'Lars Kaalund', '1964-05-29', 'Dansk skuespiller, instruktør, manuskriptforfatter, dramatiker og teaterdirektør');
INSERT INTO persons (id, name, birthdate, description) VALUES (44, 'Mette Blomsterberg', '1970-08-06', 'Er en dygtig bager');
INSERT INTO persons (id, name, birthdate, description) VALUES (39, 'Ove Sprogoe', '1919-12-21', 'Kendt som Egon Olsen');
INSERT INTO persons (id, name, birthdate, description) VALUES (42, 'Kirsten Walter', '1933-08-31', 'Kendt som Yvonne fra Olsen Banden');
INSERT INTO persons (id, name, birthdate, description) VALUES (46, 'Ole Ernst', '1940-05-16', 'Kendt som politiassitent Holm');
INSERT INTO persons (id, name, birthdate, description) VALUES (40, 'Poul Bundgård', '1992-10-27', 'Kendt som Kjeld fra Olsen Banden');
INSERT INTO persons (id, name, birthdate, description) VALUES (43, 'Erik Balling', '1924-11-29', 'Har lavet Olsen Banden');
INSERT INTO persons (id, name, birthdate, description) VALUES (56, 'Ida Erbs Falkesgaard', '1970-07-23', 'Produktionsleder hos Metronome Productions A/S');

-- Setting the sequence for persons so the next serial starts from '99'
ALTER SEQUENCE persons_id_seq RESTART WITH 99;



-- Creating the producer_list table
CREATE TABLE producer_list (
    id SERIAL PRIMARY KEY,
    programme INTEGER REFERENCES programmes(id) ON DELETE CASCADE,
    producer INTEGER REFERENCES producers(id) ON DELETE CASCADE
);

-- Inserting data into producer_list
INSERT INTO producer_list (id, programme, producer) VALUES (14, 14, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (15, 15, 2);
INSERT INTO producer_list (id, programme, producer) VALUES (26, 16, 2);
INSERT INTO producer_list (id, programme, producer) VALUES (28, 18, 4);
INSERT INTO producer_list (id, programme, producer) VALUES (30, 20, 2);
INSERT INTO producer_list (id, programme, producer) VALUES (31, 21, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (32, 22, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (33, 23, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (34, 24, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (35, 25, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (36, 26, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (37, 27, 2);
INSERT INTO producer_list (id, programme, producer) VALUES (41, 31, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (42, 32, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (52, 42, 3);
INSERT INTO producer_list (id, programme, producer) VALUES (53, 43, 4);
INSERT INTO producer_list (id, programme, producer) VALUES (54, 44, 4);
INSERT INTO producer_list (id, programme, producer) VALUES (55, 45, 4);

-- Setting the sequence for producer_list so the next serial starts from '56'
ALTER SEQUENCE producer_list_id_seq RESTART WITH 56;

-- Index on producer_list
CREATE INDEX producer_list_programme_index ON producer_list (programme ASC);



-- Creating the credits table
CREATE TABLE credits (
    id SERIAL PRIMARY KEY,
    person INTEGER NOT NULL REFERENCES persons(id) ON DELETE CASCADE,
    function_type INTEGER NOT NULL REFERENCES function_types(id)
);

-- Inserting data into credits
INSERT INTO credits (id, person, function_type) VALUES (67, 40, 1);
INSERT INTO credits (id, person, function_type) VALUES (69, 43, 32);
INSERT INTO credits (id, person, function_type) VALUES (71, 38, 4);
INSERT INTO credits (id, person, function_type) VALUES (73, 37, 5);
INSERT INTO credits (id, person, function_type) VALUES (74, 45, 1);
INSERT INTO credits (id, person, function_type) VALUES (75, 37, 1);
INSERT INTO credits (id, person, function_type) VALUES (76, 44, 6);
INSERT INTO credits (id, person, function_type) VALUES (77, 40, 1);
INSERT INTO credits (id, person, function_type) VALUES (78, 43, 28);
INSERT INTO credits (id, person, function_type) VALUES (79, 46, 1);
INSERT INTO credits (id, person, function_type) VALUES (80, 38, 5);
INSERT INTO credits (id, person, function_type) VALUES (82, 48, 1);
INSERT INTO credits (id, person, function_type) VALUES (85, 39, 1);
INSERT INTO credits (id, person, function_type) VALUES (86, 41, 1);
INSERT INTO credits (id, person, function_type) VALUES (87, 49, 1);
INSERT INTO credits (id, person, function_type) VALUES (88, 50, 1);
INSERT INTO credits (id, person, function_type) VALUES (89, 51, 24);
INSERT INTO credits (id, person, function_type) VALUES (90, 52, 24);
INSERT INTO credits (id, person, function_type) VALUES (91, 37, 24);
INSERT INTO credits (id, person, function_type) VALUES (93, 54, 24);
INSERT INTO credits (id, person, function_type) VALUES (94, 55, 24);
INSERT INTO credits (id, person, function_type) VALUES (97, 58, 29);
INSERT INTO credits (id, person, function_type) VALUES (98, 59, 15);
INSERT INTO credits (id, person, function_type) VALUES (99, 60, 24);
INSERT INTO credits (id, person, function_type) VALUES (100, 61, 24);
INSERT INTO credits (id, person, function_type) VALUES (101, 62, 24);
INSERT INTO credits (id, person, function_type) VALUES (102, 63, 24);
INSERT INTO credits (id, person, function_type) VALUES (103, 64, 24);
INSERT INTO credits (id, person, function_type) VALUES (104, 65, 24);
INSERT INTO credits (id, person, function_type) VALUES (105, 66, 24);
INSERT INTO credits (id, person, function_type) VALUES (106, 67, 24);
INSERT INTO credits (id, person, function_type) VALUES (107, 68, 24);
INSERT INTO credits (id, person, function_type) VALUES (108, 69, 34);
INSERT INTO credits (id, person, function_type) VALUES (109, 70, 24);
INSERT INTO credits (id, person, function_type) VALUES (110, 71, 24);
INSERT INTO credits (id, person, function_type) VALUES (111, 38, 24);
INSERT INTO credits (id, person, function_type) VALUES (112, 69, 34);
INSERT INTO credits (id, person, function_type) VALUES (113, 64, 24);
INSERT INTO credits (id, person, function_type) VALUES (114, 37, 24);
INSERT INTO credits (id, person, function_type) VALUES (115, 55, 24);
INSERT INTO credits (id, person, function_type) VALUES (116, 72, 24);
INSERT INTO credits (id, person, function_type) VALUES (117, 73, 24);
INSERT INTO credits (id, person, function_type) VALUES (118, 74, 24);
INSERT INTO credits (id, person, function_type) VALUES (119, 75, 24);
INSERT INTO credits (id, person, function_type) VALUES (120, 66, 7);
INSERT INTO credits (id, person, function_type) VALUES (121, 76, 24);
INSERT INTO credits (id, person, function_type) VALUES (122, 59, 31);
INSERT INTO credits (id, person, function_type) VALUES (123, 77, 24);
INSERT INTO credits (id, person, function_type) VALUES (127, 39, 31);
INSERT INTO credits (id, person, function_type) VALUES (138, 38, 5);
INSERT INTO credits (id, person, function_type) VALUES (141, 38, 2);
INSERT INTO credits (id, person, function_type) VALUES (142, 90, 24);
INSERT INTO credits (id, person, function_type) VALUES (143, 91, 24);
INSERT INTO credits (id, person, function_type) VALUES (144, 92, 1);
INSERT INTO credits (id, person, function_type) VALUES (145, 93, 1);
INSERT INTO credits (id, person, function_type) VALUES (146, 94, 1);
INSERT INTO credits (id, person, function_type) VALUES (147, 95, 1);
INSERT INTO credits (id, person, function_type) VALUES (148, 96, 32);
INSERT INTO credits (id, person, function_type) VALUES (149, 97, 25);
INSERT INTO credits (id, person, function_type) VALUES (150, 98, 1);
INSERT INTO credits (id, person, function_type) VALUES (151, 56, 29);

-- Setting the sequence for credits so the next serial starts from '152'
ALTER SEQUENCE credits_id_seq RESTART WITH 152;



-- Creating the credits_list table
CREATE TABLE credits_list (
    id SERIAL PRIMARY KEY,
    credit INTEGER REFERENCES credits(id) ON DELETE CASCADE,
    programme INTEGER REFERENCES programmes(id) ON DELETE CASCADE
);

-- Inserting data into credits_list
INSERT INTO credits_list (id, credit, programme) VALUES (121, 142, 42);
INSERT INTO credits_list (id, credit, programme) VALUES (2, 67, 15);
INSERT INTO credits_list (id, credit, programme) VALUES (122, 143, 42);
INSERT INTO credits_list (id, credit, programme) VALUES (4, 69, 15);
INSERT INTO credits_list (id, credit, programme) VALUES (123, 144, 43);
INSERT INTO credits_list (id, credit, programme) VALUES (6, 71, 14);
INSERT INTO credits_list (id, credit, programme) VALUES (124, 145, 43);
INSERT INTO credits_list (id, credit, programme) VALUES (8, 73, 15);
INSERT INTO credits_list (id, credit, programme) VALUES (9, 74, 16);
INSERT INTO credits_list (id, credit, programme) VALUES (10, 75, 14);
INSERT INTO credits_list (id, credit, programme) VALUES (11, 76, 18);
INSERT INTO credits_list (id, credit, programme) VALUES (125, 146, 44);
INSERT INTO credits_list (id, credit, programme) VALUES (13, 77, 16);
INSERT INTO credits_list (id, credit, programme) VALUES (14, 78, 16);
INSERT INTO credits_list (id, credit, programme) VALUES (15, 79, 16);
INSERT INTO credits_list (id, credit, programme) VALUES (16, 80, 15);
INSERT INTO credits_list (id, credit, programme) VALUES (126, 147, 44);
INSERT INTO credits_list (id, credit, programme) VALUES (18, 82, 15);
INSERT INTO credits_list (id, credit, programme) VALUES (127, 148, 44);
INSERT INTO credits_list (id, credit, programme) VALUES (21, 85, 20);
INSERT INTO credits_list (id, credit, programme) VALUES (22, 86, 20);
INSERT INTO credits_list (id, credit, programme) VALUES (23, 87, 18);
INSERT INTO credits_list (id, credit, programme) VALUES (128, 149, 45);
INSERT INTO credits_list (id, credit, programme) VALUES (25, 89, 21);
INSERT INTO credits_list (id, credit, programme) VALUES (129, 150, 45);
INSERT INTO credits_list (id, credit, programme) VALUES (27, 90, 21);
INSERT INTO credits_list (id, credit, programme) VALUES (130, 151, 22);
INSERT INTO credits_list (id, credit, programme) VALUES (29, 91, 22);
INSERT INTO credits_list (id, credit, programme) VALUES (33, 93, 22);
INSERT INTO credits_list (id, credit, programme) VALUES (35, 94, 22);
INSERT INTO credits_list (id, credit, programme) VALUES (41, 97, 23);
INSERT INTO credits_list (id, credit, programme) VALUES (43, 98, 23);
INSERT INTO credits_list (id, credit, programme) VALUES (45, 99, 23);
INSERT INTO credits_list (id, credit, programme) VALUES (47, 100, 24);
INSERT INTO credits_list (id, credit, programme) VALUES (49, 101, 24);
INSERT INTO credits_list (id, credit, programme) VALUES (51, 102, 24);
INSERT INTO credits_list (id, credit, programme) VALUES (53, 103, 25);
INSERT INTO credits_list (id, credit, programme) VALUES (55, 104, 25);
INSERT INTO credits_list (id, credit, programme) VALUES (57, 105, 25);
INSERT INTO credits_list (id, credit, programme) VALUES (59, 106, 26);
INSERT INTO credits_list (id, credit, programme) VALUES (61, 107, 26);
INSERT INTO credits_list (id, credit, programme) VALUES (63, 108, 27);
INSERT INTO credits_list (id, credit, programme) VALUES (65, 109, 27);
INSERT INTO credits_list (id, credit, programme) VALUES (67, 110, 28);
INSERT INTO credits_list (id, credit, programme) VALUES (69, 111, 28);
INSERT INTO credits_list (id, credit, programme) VALUES (71, 112, 28);
INSERT INTO credits_list (id, credit, programme) VALUES (73, 113, 28);
INSERT INTO credits_list (id, credit, programme) VALUES (75, 114, 28);
INSERT INTO credits_list (id, credit, programme) VALUES (77, 115, 28);
INSERT INTO credits_list (id, credit, programme) VALUES (79, 116, 29);
INSERT INTO credits_list (id, credit, programme) VALUES (81, 117, 30);
INSERT INTO credits_list (id, credit, programme) VALUES (83, 118, 30);
INSERT INTO credits_list (id, credit, programme) VALUES (85, 119, 31);
INSERT INTO credits_list (id, credit, programme) VALUES (87, 120, 18);
INSERT INTO credits_list (id, credit, programme) VALUES (88, 121, 31);
INSERT INTO credits_list (id, credit, programme) VALUES (90, 122, 18);
INSERT INTO credits_list (id, credit, programme) VALUES (91, 123, 32);
INSERT INTO credits_list (id, credit, programme) VALUES (96, 127, 32);

-- Setting the sequence for credits so the next serial starts from '131'
ALTER SEQUENCE credits_list_id_seq RESTART WITH 131;

-- Index on credits_list
CREATE INDEX credits_list_programme_index ON credits_list (programme ASC);

-- Creating the log table
CREATE TABLE log (
    id SERIAL PRIMARY KEY,
    "timestamp" timestamp without time zone DEFAULT now(),
    message TEXT NOT NULL
);



-- Creating functionTypeByName function
CREATE OR REPLACE FUNCTION functionTypeByName(nameToFind varchar)
    RETURNS integer
AS $$
DECLARE
    functionTypeId integer := 42; -- Uknown
    BEGIN
        SELECT FT.id INTO functionTypeId FROM function_types FT WHERE FT.name = nameToFind;
        RETURN functionTypeId;
    END; $$
LANGUAGE plpgsql;



-- Creating categoryByName function
CREATE OR REPLACE FUNCTION categoryByName(nameToFind varchar)
    RETURNS integer
AS $$
DECLARE
    categoryId integer := 1; -- Uknown
    BEGIN
        SELECT CA.id INTO categoryId FROM categories CA WHERE CA.name = nameToFind;
        RETURN categoryId;
    END; $$
LANGUAGE plpgsql;