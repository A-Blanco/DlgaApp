package com.dlgaApp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.Grupo;

@Repository
public interface GrupoRepository extends CrudRepository<Grupo, Long> {

	
	
	@Query("SELECT COUNT(*) FROM Grupo WHERE curso = :curso AND esingles = :ingles AND numerogrupo = :numeroGrupo AND titulacion_id = :titulacion")
	public long numeroGruposIguales(@Param("numeroGrupo") Integer g, @Param("ingles") boolean i, @Param("curso") Integer c, @Param("titulacion") Long t);
	
}
