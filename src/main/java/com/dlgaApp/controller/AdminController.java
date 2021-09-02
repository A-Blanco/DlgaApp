package com.dlgaApp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlgaApp.service.AsignaturaServiceImpl;
import com.dlgaApp.service.DepartamentoServiceImpl;
import com.dlgaApp.service.ProfesorService;

@Controller
public class AdminController {

	@Autowired
	private DepartamentoServiceImpl departamentoService;

	@Autowired
	private AsignaturaServiceImpl asignaturaService;

	@Autowired
	private FiltroMantenimiento filtroMantenimiento;
	
	@Autowired
	private ProfesorService profesorService;

	@GetMapping(value = "/administrador")
	public String herraminetasAdmin(Model model) {

		return "admin/herramientasAdmin";
	}

	@RequestMapping(value = "/populateBd")
	public String actualizaBd(Model model) {
		
		
		
		
		this.filtroMantenimiento.activarMantenimiento();

		this.departamentoService.añadirDepartamentos();
//		this.profesorService.añadirProfesores();
//		this.asignaturaService.añadirAsignaturas();
		
		this.filtroMantenimiento.desactivarMantenimiento();
		
		return "redirect:";

	}
	
	@RequestMapping(value = "/mantenimiento")
	public String vistaMantenimiento(Model model,HttpServletRequest request) {
		
		return "recursos/denegado";

	}

}
