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
	
//	@InitBinder
//	public void initPetBinder(final WebDataBinder dataBinder) {
//		dataBinder.setValidator(new UsuarioValidator());
//	}
//	@Autowired
//	private UsuarioValidator validator;
	
	
	//HAY QUE HACER VALIDATORS AL EMAIL DEL ALUMNO Y TELEFONO DE USUARIOS
	@GetMapping(value = "/crearUsuario")
	public String crearUsuarioForm(Model model, HttpServletRequest request) {
	
		
		System.out.println();
		Usuario usuario = new Usuario();
		usuario.setRol(Roles.ROLE_REGISTRADO);
		String tipo = "usuario";
		
		request.getSession().setAttribute("usuario", usuario); 
		request.getSession().setAttribute("tipo", tipo); 
		
		return "redirect:/crearAlumno";
	}
	
	@PostMapping(value = "/crearUsuario")
	public String crearusuario(@Valid @ModelAttribute("usuario") Usuario usuario,BindingResult result,Model model){
		
		validarUsuario(usuario, result);
		if(result.hasErrors()) {		
			model.addAttribute("usuario", usuario);
			return "formUser";
		}else {
			
			usuarioService.saveUsuario(usuario);;
		}
		
		return "redirect:";
		
	}
	
	public void validarUsuario(Usuario usuario,BindingResult result) {
		
		//validar que el username es único
		if (usuarioService.numeroUsuariosByUsername(usuario.getUsername()) != 0) {
			result.rejectValue("username", "username", "El usuario introducido ya existe");
		}
		
		//validar que el numero de telefono es único
		if(usuarioService.numeroUsuariosByTelefono(usuario.getTelefono()) != 0) {
			result.rejectValue("telefono", "telefono", "El telefono introducido ya está registrado");
		}
		
	}
	
	
	
	
}
