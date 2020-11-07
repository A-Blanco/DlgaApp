package com.dlgaApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Delegado;

@Repository
public interface DelegadoRepository extends CrudRepository<Delegado, Object> {

}
