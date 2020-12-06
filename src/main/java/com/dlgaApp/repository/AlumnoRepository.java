package com.dlgaApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Alumno;

@Repository
public interface AlumnoRepository extends CrudRepository<Alumno, Long> {



	
}