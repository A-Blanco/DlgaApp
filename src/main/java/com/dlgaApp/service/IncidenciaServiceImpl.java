package com.dlgaApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Incidencia;
import com.dlgaApp.repository.IncidenciaRepository;

@Service
public class IncidenciaServiceImpl {
	
	
	@Autowired
	private IncidenciaRepository incidenciaRepository;
	
	
	public void save (Incidencia incidencia) {
		this.incidenciaRepository.save(incidencia);
	}

}
