package com.dlgaApp.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.entity.Titulacion;
import com.dlgaApp.service.AsignaturaServiceImpl;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

@Controller
public class AsignaturaController {
	
	
	@Autowired
	private AsignaturaServiceImpl asignaturaService;
	
	
	@GetMapping(value = "/asignaturas")
	public String cargarAsignaturas(Model model) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		this.asignaturaService.añadirAsignaturas();
		
		return "recursos/index";
	}
	
//	@RequestMapping(value = "/asignaturaDelete")
//	public String delete(Model model)  {
//		
//		this.asignaturaService.delete();
//		
//		return "recursos/index";
//	}
	

	@GetMapping(value = "/asignaturaList")
	public String asignaturaList(Model model) {
		List<Asignatura> asignaturas = this.asignaturaService.asignaturaList();
		model.addAttribute("asignaturas", asignaturas);
		return "asignatura/asignaturaList";
	}
	
	
	@GetMapping(value = "/detallesAsignatura/{asignaturaId}")
	public String detallesAsignatura(@PathVariable("asignaturaId") final long asignaturaId, Model model) {
		
		Asignatura asignatura = this.asignaturaService.findById(asignaturaId);
		
		model.addAttribute("asignatura", asignatura);
		return "asignatura/asignaturaDetails";
	}
	
	@GetMapping(value = "/asignaturaDelete/{asignaturaId}")
	public String alumnoDelete(Model model, @PathVariable("asignaturaId") final long asignaturaId, HttpServletRequest request) {

		this.asignaturaService.deleteAsignaturaByID(asignaturaId);

		return "redirect:/asignaturaList";

	}
	
	@GetMapping(value = "/asignaturaUpdate")
	public String asignaturaUpdateModal(@RequestParam(name = "asignaturaId") final long asignaturaId, Model model,
			HttpServletRequest request) {

		Asignatura asignatura = this.asignaturaService.findById(asignaturaId);

		model.addAttribute("asignaturaSeleccionado", asignatura);
		model.addAttribute("asignaturas", this.asignaturaService.findAll());

		return "asignatura/asignaturaList";

	}
	
	@PostMapping(value = "/asignaturaUpdate")
	public String asignaturaUpdate(@Valid @ModelAttribute("asignaturaSeleccionado") Asignatura asignatura, BindingResult result,
			Model model, HttpServletRequest request) {

		this.validarAsignatura(asignatura, result, true);

		if (result.hasErrors()) {
			
			model.addAttribute("asignaturaSeleccionado", asignatura);
			model.addAttribute("asignaturas", this.asignaturaService.findAll());

			return "asignatura/asignaturaList";
		} else {

			this.asignaturaService.saveAsignatura(asignatura);

			return "redirect:/asignaturaList";
		}

	}
	
	public void validarAsignatura(Asignatura asignatura, BindingResult result, boolean checkUpdate) {

		if(asignatura.getNombre().equals("")) {
			result.rejectValue("nombre", "nombre", "El nombre no puede ser nulo");

		}
		
		// validar que el email es único
		if (checkUpdate) {

			Asignatura asignaturaBd = this.asignaturaService.findById(asignatura.getId());

			
			
			String nombre = asignatura.getNombre();
			Long titulacionId = asignatura.getTitulacion().getId();
			Titulacion titulacion = asignatura.getTitulacion();
			
			String nombreBd = asignaturaBd.getNombre();
			Long titulacionIdBd = asignaturaBd.getTitulacion().getId();
			

			if (!nombre.equals(nombreBd) || !titulacionId.equals(titulacionIdBd) ) {

				if (this.asignaturaService.numeroAsignaturasByNombre(nombre) != 0 &&
						this.asignaturaService.numeroAsignaturasByTitulacion(titulacion) != 0) {
					result.rejectValue("nombre", "nombre", "La asignatura ya está añadida en la titulación seleccionada");
				}

			}
		} else {
			if (this.asignaturaService.numeroAsignaturasByNombre(asignatura.getNombre()) != 0 &&
					this.asignaturaService.numeroAsignaturasByTitulacion(asignatura.getTitulacion()) != 0) {
				result.rejectValue("nombre", "nombre", "La asignatura ya está añadida en la titulación seleccionada");
			}
		}
	}

}
