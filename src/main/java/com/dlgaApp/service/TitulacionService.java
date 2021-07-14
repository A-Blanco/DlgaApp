package com.dlgaApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Titulacion;
import com.dlgaApp.repository.TitulacionRepository;

@Service
public class TitulacionService {

	@Autowired
	private TitulacionRepository titulacionRepository;
	
	
	
	public List<Titulacion> findAll() {
		return (List<Titulacion>) this.titulacionRepository.findAll();
	}
}
