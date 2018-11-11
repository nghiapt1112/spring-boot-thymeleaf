CREATE TABLE hibernate_sequence(
next_val BIGINT(20)
);
INSERT INTO hibernate_sequence (next_val) VALUE (1);

----------------------------------------------
----------------------------------------------
----------------------------------------------
----------------------------------------------

-- Table structure for roles
CREATE TABLE roles (
  role_id int(11) NOT NULL AUTO_INCREMENT COMMENT 'role identify',
  role_name varchar(100) NOT NULL COMMENT 'Roles name',
  role_description varchar(200) DEFAULT NULL COMMENT 'Roles description',
  PRIMARY KEY (role_id)
);

-- Table structure for user
CREATE TABLE user (
  user_id int(11) NOT NULL AUTO_INCREMENT COMMENT 'User Identify',
  email varchar(100) NOT NULL COMMENT 'Value of mail registration. It used to authenticate with system.',
  password varchar(100) NOT NULL COMMENT 'Users password',
  first_name varchar(100) DEFAULT NULL COMMENT 'users first name',
  last_name varchar(100) DEFAULT NULL COMMENT 'users last name',
  is_disabled int(1) NOT NULL DEFAULT 0 COMMENT 'disable flag',
--   created_by varchar(100) DEFAULT NULL COMMENT 'account of the creator' ,
--   created_date date DEFAULT NULL COMMENT 'created date',
--   updated_by varchar(100) DEFAULT NULL,
--   updated_date date DEFAULT NULL COMMENT 'modified date',
  PRIMARY KEY (user_id),
  UNIQUE KEY username_UNIQUE (email)
);

-- Table structure for users_roles
CREATE TABLE users_roles (
  user_id int(11) NOT NULL,
  role_id int(11) NOT NULL,
  PRIMARY KEY (user_id,role_id),
  CONSTRAINT FK_UR_Roles FOREIGN KEY (role_id) REFERENCES roles (role_id),
  CONSTRAINT FK_UR_User FOREIGN KEY (user_id) REFERENCES user (user_id)
);