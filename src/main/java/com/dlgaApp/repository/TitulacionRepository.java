package com.dlgaApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Titulacion;

@Repository
public interface TitulacionRepository extends CrudRepository<Titulacion, Long> {

	public boolean existsByNombre(String nombre);
	
	
	public Titulacion findByNombre(String nombre);
}
