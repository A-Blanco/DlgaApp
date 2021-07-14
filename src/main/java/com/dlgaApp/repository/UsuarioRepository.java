package com.dlgaApp.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {


	public void save(org.springframework.security.core.userdetails.User u);

	public Usuario findByUsername(String username);
	
	@Query("SELECT COUNT(*) FROM Usuario WHERE USERNAME= :username")
	public long numeroUsuariosByUsername(@Param("username") String username);
	
	public long countByTelefono(String telefono);
	
	


	


	
}
