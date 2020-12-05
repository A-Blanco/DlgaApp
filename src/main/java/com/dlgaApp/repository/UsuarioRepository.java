package com.dlgaApp.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

//   @Query("SELECT u FROM Usuarios u WHERE u.username = :username")
//   public Usuario getUserByUsername(@Param("username") String username);

public void save(org.springframework.security.core.userdetails.User u);


//@Modifying
//@Query(value="INSERT INTO users_roles (user_id,role_id) values (:username_id,1)", nativeQuery = true)
//public void añadirRolUsuario(@Param("username_id") Long username_id);

public Usuario findByUsername(String username);
	
}
