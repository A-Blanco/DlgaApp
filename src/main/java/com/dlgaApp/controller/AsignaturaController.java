package com.dlgaApp.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.service.AsignaturaServiceImpl;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

@Controller
public class AsignaturaController {
	
	
	@Autowired
	private AsignaturaServiceImpl asignaturaService;
	
	
	@GetMapping(value = "/asignaturas")
	public String cargarAsignaturas(Model model) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		this.asignaturaService.añadirAsignaturas();
		
		return "recursos/index";
	}
	
	@RequestMapping(value = "/borra")
	public String delete(Model model)  {
		
		this.asignaturaService.delete();
		
		return "recursos/index";
	}
	

	@GetMapping(value = "/asignaturaList")
	public String asignaturaList(Model model) {
		List<Asignatura> asignaturas = this.asignaturaService.asignaturaList();
		model.addAttribute("asignaturas", asignaturas);
		return "asignatura/asignaturaList";
	}
	
	
	@GetMapping(value = "/detallesAsignatura/{asignaturaId}")
	public String detallesAsignatura(@PathVariable("asignaturaId") final long asignaturaId, Model model) {
		
		Asignatura asignatura = this.asignaturaService.findById(asignaturaId);
		
		model.addAttribute("asignatura", asignatura);
		return "asignatura/asignaturaDetails";
	}

}
