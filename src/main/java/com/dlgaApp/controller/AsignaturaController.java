package com.dlgaApp.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dlgaApp.service.AsignaturaServiceImpl;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

@Controller
public class AsignaturaController {
	
	
	@Autowired
	private AsignaturaServiceImpl asignaturaService;
	
	
	@GetMapping(value = "/asignaturas")
	public String cargarAsignaturas(Model model) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		this.asignaturaService.añadirAsignaturas();
		
		return "index";
	}

}
