package com.dlgaApp.controller;

import java.io.IOException;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dlgaApp.entity.Departamento;
import com.dlgaApp.service.DepartamentoServiceImpl;

@Controller
public class DepartamentoController {
	
	
	@Autowired
	private DepartamentoServiceImpl departamentoService;
	
	@GetMapping(value = "/departamentos")
	public String departamentos(Model model) throws IOException {

		departamentoService.añadirDepartamentos();
		return "index";

	}
	
	@GetMapping(value = "/listDepartamentos")
	public String listDepartamentos(Model model) {
		
		model.addAttribute("departamentos", departamentoService.listaDepartamento());
		
		return "listDepartamentos";
	}

	@GetMapping(value = "/detallesDepartamento/{departamentoId}")
	public String detallesDepartamento(@PathVariable("departamentoId") final long departamentoId, Model model) {
		
		Departamento departamento = this.departamentoService.getDepartamentoById(departamentoId);
		
		model.addAttribute("departamento", departamento);
		return "detallesDepartamento";
	}
}