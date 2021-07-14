package com.dlgaApp.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dlgaApp.entity.Departamento;
import com.dlgaApp.entity.Profesor;

@Repository
public interface ProfesorRepository extends CrudRepository<Profesor, Long> {
	
	@Transactional
	@Modifying
	@Query("delete FROM Profesor WHERE nombre = null")
	public void limpiarTabla();
	
	public Profesor findByNombre(String nombre);

}
