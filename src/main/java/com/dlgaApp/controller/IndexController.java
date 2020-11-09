package com.dlgaApp.controller;



import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlgaApp.entity.Delegado;
import com.dlgaApp.entity.User;
import com.dlgaApp.service.DelegadoService;
import com.dlgaApp.service.UserDetailsServiceImpl;



@Controller
public class IndexController {
	
	@Autowired
	private DelegadoService delegadoService;
	
	@Autowired
	private UserDetailsServiceImpl service;
	
	
	
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
	public String guardar(@Valid Delegado delegado, BindingResult result) {
		if(result.hasErrors()) {
			return "formDelegado";
		}
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
	
	@RequestMapping(value = "/usuario")
	public String inicioCrear(Model model) {
		
		User u = new User();
		model.addAttribute("usuario",u);
		
		return "formUser";
	}
	
	@RequestMapping(value = "/gUsuario")
	public String guardarUsuario(User usuario) {
		
		service.guardarusuario(usuario);
		
		
		return "redirect:listar";
	}

}
