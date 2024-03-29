package com.dlgaApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.entity.Titulacion;

@Repository
public interface AsignaturaRepository extends CrudRepository<Asignatura	, Long> {
	
	
	@Transactional
	@Modifying
	@Query("delete FROM Asignatura WHERE nombre = null")
	public void limpiarTabla();
	
	public long countByNombre(String nombre);
	
	public long countByTitulacion(Titulacion titulacion);
	
	@Query("SELECT DISTINCT caracter FROM Asignatura")
	public List<String> obtenerCaracters();
	
	@Query("SELECT DISTINCT duracion FROM Asignatura")
	public List<String> obtenerDuraciones();
	
	@Query("SELECT DISTINCT creditos FROM Asignatura")
	public List<String> obtenerCreditos();
	
	@Query("SELECT DISTINCT ano FROM Asignatura")
	public List<String> obtenerAños();
	
	@Query("SELECT DISTINCT id FROM Asignatura")
	public List<Long> obtenerIds();

}
