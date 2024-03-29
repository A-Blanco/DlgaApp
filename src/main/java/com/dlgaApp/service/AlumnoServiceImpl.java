package com.dlgaApp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	
	public void eliminaAlumnoById(Long id) {
		
		alumnoRepository.deleteById(id);
	}
	
	public long numeroAlumnos() {
		return this.alumnoRepository.count();
		
	}
	
	public List<Alumno> findAll(){
		return (List<Alumno>) this.alumnoRepository.findAll();
	}
	
	public Alumno findById(long id) {
		return this.alumnoRepository.findById(id).get();
	}
	
	public Alumno findByEmail(String email) {
		return this.alumnoRepository.findByEmail(email);
	}
	
	public List<String> listaEmailSinPropio(Alumno alumno){
		
		List<Alumno> lista = (List<Alumno>) this.alumnoRepository.findAll();
		lista.remove(alumno);
		
		List<String> l = lista.stream().map(x->x.getEmail()).collect(Collectors.toList());
		
		
		return l;
	}
	
}
