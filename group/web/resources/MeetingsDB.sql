/* 
 * Created by Alex Martin on 2017.03.22  * 
 * Copyright © 2017 Alex Martin. All rights reserved. * 
 */

SET FOREIGN_KEY_CHECKS=0; 
DROP TABLE IF EXISTS User; 
DROP TABLE IF EXISTS Meeting; 
DROP TABLE IF EXISTS Meeting_Users;
DROP TABLE IF EXISTS MeetingFile;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR (32) NOT NULL,
    password VARCHAR (32) NOT NULL,
    first_name VARCHAR (32) NOT NULL,
    middle_name VARCHAR (32),
    last_name VARCHAR (32) NOT NULL,
    address1 VARCHAR (128) NOT NULL,
    address2 VARCHAR (128),
    city VARCHAR (64) NOT NULL,
    state VARCHAR (2) NOT NULL,
    zipcode VARCHAR (10) NOT NULL, /* e.g., 24060-1804 */
    security_question INT NOT NULL, /* Refers to the number of the selected security question */
    security_answer VARCHAR (128) NOT NULL,
    email VARCHAR (128) NOT NULL, 
    userPhoto VARCHAR (255) NOT NULL,   
    PRIMARY KEY (id)
);

CREATE TABLE Meeting
(
        id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
        address1 VARCHAR (128) NOT NULL,
        address2 VARCHAR (128),
        city VARCHAR(64) NOT NULL,
        state VARCHAR(2) NOT NULL,
        zipcode SMALLINT(5) NOT NULL,
        owner_id INT UNSIGNED,
        FOREIGN KEY (owner_id) REFERENCES User(id) ON DELETE CASCADE,
        topic VARCHAR(64),
        description VARCHAR(256),
        timeslots VARCHAR (256),
        invitees VARCHAR (256),
        finaltime VARCHAR (256)
);

CREATE TABLE Meeting_Users
(
        user_id INT UNSIGNED NOT NULL,
        meeting_id INT UNSIGNED NOT NULL,
        PRIMARY KEY (user_id, meeting_id),
        response BOOLEAN,
        available_times VARCHAR (4000),
        FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
        FOREIGN KEY (meeting_id) REFERENCES Meeting(id) ON DELETE CASCADE,
        UNIQUE(user_id, meeting_id)
);

CREATE TABLE MeetingFile
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       filename VARCHAR (256) NOT NULL,
       meeting_id INT UNSIGNED NOT NULL,
       FOREIGN KEY (meeting_id) REFERENCES Meeting(id) ON DELETE CASCADE
);


INSERT INTO User (username, password, first_name, middle_name, last_name, address1, city, state, zipcode, security_question, security_answer, email, userPhoto) VALUES 
('alexmartin', 'password1', 'Alex', 'James', 'Martin', '220 Edge Way', 'Blacksburg', 'VA', '24060', '1', 'answer', 'alexm118@vt.edu', 'spongebob.jpg'),
('johndoe', 'password1', 'John', 'Jacob', 'Doe', '100 Main Street', 'Blacksburg', 'VA', '24060', '1', 'answer', 'johndoe@jd.com', 'Gandalf.jpg'),
('p', 'p', 'Patrick', 'Eugene', 'Gatewood', '100 Main Street', 'Blacksburg', 'VA', '24060', '1', 'answer', 'johndoe@jd.com', 'penguin.png');

INSERT INTO Meeting (address1, city, state, zipcode, owner_id, topic, description, timeslots, invitees, finaltime) VALUES
('800 West Campus Drive', 'Blacksburg', 'VA', '24060', '1', 'Test Meetings', '1: This is a preloaded meeting to test functionality', 'Thu Jan 10 02:00:00 EET 1992,Thu Jan 10 02:30:00 EET 1992,Thu Jan 10 3:00:00 EET 1992,Fri Jan 11 02:00:00 EET 1992,Sat Jan 12 02:00:00 EET 1992', '', ''),
('800 West Campus Drive', 'Blacksburg', 'VA', '24060', '1', 'Test Meetings', '2: This is a preloaded meeting to test functionality', 'Fri Jan 11 02:00:00 EET 1992,Sat Jan 12 02:00:00 EET 1992', '', ''),
('800 West Campus Drive', 'Blacksburg', 'VA', '24060', '1', 'Test Meetings', '3: This is a preloaded meeting to test functionality', 'Thu Jan 10 02:00:00 EET 1992,Thu Jan 10 02:30:00 EET 1992,Thu Jan 10 3:00:00 EET 1992,Fri Jan 11 02:00:00 EET 1992,Sat Jan 12 02:00:00 EET 1992,Sat Jan 12 02:45:00 EET 1992,Sat Jan 12 05:00:00 EET 1992', '', ''),
('800 West Campus Drive', 'Blacksburg', 'VA', '24060', '1', 'Test Meetings', '4: This is a preloaded meeting to test functionality', 'Fri Jan 11 02:00:00 EET 1992,Sat Jan 12 02:00:00 EET 1992', '', ''),
('900 West Campus Drive', 'Blacksburg', 'VA', '24060', '1', 'Test Meeting!', '5: This is a meeting to test functionality', 'Fri Jan 10 02:00:00 EET 1992,Sat Jan 12 02:00:00 EET 1992', '', '');

INSERT INTO Meeting_Users (user_id, meeting_id, response, available_times) VALUES 
('1', '1', TRUE, 'Thu Jan 10 02:00:00 EET 1992,Thu Jan 10 02:30:00 EET 1992'),
('2', '1', FALSE, ''),
('3', '1', TRUE, 'Fri Jan 11 02:00:00 EET 1992,Sat Jan 12 02:00:00 EET 1992'),
('3', '2', TRUE, ''),
('3', '3', FALSE, ''),
('3', '4', FALSE, ''),
('3', '5', TRUE, ''),
('1', '5', TRUE, ''),
('2', '5', TRUE, '');
