package com.dlgaApp.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
