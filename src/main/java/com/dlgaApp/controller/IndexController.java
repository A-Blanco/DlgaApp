package com.dlgaApp.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlgaApp.entity.Delegado;
import com.dlgaApp.service.DelegadoService;



@Controller
public class IndexController {
	
	@Autowired
	private DelegadoService delegadoService;
	
	@RequestMapping("")
	public String welcome (Model model) {
		
		
		return "index";
	}
	
	@RequestMapping("/crear")
	public String crear(Model model) {
		
		Delegado delegado = new Delegado();
		model.addAttribute("delegado", delegado);
		
		return "formDelegado";
	}
	
	@RequestMapping(value="/guardar")
	public String guardar(Delegado delegado, Model model) {
		delegadoService.guardarDelegado(delegado);
		
		return "redirect:listar";
	}
	
	@RequestMapping(value="/listar")
	public String listar(Model model) {
		
		List<Delegado> l = new ArrayList<>();
		l = delegadoService.getAllDelegados();
		model.addAttribute("lista",l);
		
		return "lista";
	}

}
