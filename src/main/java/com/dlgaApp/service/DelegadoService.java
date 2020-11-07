package com.dlgaApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Delegado;
import com.dlgaApp.repository.DelegadoRepository;

@Service
public class DelegadoService {
	
	@Autowired
	private DelegadoRepository delegadoRepository;
	
	public List<Delegado> getAllDelegados(){
		return (List<Delegado>) delegadoRepository.findAll();
	}

	public void guardarDelegado(Delegado delegado) {
		delegadoRepository.save(delegado);
	}
}
