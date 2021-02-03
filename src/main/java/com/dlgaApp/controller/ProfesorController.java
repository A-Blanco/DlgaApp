package com.dlgaApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dlgaApp.entity.Departamento;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.service.ProfesorService;

@Controller
public class ProfesorController {

	@Autowired
	private ProfesorService profesorService;
	
	@GetMapping(value = "/profesor")
	public String traeProfesores(Model model) {
		
		this.profesorService.añadirProfesores();
		
		return "index";
		
	}
	
	@GetMapping(value = "/profesorList")
	public String profesorList(Model model) {
		List<Profesor> profesores = this.profesorService.profesorList();
		model.addAttribute("profesores", profesores);
		return "listProfesores";
	}
	
	
	@GetMapping(value = "/detallesProfesor/{profesorId}")
	public String detallesDepartamento(@PathVariable("profesorId") final long profesorId, Model model) {
		
		Profesor profesor = this.profesorService.findById(profesorId);
		
		model.addAttribute("profesor", profesor);
		return "profesorDetails";
	}
}
