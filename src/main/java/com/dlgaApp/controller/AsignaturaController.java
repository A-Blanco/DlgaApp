package com.dlgaApp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dlgaApp.service.asignaturaServiceImpl;

@Controller
public class AsignaturaController {
	
	
	@Autowired
	private asignaturaServiceImpl asignaturaService;
	
	
	@GetMapping(value = "/asignaturas")
	public String cargarAsignaturas(Model model) {
		
		try {
			asignaturaService.elancesAsignatura();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "index";
	}

}
