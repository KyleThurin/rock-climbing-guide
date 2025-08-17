-- database m2_final_project
BEGIN TRANSACTION;

-- *************************************************************************************************
-- Drop all db objects in the proper order
-- *************************************************************************************************
DROP TABLE IF EXISTS user_log;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS faces;
DROP TABLE IF EXISTS areas;


-- *************************************************************************************************
-- Create the tables and constraints
-- *************************************************************************************************

--users (name is pluralized because 'user' is a SQL keyword)
CREATE TABLE users (
	user_id SERIAL PRIMARY KEY,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL
	--CONSTRAINT PK_user PRIMARY KEY (user_id)
);

--areas
create table areas (
	area_id SERIAL PRIMARY KEY,
	area_name VARCHAR(50) NOT NULL,
	latitude DECIMAL(9,6) NOT NULL, -- Changed from: latitude VARCHAR(50) NOT NULL,
    longitude DECIMAL(9,6) NOT NULL, -- Changed from: longitude VARCHAR(50) NOT NULL,
	area_details TEXT --varchar?
	--CONSTRAINT PK_area PRIMARY KEY (area_id)
);


--faces
create table faces (
	face_id SERIAL PRIMARY KEY,
	area_id INT NOT NULL,
	face_name VARCHAR, --NOT NULL,
	face_direction VARCHAR,
    face_notes TEXT, --varchar?
	--CONSTRAINT PK_face PRIMARY KEY (face_id),
    CONSTRAINT FK_face_area FOREIGN KEY(area_id) REFERENCES areas(area_id)  -- DELETE CASCADE?
);


--routes
create table routes (
	route_id SERIAL PRIMARY KEY,
	face_id INT NOT NULL, --UNIQUE?
	route_name VARCHAR, --NOT NULL,
	route_difficulty VARCHAR, --Old: route_difficulty DECIMAL(3,2),
    number_of_pitches INT,
    route_notes TEXT,
	--CONSTRAINT PK_route PRIMARY KEY (route_id),
    CONSTRAINT FK_route_face FOREIGN KEY(face_id) REFERENCES faces(face_id) -- DELETE CASCADE?
);

