package com.dlgaApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dlgaApp.entity.Departamento;

@Repository
public interface DepartamentoRepository extends CrudRepository<Departamento, Long>{

	public Departamento findByNombre(String nombre);
	
	@Transactional
	@Modifying
	@Query("delete FROM Departamento WHERE nombre = null")
	public void limpiarTabla();
	
	
	@Query("SELECT DISTINCT id FROM Departamento")
	public List<Long> obtenerIds();
	
	@Query("SELECT DISTINCT sede FROM Departamento")
	public List<String> obtenerSedes();
}
