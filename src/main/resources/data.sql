
INSERT INTO titulaciones (nombre) VALUES ('software');
INSERT INTO titulaciones (nombre) VALUES ('salud');

INSERT INTO departamentos (nombre, sede, email, telefono, web) VALUES ('lsi', 'casa', 'jajscda@email.com', '623478876', 'www.holka.es');
INSERT INTO departamentos (nombre, sede, email, telefono, web) VALUES ('lsi2', 'casa', 'jaja@email.com', '654789876', 'www.hola.es');

INSERT INTO grupos (numerogrupo, curso, esingles,titulacion_id) VALUES (1,'4º',1,1);
INSERT INTO grupos (numerogrupo, curso, esingles,titulacion_id) VALUES (2,'4º',1,1);

INSERT INTO alumnos (nombre, apellidos, email, edad, grupo_Es_Delegado) VALUES ('pepe','blanco','pepe@gmail.com',23, 1);
INSERT INTO alumnos (nombre, apellidos, email, edad, grupo_Es_Delegado) VALUES ('pepa','blanco','pepa@gmail.com',23, 1);
INSERT INTO alumnos (nombre, apellidos, email, edad, grupo_Es_Delegado) VALUES ('a','blanco','pep0@gmail.com',23, 2);
INSERT INTO alumnos (nombre, apellidos, email, edad, grupo_Es_Delegado) VALUES ('b','blanco','pepi@gmail.com',23, null);

INSERT INTO profesores (nombre, apellidos, email, telefono, departamento_id) VALUES ('pedro', 'pere', 'oldva@gmail.com', '432594777',1);
INSERT INTO profesores (nombre, apellidos, email, telefono, departamento_id) VALUES ('pedro', 'pere', 'olEGQWa@gmail.com', '432504777',1);
INSERT INTO profesores (nombre, apellidos, email, telefono, departamento_id) VALUES ('pedro', 'pere', 'oFFa@gmail.com', '432565777',2);

INSERT INTO asignaturas (nombre, caracter, duracion, creditos, titulacion_id, departamento_id) VALUES ('di','opcional','cuatrimestral',6,1,1);
INSERT INTO asignaturas (nombre, caracter, duracion, creditos, titulacion_id, departamento_id) VALUES ('pgpi','opcional','cuatrimestral',6,1,1);
INSERT INTO asignaturas (nombre, caracter, duracion, creditos, titulacion_id, departamento_id) VALUES ('aii','opcional','cuatrimestral',6,2,2);

INSERT INTO usuario (username, password, telefono, rol,alumno_id) VALUES ('pepe@gmail.com', '$2a$10$TzjFYK0l572jpy7hkh3NBOlGqaCrP2FJARnG.wfzTXoJjSK5xQwcG','666355444','ROLE_MIEMBRO',1);
INSERT INTO usuario (username, password, telefono, rol,alumno_id) VALUES ('pepa@gmail.com', '$2a$10$TzjFYK0l572jpy7hkh3NBOlGqaCrP2FJARnG.wfzTXoJjSK5xQwcG','666555443','ROLE_MIEMBRO',2);


