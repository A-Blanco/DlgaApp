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
	
	public Titulacion findById(long id) {
		return this.titulacionRepository.findById(id).orElse(null);
	}
	
	public List<Long> getIdsTitulaciones(){
		
		return this.titulacionRepository.obtenerIds();
	}
	
	public void save(Titulacion t) {
		this.titulacionRepository.save(t);
	}
	
	public void deleteById(Long id) {
		this.titulacionRepository.deleteById(id);
	}
}
