CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(500) NOT NULL
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(10)
);

CREATE TABLE authorities (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE tokens (
    user_id BIGINT PRIMARY KEY,
    token VARCHAR(512) NOT NULL,
    CONSTRAINT tokens_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(1000) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    priority VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    author BIGINT NOT NULL,
    CONSTRAINT tasks_author_fkey FOREIGN KEY (author) REFERENCES users(id)
);

CREATE TABLE performers (
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT performers_task_id_fkey FOREIGN KEY (task_id) REFERENCES tasks(id),
    CONSTRAINT performers_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(500) NOT NULL,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT comments_task_id_fkey FOREIGN KEY (task_id) REFERENCES tasks(id),
    CONSTRAINT comments_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO roles(name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

-- Password for all these users: users_password
INSERT INTO users(name, email, password) VALUES ('user1', 'user1@mail.ru', '$2y$10$bdhdBLiGPW.dQXWarc14V.Aal1TYQq7VDiAXNbf1KZfYkg1uWWSFG'), 
('user2', 'user2@mail.ru', '$2y$10$bdhdBLiGPW.dQXWarc14V.Aal1TYQq7VDiAXNbf1KZfYkg1uWWSFG'),
('admin', 'admin@mail.ru', '$2y$10$bdhdBLiGPW.dQXWarc14V.Aal1TYQq7VDiAXNbf1KZfYkg1uWWSFG'); 

INSERT INTO authorities(user_id, role_id) VALUES (1, 1), (2, 1), (3, 1), (3, 2);

INSERT INTO tasks (title, description, priority, status, author) VALUES
('Task 1', 'Description of task 1', 'HIGH', 'PENDING', 3),
('Task 2', 'Description of task 2', 'MEDIUM', 'IN_PROGRESS', 3),
('Task 3', 'Description of task 3', 'LOW', 'COMPLETED', 3),
('Task 4', 'Description of task 4', 'HIGH', 'IN_PROGRESS', 3),
('Task 5', 'Description of task 5', 'MEDIUM', 'PENDING', 3);

INSERT INTO performers (task_id, user_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 1),
(5, 2);

INSERT INTO comments (content, task_id, user_id) VALUES ('Ill do this', 1, 1), ('This is fine', 2, 2), ('No comments, bro', 2, 3);