--user_log
create table user_log (
    user_log_id SERIAL PRIMARY KEY,
	--user_id INT NOT NULL,
	username varchar(50) NOT NULL,
	route_id INT NOT NULL,
	log_details TEXT,
	--CONSTRAINT PK_user_log PRIMARY KEY (user_log_id),
    --CONSTRAINT FK_user_log_user FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT FK_user_log_user FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT FK_user_log_route FOREIGN KEY(route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);
-- *************************************************************************************************
-- Insert some sample starting data
-- *************************************************************************************************

-- Users
-- Password for all users is password
INSERT INTO
    users (username, password_hash, role)
VALUES
    ('test1', 'test1','ROLE_ADMIN'),
    ('test2', 'test2','ROLE_USER'),
    ('test3', 'test3','ROLE_USER'),
    ('test4', 'test4','ROLE_USER'),
    ('user', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_USER'),
    ('admin','$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_ADMIN');

-- Area (areas)
insert into areas (area_name, latitude, longitude, area_details) values ('Austin', 30.2672, -97.7431, 'Cras non velit nec nisi vulputate nonummy.');
insert into areas (area_name, latitude, longitude, area_details) values ('Yosemite National Park', 37.8651, -119.5757, 'Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem.');
insert into areas (area_name, latitude, longitude, area_details) values ('Red River Gorge', 37.8139, -83.6279, 'Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl. Aenean lectus.');
insert into areas (area_name, latitude, longitude, area_details) values ('Test Area', 40.7128, -74.0060, 'Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl. Aenean lectus.');
-- Face (faces)
INSERT INTO faces (area_id, face_name, face_direction, face_notes) values (3, 'face_1-3', 'West', 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis faucibus accumsan odio. Curabitur convallis. Duis consequat dui nec nisi volutpat eleifend.');
insert into faces (area_id, face_name, face_direction, face_notes) values (2, 'face_1-2', 'East', 'Proin risus. Praesent lectus. Vestibulum quam sapien, varius ut, blandit non, interdum in, ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis faucibus accumsan odio. Curabitur convallis.');
insert into faces (area_id, face_name, face_direction, face_notes) values (1, 'face_1-1', 'North', 'Maecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam. Suspendisse potenti. Nullam porttitor lacus at turpis. Donec posuere metus vitae ipsum.');
insert into faces (area_id, face_name, face_direction, face_notes) values (3, 'face_2-3', 'West', 'Duis at velit eu est congue elementum. In hac habitasse platea dictumst. Morbi vestibulum, velit id pretium iaculis, diam erat fermentum justo, nec condimentum neque sapien placerat ante. Nulla justo. Aliquam quis turpis eget elit sodales scelerisque.');
insert into faces (area_id, face_name, face_direction, face_notes) values (1, 'face_2-1', 'East', 'Donec dapibus. Duis at velit eu est congue elementum. In hac habitasse platea dictumst.');
insert into faces (area_id, face_name, face_direction, face_notes) values (2, 'face_2-2', 'West', 'Cras mi pede, malesuada in, imperdiet et, commodo vulputate, justo. In blandit ultrices enim. Lorem ipsum dolor sit amet, consectetuer adipiscing elit.');

-- Route (routes)
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (2, 'rout_1', 5.0, 12, 'Vestibulum ac est lacinia nisi venenatis tristique.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (5, 'rout_1', 5.0, 20, 'Mauris lacinia sapien quis libero. Nullam sit amet turpis elementum ligula vehicula consequat.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (1, 'rout_1', 5.0, 4, 'Pellentesque at nulla. Suspendisse potenti. Cras in purus eu magna vulputate luctus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (6, 'rout_1', 5.0, 11, 'Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Etiam vel augue. Vestibulum rutrum rutrum neque.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (3, 'rout_2', 5.0, 18, 'Aenean lectus. Pellentesque eget nunc.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (4, 'rout_2', 5.0, 9, 'Nam nulla. Integer pede justo, lacinia eget, tincidunt eget, tempus vel, pede.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (2, 'rout_1', 5.0, 4, 'Suspendisse accumsan tortor quis turpis.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (5, 'rout_2', 5.0, 13, 'Integer ac leo. Pellentesque ultrices mattis odio. Donec vitae nisi.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (6, 'rout_1', 5.0, 5, 'Morbi a ipsum. Integer a nibh. In quis justo. Maecenas rhoncus aliquam lacus.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (1, 'rout_2', 5.0, 2, 'Donec odio justo, sollicitudin ut, suscipit a, feugiat et, eros. Vestibulum ac est lacinia nisi venenatis tristique. Fusce congue, diam id ornare imperdiet, sapien urna pretium nisl, ut volutpat sapien arcu sed augue. Aliquam erat volutpat. In congue.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (3, 'rout_1', 5.0, 7, 'Integer non velit.');
insert into routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) values (4, 'rout_1', 5.0, 14, 'In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.');

-- User Log (user_log)
insert into user_log (username, route_id, log_details) values ('user', 7, 'Etiam faucibus cursus urna. Ut tellus. Nulla ut erat id mauris vulputate elementum. Nullam varius. Nulla facilisi.');
insert into user_log (username, route_id, log_details) values ('admin', 9, 'Maecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam. Suspendisse potenti. Nullam porttitor lacus at turpis. Donec posuere metus vitae ipsum. Aliquam non mauris. Morbi non lectus. Aliquam sit amet diam in magna bibendum imperdiet. Nullam orci pede, venenatis non, sodales sed, tincidunt eu, felis.');
insert into user_log (username, route_id, log_details) values ('admin', 3, 'Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus.');
insert into user_log (username, route_id, log_details) values ('user', 4, 'Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus. Suspendisse potenti.');
insert into user_log (username, route_id, log_details) values ('user', 11, 'Phasellus id sapien in sapien iaculis congue. Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl. Aenean lectus. Pellentesque eget nunc. Donec quis orci eget orci vehicula condimentum.');
insert into user_log (username, route_id, log_details) values ('test4', 5, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin risus. Praesent lectus. Vestibulum quam sapien, varius ut, blandit non, interdum in, ante.');
insert into user_log (username, route_id, log_details) values ('test3', 2, 'Maecenas pulvinar lobortis est. Phasellus sit amet erat. Nulla tempus. Vivamus in felis eu sapien cursus vestibulum. Proin eu mi.');
insert into user_log (username, route_id, log_details) values ('test2', 1, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin risus. Praesent lectus. Vestibulum quam sapien, varius ut, blandit non, interdum in, ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis faucibus accumsan odio. Curabitur convallis. Duis consequat dui nec nisi volutpat eleifend. Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus. Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis.');
insert into user_log (username, route_id, log_details) values ('test1', 6, 'In congue. Etiam justo. Etiam pretium iaculis justo. In hac habitasse platea dictumst.');
insert into user_log (username, route_id, log_details) values ('test1', 12, 'Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus. Suspendisse potenti. In eleifend quam a odio.');


COMMIT TRANSACTION; --COMMIT;


