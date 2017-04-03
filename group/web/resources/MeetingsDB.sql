/* 
 * Created by Alex Martin on 2017.03.22  * 
 * Copyright © 2017 Alex Martin. All rights reserved. * 
 */
/**
 * Author:  alexmartin
 * Created: Mar 22, 2017
 */

DROP TABLE IF EXISTS Meeting, User, Meeting_Users;

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
        description VARCHAR(256)
);

CREATE TABLE Meeting_Users
(
        user_id INT UNSIGNED NOT NULL,
        meeting_id INT UNSIGNED NOT NULL,
        FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
        FOREIGN KEY (meeting_id) REFERENCES Meeting(id) ON DELETE CASCADE,
        UNIQUE(user_id, meeting_id)
);