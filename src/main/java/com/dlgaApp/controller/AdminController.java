package com.dlgaApp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlgaApp.service.AsignaturaServiceImpl;
import com.dlgaApp.service.DepartamentoServiceImpl;
import com.dlgaApp.service.MailService;
import com.dlgaApp.service.ProfesorService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

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
	
	@Autowired
	private MailService mailService;

	@GetMapping(value = "/administrador")
	public String herraminetasAdmin(Model model) {

		return "admin/herramientasAdmin";
	}

	@RequestMapping(value = "/populateBd")
	public String actualizaBd(Model model) throws JSONException, MailjetException, MailjetSocketTimeoutException, IOException {
		
		
		
		
		this.filtroMantenimiento.activarMantenimiento();
		this.mailService.inicioMantenimiento();

		this.departamentoService.añadirDepartamentos();
		this.profesorService.añadirProfesores();
		this.asignaturaService.añadirAsignaturas();
		
		this.filtroMantenimiento.desactivarMantenimiento();
		this.mailService.finalizaciónMantenimiento();
		
		return "redirect:";

	}
	
	@RequestMapping(value = "/mantenimiento")
	public String vistaMantenimiento(Model model,HttpServletRequest request) {
		
		return "recursos/denegado";

	}
	
	@GetMapping(value = "/gestionBd")
	public String vistaGestionBd(Model model,HttpServletRequest request) {
		
		return "admin/opcionesGestionBd";

	}
	
	
	
	

}
