package com.dlgaApp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.entity.Grupo;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.entity.Titulacion;
import com.dlgaApp.service.AsignaturaServiceImpl;
import com.dlgaApp.service.GrupoServiceImpl;
import com.dlgaApp.service.TitulacionService;

@Controller
public class TitulacionController {

	@Autowired
	private TitulacionService titulacionService;

	@Autowired
	private AsignaturaServiceImpl asignaturaService;

	@Autowired
	private GrupoServiceImpl grupoServices;

	@GetMapping(value = "/titulacionList")
	public String titulacionList(Model model,@ModelAttribute("alert") final Object alert) {

		List<Titulacion> l = this.titulacionService.findAll();

		model.addAttribute("titulaciones", l);
		model.addAttribute("alert", alert);

		return "titulacion/titulacionesList";

	}

	@GetMapping(value = "/titulacionUpdate")
	public String titulacionUpdateGet(@RequestParam(name = "titulacionId") final long titulacionId, Model model,
			HttpServletRequest request) {

		Titulacion titulacion = this.titulacionService.findById(titulacionId);

		List<Titulacion> titulaciones = this.titulacionService.findAll();


		model.addAttribute("titulacionSeleccionado", titulacion);
		model.addAttribute("titulaciones", titulaciones);
	

		return "titulacion/titulacionesList";

	}

	@PostMapping(value = "/titulacionUpdate")
	public String titulacionUpdatePost(@Valid @ModelAttribute("titulacionSeleccionado") Titulacion titulacion,
			BindingResult result, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {

		validarTitulacion(titulacion, result);

		if (result.hasErrors()) {

			List<Titulacion> titulaciones = this.titulacionService.findAll();

			model.addAttribute("titulacionSeleccionado", titulacion);
			model.addAttribute("titulaciones", titulaciones);
			

			return "titulacion/titulacionesList";

		} else {

			this.titulacionService.save(titulacion);

			
			redirectAttributes.addFlashAttribute("alert", 2);

			return "redirect:/titulacionList";
		}

	}
	
	@GetMapping(value = "/titulacionCreate")
	public String titulacionCreateGet( Model model,
			HttpServletRequest request) {

		Titulacion titulacion = new Titulacion();

		List<Titulacion> titulaciones = this.titulacionService.findAll();


		model.addAttribute("titulacionCreado", titulacion);
		model.addAttribute("titulaciones", titulaciones);
	

		return "titulacion/titulacionesList";

	}
	
	@PostMapping(value = "/titulacionCreate")
	public String titulacionCreatePost(@Valid @ModelAttribute("titulacionCreado") Titulacion titulacion,
			BindingResult result, Model model, HttpServletRequest request
			,RedirectAttributes redirectAttributes) {

		validarTitulacion(titulacion, result);

		if (result.hasErrors()) {

			List<Titulacion> titulaciones = this.titulacionService.findAll();

			model.addAttribute("titulacionCreado", titulacion);
			model.addAttribute("titulaciones", titulaciones);
			

			return "titulacion/titulacionesList";

		} else {

			this.titulacionService.save(titulacion);
			redirectAttributes.addFlashAttribute("alert", 1);

			return "redirect:/titulacionList";
		}

	}
	
	@GetMapping(value = "/titulacionDelete/{titulacionId}")
	public String titulacionDelete(Model model, @PathVariable("titulacionId") final long titulacionId, HttpServletRequest request
			,RedirectAttributes redirectAttributes) {

		this.titulacionService.deleteById(titulacionId);
		redirectAttributes.addFlashAttribute("alert", 3);

		return "redirect:/titulacionList";

	}
	
	@GetMapping(value = "/titulacionDeleteSeguridad/{titulacionId}")
	public String departamentoDeleteModal(@PathVariable("titulacionId") final long titulacionId, Model model,
			HttpServletRequest request) {

		model.addAttribute("idTitulacionSeleccionado", titulacionId);
		List<Titulacion> l = this.titulacionService.findAll();

		model.addAttribute("titulaciones", l);

		return "titulacion/titulacionesList";
		
	}

	
public void validarTitulacion(Titulacion titulacion, BindingResult result) {
		
	

		if(titulacion.getNombre().trim().equals("")) {
			result.rejectValue("nombre", "nombre", "El nombre no puede ser nulo");
			}
		
		
	}
}
