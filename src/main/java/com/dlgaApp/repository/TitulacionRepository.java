package com.dlgaApp.repository;

import org.springframework.data.repository.CrudRepository;

import com.dlgaApp.entity.Titulacion;

public interface TitulacionRepository extends CrudRepository<Titulacion, Long> {

	public boolean existsByNombre(String nombre);
	
	
	public Titulacion findByNombre(String nombre);
}
