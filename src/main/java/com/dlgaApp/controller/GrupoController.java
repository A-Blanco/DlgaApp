package com.dlgaApp.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dlgaApp.entity.Grupo;
import com.dlgaApp.entity.Titulacion;
import com.dlgaApp.repository.GrupoRepository;
import com.dlgaApp.service.GrupoServiceImpl;
import com.dlgaApp.service.TitulacionService;

@Controller
public class GrupoController  {

	@Autowired
	private GrupoServiceImpl grupoService;

	@Autowired
	private TitulacionService titulacionService;
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	
	@GetMapping(value = "/crearGrupo")
	public String crearGrupoForm(Model model) {

		Grupo grupo = new Grupo();
		List<Titulacion> t = titulacionService.findAll();
		
		
		model.addAttribute("grupo", grupo);
		model.addAttribute("titulaciones", t);
		return "grupoForm";

	}

	@PostMapping(value = "/crearGrupo")
	public String crearGrupo(@Valid @ModelAttribute("grupo") Grupo grupo, BindingResult result, Model model,
			HttpServletRequest request) {

		validarGrupo(grupo, result);

		if (result.hasErrors()) {
			List<Titulacion> t = titulacionService.findAll();
			
			
			model.addAttribute("grupo", grupo);
			model.addAttribute("titulaciones", t);
			return "grupoForm";
		} else {
			
			
			grupoService.save(grupo);

		}

		return "redirect:";

	}

	public void validarGrupo(Grupo grupo, BindingResult result) {

		
		
		// validar que el grupo es único
		if (this.grupoService.numeroGruposRepetidos(grupo.getCurso(), grupo.getNumerogrupo(), grupo.getEsingles(), grupo.getTitulacion().getId()) 
				!= 0) {
			result.reject("unico", "El grupo introducido ya está registrado en la base de datos");
		}
	}

	
}
