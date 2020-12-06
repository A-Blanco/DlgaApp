package com.dlgaApp.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {


	public void save(org.springframework.security.core.userdetails.User u);

	public Usuario findByUsername(String username);


	


	
}
