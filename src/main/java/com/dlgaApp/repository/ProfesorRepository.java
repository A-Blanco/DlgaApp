package com.dlgaApp.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.dlgaApp.entity.Departamento;
import com.dlgaApp.entity.Profesor;

public interface ProfesorRepository extends CrudRepository<Profesor, Long> {
	
	@Transactional
	@Modifying
	@Query("delete FROM Profesor WHERE nombre = null")
	public void limpiarTabla();

}