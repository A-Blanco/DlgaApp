package com.dlgaApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.dlgaApp.entity.Departamento;

@Repository
public interface DepartamentoRepository extends CrudRepository<Departamento, Long>{

	public Departamento findByNombre(String nombre);
}
