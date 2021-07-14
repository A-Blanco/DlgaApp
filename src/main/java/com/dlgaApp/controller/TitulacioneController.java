package com.dlgaApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dlgaApp.entity.Titulacion;
import com.dlgaApp.service.TitulacionService;


@Controller
public class TitulacioneController {

	@Autowired
	private TitulacionService titulacionService;
	
	
	@GetMapping(value = "/titulacionList")
	public String titulacionList (Model model) {
		
		List<Titulacion> l = this.titulacionService.findAll();
		
		model.addAttribute("titulaciones", l);
		
		
		return "titulacionesList";
		
	}
	
}
