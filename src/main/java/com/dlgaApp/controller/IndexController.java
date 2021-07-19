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
	
	
	
	
	@RequestMapping(value = "/login")
	public String login(Model model,HttpServletRequest request) {
		if(request.getUserPrincipal() != null) {
			return "403";
		}
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
	
	


}
