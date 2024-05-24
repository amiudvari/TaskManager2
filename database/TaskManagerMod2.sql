DROP TABLE if exists task, users, roles;
CREATE TABLE task (
  task_id serial,
  task_name varchar(80) NOT NULL,          -- Name of the task
  priority varchar(50) NOT NULL,     	   -- Priority of task
  due_date date NOT NULL,       		   -- Due date
  completed boolean default false,              -- Status of the task - complete or not 
  description varchar(500)  NULL	       -- Description of task
);

CREATE TABLE users (
	username varchar(250) PRIMARY KEY,
	password varchar (255) 
);

CREATE TABLE roles(
	username varchar(250),
	role varchar(250) NOT NULL,
	PRIMARY KEY (username, role),
	FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

INSERT INTO task (task_name, priority, due_date, completed)
VALUES ('X-ray Validation', 'Medium', '2024-07-31', false);

INSERT INTO task (task_name, priority, due_date, completed)
VALUES ('Micro Validation',	'High', '2024-05-01', false);

INSERT INTO task (task_name, priority, due_date, completed)
VALUES ('Test Task', 'High', '2024-05-21', false);

INSERT INTO users(username, password)
VALUES ('admin', '$2a$10$9cWTb5cC90z0GAqHFzYBlOax5kYv9U/7cDAxBBOnCrv7BSA0HsTPi');

INSERT INTO roles (username, role) 
VALUES ('admin', 'ADMIN');


INSERT INTO users (username, password)
VALUES ('test', '$2a$10$FvhoMEdUf.FrQ23g53gqmeqX/3QlJGF/P3AouwF0vuAHhTihP4pQS');

INSERT INTO roles (username, role) 
VALUES ('test', 'USER')

