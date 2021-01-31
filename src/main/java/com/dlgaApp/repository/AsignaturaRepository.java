package com.dlgaApp.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.dlgaApp.entity.Asignatura;

public interface AsignaturaRepository extends CrudRepository<Asignatura	, Long> {
	
	
	@Transactional
	@Modifying
	@Query("delete FROM Asignatura WHERE nombre = null")
	public void limpiarTabla();

}
