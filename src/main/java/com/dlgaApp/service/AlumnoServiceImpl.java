package com.dlgaApp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.repository.AlumnoRepository;

@Service
public class AlumnoServiceImpl {

	@Autowired
	private AlumnoRepository alumnoRepository;
	
	
	@Transactional
	public void saveAlumno(Alumno alumno) {
		
		alumnoRepository.save(alumno);
	}
	
	public Alumno getAlumnoById(Long id) {
		return alumnoRepository.findById(id).get();
		
	}
	
	public Long numeroAlumnosByEmail(String email) {
		return alumnoRepository.countByEmail(email);
	}
	
	
	
}
