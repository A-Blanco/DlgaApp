package com.dlgaApp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.dlgaApp.entity.Roles;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.UserDetailsServiceImpl;

@Controller
public class UserController {

	@Autowired
	private UserDetailsServiceImpl usuarioService;
	
	
	
	@GetMapping(value = "/crearUsuario")
	public String crearUsuarioForm(Model model, HttpServletRequest request) {
	
		Usuario usuario = new Usuario();
		usuario.setRol(Roles.ROLE_REGISTRADO);
		String tipo = "usuario";
		
		request.getSession().setAttribute("usuario", usuario); 
		request.getSession().setAttribute("tipo", tipo); 
		
		return "redirect:/crearAlumno";
	}
	
	@PostMapping(value = "/crearUsuario")
	public String crearusuario(@Valid @ModelAttribute("usuario") Usuario usuario,BindingResult result,Model model){
		
		if(result.hasErrors()) {		
			model.addAttribute("usuario", usuario);
			return "formUser";
		}else {
			
			usuarioService.saveUsuario(usuario);;
		}
		
		return "redirect:";
		
	}
	
	
	
	
}
