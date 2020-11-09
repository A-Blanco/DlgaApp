insert into delegado (nombre,apellidos,edad)values('Alejandro', 'Blanco Perez',20);
insert into delegado (nombre,apellidos,edad)values('jose', 'Blanco Perez',20);


INSERT INTO roles (name) VALUES ('USER');

INSERT INTO users (username, password, enabled) VALUES ('pepe', '$2a$10$2ENiXkrocYQUSlCob1gk4OQ97rcqnVA5zo7ginZz9FAcinXyrilH6', '1');

insert into users_roles (user_id,role_id) values ('1','1');