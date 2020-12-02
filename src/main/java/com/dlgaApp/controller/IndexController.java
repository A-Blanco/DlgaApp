package com.dlgaApp.controller;



import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.UserDetailsServiceImpl;



@Controller
public class IndexController {
	
	
	
	@Autowired
	private UserDetailsServiceImpl service;
	
	
	
	@RequestMapping("")
	public String welcome (Model model) {
		
		
		return "index";
	}
	
	
	
	
	@RequestMapping(value="/listar")
	public String listar(Model model) {
		
		List<com.dlgaApp.entity.Usuario> l = new ArrayList<>();
		l = service.findAllUsers();
		model.addAttribute("lista",l);
		
		return "lista";
	}
	
	@RequestMapping(value = "/crear")
	public String inicioCrear(Model model) {
		
		Usuario u = new Usuario();
		
		model.addAttribute("usuario",u);
		
		return "formUser";
	}
	
	@RequestMapping(value = "/gUsuario")
	public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario us,BindingResult result,Model model) {
		
		if(result.hasErrors()) {
			
			model.addAttribute("usuario", us);
			return "formUser";
		}
		service.guardarusuario(us);
		
		
		return "redirect:listar";
	}

}
