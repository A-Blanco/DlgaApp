package com.dlgaApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Titulacion;

@Repository
public interface TitulacionRepository extends CrudRepository<Titulacion, Long> {

	public boolean existsByNombre(String nombre);
	
	
	public Titulacion findByNombre(String nombre);
	
	@Query("SELECT DISTINCT id FROM Titulacion")
	public List<Long> obtenerIds();
}
