
INSERT INTO roles (name) VALUES ('USER');

INSERT INTO users (username, password, enabled, nombre, apellidos, edad) VALUES ('pepe@gmail.com', '$2a$10$TzjFYK0l572jpy7hkh3NBOlGqaCrP2FJARnG.wfzTXoJjSK5xQwcG', '1','pepe','blanco',20);

insert into users_roles (user_id,role_id) values ('1','1');