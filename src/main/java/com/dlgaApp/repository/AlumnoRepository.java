package com.dlgaApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Alumno;

@Repository
public interface AlumnoRepository extends CrudRepository<Alumno, Long> {

//   @Query("SELECT u FROM Usuarios u WHERE u.username = :username")
//   public Usuario getUserByUsername(@Param("username") String username);


//@Modifying
//@Query(value="INSERT INTO users_roles (user_id,role_id) values (:username_id,1)", nativeQuery = true)
//public void añadirRolUsuario(@Param("username_id") Long username_id);


	
}