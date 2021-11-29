package com.dlgaApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Incidencia;

@Repository
public interface IncidenciaRepository extends CrudRepository<Incidencia, Long> {

}
