package com.dlgaApp.controller;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlgaApp.service.AsignaturaServiceImpl;
import com.dlgaApp.service.DepartamentoServiceImpl;
import com.dlgaApp.service.ProfesorService;



@Controller
public class IndexController {
	
	
	
	@Autowired
	private DepartamentoServiceImpl departamentoService;
	
	@Autowired
	private AsignaturaServiceImpl asignaturaService;
	
	@Autowired 
	private ProfesorService profesorService;
	
	@RequestMapping(value = "/populateBd")
	public String actualizaBd(Model model) {
		
		
		this.departamentoService.añadirDepartamentos();
		this.profesorService.añadirProfesores();
		this.asignaturaService.añadirAsignaturas();
		
		
		
		return "recursos/index";
		
	}
	
	@RequestMapping(value = "/login")
	public String login(Model model) {
		return "recursos/login";
	}
	
	
	
	
	@RequestMapping("")
	public String welcome (Model model, HttpServletRequest request) {
		
		request.getSession().removeAttribute("op");
		request.getSession().removeAttribute("grupoId");
		
		
		
		return "recursos/index";
	}
	

	@RequestMapping("/denegado")
	public String denegado (Model model, HttpServletRequest request) {
		
		return "recursos/denegado";
	}
	
	
	
	
//	
//	@RequestMapping(value="/listar")
//	public String listar(Model model) {
//		
//		List<com.dlgaApp.entity.Usuario> l = new ArrayList<>();
//		l = service.findAllUsers();
//		model.addAttribute("lista",l);
//		
//		return "lista";
//	}
//	
//	@RequestMapping(value = "/crear")
//	public String inicioCrear(Model model) {
//		
//		Usuario u = new Usuario();
//		
//		
//		Alumno al = service.getAlumnoById((long) 1);
//		u.setAlumno(al);
//		
//		model.addAttribute("usuario",u);
//		
//		
//		
//		return "formUser";
//	}
//	
//	@RequestMapping(value = "/crearAl")
//	public String inicioCrearAL(Model model) {
//		
//		
//		Alumno a = new Alumno();
//		
//		
//		
//		model.addAttribute("alumno",a);
//		
//		
//		
//		return "formAlumno";
//	}
//	
//	@RequestMapping(value = "/gUsuario")
//	public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario us,BindingResult result,Model model) {
//		
//		if(result.hasErrors()) {
//			
//			model.addAttribute("usuario", us);
//			return "formUser";
//		}
//		service.guardarusuario(us);
//		
//		
//		return "redirect:listar";
//	}
	
//	@RequestMapping(value = "/gAlumno")
//	public String guardarAlumno(@Valid @ModelAttribute("alumno") Alumno a,BindingResult result,Model model) {
//		
//		if(result.hasErrors()) {
//			
//			model.addAttribute("alumno", a);
//			return "formUser";
//		}
//		service.guardarAl(a);
//		
//		
//		return "redirect:listar";
//	}

}
