package com.dlgaApp.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {


	public void save(org.springframework.security.core.userdetails.User u);

	public Usuario findByUsername(String username);


	
	
//  @Query("SELECT u FROM Usuarios u WHERE u.username = :username")
//  public Usuario getUserByUsername(@Param("username") String username);	
	
//@Modifying
//@Query(value="INSERT INTO users_roles (user_id,role_id) values (:username_id,1)", nativeQuery = true)
//public void añadirRolUsuario(@Param("username_id") Long username_id);


	
}
