package com.dlgaApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	
}
