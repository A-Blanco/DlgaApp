package com.dlgaApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Grupo;
import com.dlgaApp.entity.Titulacion;
import com.dlgaApp.repository.GrupoRepository;

@Service
public class GrupoServiceImpl {

	@Autowired
	private GrupoRepository grupoRepository;


	public void save(Grupo grupo) {
		this.grupoRepository.save(grupo);
	}

	public Grupo findByIdString(String id) {
		return this.grupoRepository.findById(Long.valueOf(id)).get();
	}

	
	
	public Long numeroGruposRepetidos(Integer c, Integer g, Boolean i, Long t) {
		return this.grupoRepository.numeroGruposIguales(g, i, c, t);
		
	}
}
