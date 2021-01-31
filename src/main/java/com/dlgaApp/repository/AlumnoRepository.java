package com.dlgaApp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Alumno;

@Repository
public interface AlumnoRepository extends CrudRepository<Alumno, Long> {


	public long countByEmail(String email);
	
	

	
}