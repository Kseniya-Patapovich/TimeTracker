CREATE TABLE IF NOT EXISTS time_tracker_user
(
    id bigserial PRIMARY KEY,
    login varchar(100) NOT NULL UNIQUE,
    password varchar(60) NOT NULL,
    full_name varchar(100) NOT NULL,
    locked BOOLEAN DEFAULT false NOT NULL,
    user_role varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS project
(
  id bigserial PRIMARY KEY,
  project_name varchar(200) NOT NULL,
  status varchar(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_to_project
(
  user_id INT NOT NULL,
  project_id INT NOT NULL,
  PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES time_tracker_user (id),
    FOREIGN KEY (project_id) REFERENCES project (id)
);

CREATE TABLE IF NOT EXISTS record
(
  id bigserial PRIMARY KEY,
  record_date date NOT NULL,
  spent INT NOT NULL,
  user_id INT NOT NULL,
  project_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES time_tracker_user (id),
    FOREIGN KEY (project_id) REFERENCES project (id)
);

INSERT INTO time_tracker_user (full_name, login, password, user_role, locked)
VALUES ('superadmin', 'superadmin','$2a$12$S/TqvMn7YbxV/zo4Zda1ReC1xQ9wQ6tcYckq3GhRc3bpkn3bEI7ka', 'SUPER_ADMIN', false);