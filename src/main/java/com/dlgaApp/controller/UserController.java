package com.dlgaApp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.dlgaApp.entity.Roles;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.AlumnoServiceImpl;
import com.dlgaApp.service.UserDetailsServiceImpl;

@Controller
public class UserController {

	@Autowired
	private UserDetailsServiceImpl usuarioService;
	
	@Autowired
	private AlumnoServiceImpl alumnoService;
	

	
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
			return "usuario/formUser";
		}else {
			
			usuarioService.creaUsuario(usuario);;
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
	
	@GetMapping(value = "/listaUsuario")
	public String listarUsuarios(Model model) {
		
		List<Usuario> l = new ArrayList<Usuario>();
		l = usuarioService.findAllUsers();
		
		model.addAttribute("usuarios", l);
		
		return "usuario/listUsuario";
	}
	
	@GetMapping(value = "/eliminarUsuario/{usuarioId}")
	public String eliminaUsuario(@PathVariable("usuarioId") final long usuarioId, Model model) {
		
		Usuario usuario =this.usuarioService.findById(usuarioId);
		
		Long alumnoId = usuario.getAlumno().getId();
		this.alumnoService.eliminaAlumnoById(alumnoId);
		
		return "redirect:/listaUsuario";
	}
	
	@GetMapping(value = "/usuario/{usuarioId}")
	public String detallesUsuario(@PathVariable("usuarioId") final long usuarioId, Model model) {
		
		Usuario usuario =this.usuarioService.findById(usuarioId);
		
		model.addAttribute("usuario", usuario);
		
		return "usuario/detallesUsuario";
	}
	
	@GetMapping(value = "/usuariosNoAceptados")
	public String usuariosNoAceptados(Model model) {
		
		List<Usuario> usuarios = this.usuarioService.findAllUsuarios().stream().filter(x->x.getRol().equals(Roles.ROLE_REGISTRADO))
				.collect(Collectors.toList());
		
		model.addAttribute("usuarios", usuarios);
		
		return "usuario/usuarioListNoAceptados";
	}
	
	@GetMapping(value = "/aceptarUsuario/{usuarioId}")
	public String aceptarUsuario(@PathVariable("usuarioId") final long usuarioId, Model model) {
		
		Usuario usuario =this.usuarioService.findById(usuarioId);
		
		
		usuario.setRol(Roles.ROLE_MIEMBRO);
		this.usuarioService.save(usuario);
		
		return "redirect:/usuariosNoAceptados";
	}
	
	
}
