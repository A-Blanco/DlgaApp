package com.dlgaApp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.entity.Departamento;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.service.AsignaturaServiceImpl;
import com.dlgaApp.service.DepartamentoServiceImpl;
import com.dlgaApp.service.ProfesorService;

@Controller
public class ProfesorController {

	@Autowired
	private ProfesorService profesorService;
	
	@Autowired
	private DepartamentoServiceImpl departamentoService;
	
	@Autowired
	private AsignaturaServiceImpl asignaturaService;
	
	@GetMapping(value = "/profesor")
	public String traeProfesores(Model model) {
		
		this.profesorService.añadirProfesores();
		
		return "recursos/index";
		
	}
	
	@GetMapping(value = "/profesorList")
	public String profesorList(Model model) {
		List<Profesor> profesores = this.profesorService.profesorList();
		model.addAttribute("profesores", profesores);
		return "profesor/listProfesores";
	}
	
	
	@GetMapping(value = "/detallesProfesor/{profesorId}")
	public String detallesDepartamento(@PathVariable("profesorId") final long profesorId, Model model) {
		
		Profesor profesor = this.profesorService.findById(profesorId);
		
		model.addAttribute("profesor", profesor);
		
		return "profesor/profesorDetails";
	}
	
	@GetMapping(value = "/profesorDelete/{profesorId}")
	public String profesorDelete(Model model, @PathVariable("profesorId") final long profesorId, HttpServletRequest request) {

		this.profesorService.deleteById(profesorId);

		return "redirect:/profesorList";

	}
	
	@GetMapping(value = "/profesorUpdate")
	public String profesorUpdateGet(@RequestParam(name = "profesorId") final long profesorId, Model model,
			HttpServletRequest request) {

		Profesor profesor = this.profesorService.findById(profesorId);
		
		List<Asignatura> asignaturas = this.asignaturaService.findAll();
		
		Map<String,String> opcionesAsignatura = new HashMap<String,String>();
		
		for (Asignatura a: asignaturas) {
			opcionesAsignatura.put(a.getId().toString(),
					a.getNombre() + "(" + a.getTitulacion().getNombre()+")");
			
		}


		model.addAttribute("profesorSeleccionado", profesor);
		model.addAttribute("profesores", this.profesorService.profesorList());
		model.addAttribute("opcionesDepartamento", this.departamentoService.getDepartamentos());
		model.addAttribute("opcionesAsignatura", opcionesAsignatura);

		return "profesor/listProfesores";

	}
	
	@PostMapping(value = "/profesorUpdate")
	public String profesorUpdatePost(@Valid @ModelAttribute("profesorSeleccionado") Profesor profesor,
			BindingResult result, Model model, HttpServletRequest request) {

		validarProfesor(profesor, result);
		
		if(result.hasErrors()) {
			
			List<Asignatura> asignaturas = this.asignaturaService.findAll();
			
			Map<String,String> opcionesAsignatura = new HashMap<String,String>();
			
			for (Asignatura a: asignaturas) {
				opcionesAsignatura.put(a.getId().toString(),
						a.getNombre() + "(" + a.getTitulacion().getNombre()+")");
				
			}


			model.addAttribute("profesorSeleccionado", profesor);
			model.addAttribute("profesores", this.profesorService.profesorList());
			model.addAttribute("opcionesDepartamento", this.departamentoService.getDepartamentos());
			model.addAttribute("opcionesAsignatura", opcionesAsignatura);

			return "profesor/listProfesores";
			
		}else {
			
			this.profesorService.save(profesor);
			
			List<Asignatura> asignaturasBd = this.profesorService.findById(profesor.getId())
					.getAsignaturas();
			
			List<Asignatura> asignaturas = profesor.getAsignaturas();
			
			for(Asignatura a : asignaturasBd) {
				
				if(!asignaturas.contains(a)) {
					
					a.getProfesores().removeIf(x->x.getId()==profesor.getId());
					this.asignaturaService.saveAsignatura(a);
				}
			}
			
			for(Asignatura asig :asignaturas) {
				
				if(!asignaturasBd.contains(asig)) {
				
				asig.getProfesores().add(profesor);
				this.asignaturaService.saveAsignatura(asig);
				}
				
			}
			
			
			
			return "redirect:/profesorList";
		}
		
		

	}
	
	
	@GetMapping(value = "/profesorCreate")
	public String profesorCreateGet( Model model,
			HttpServletRequest request) {

		Profesor profesor = new Profesor();
		
		List<Asignatura> asignaturas = this.asignaturaService.findAll();
		
		Map<String,String> opcionesAsignatura = new HashMap<String,String>();
		
		for (Asignatura a: asignaturas) {
			opcionesAsignatura.put(a.getId().toString(),
					a.getNombre() + "(" + a.getTitulacion().getNombre()+")");
			
		}


		model.addAttribute("profesorCreado", profesor);
		model.addAttribute("profesores", this.profesorService.profesorList());
		model.addAttribute("opcionesDepartamento", this.departamentoService.getDepartamentos());
		model.addAttribute("opcionesAsignatura", opcionesAsignatura);

		return "profesor/listProfesores";

	}
	
	@PostMapping(value = "/profesorCreate")
	public String profesorCreatePost(@Valid @ModelAttribute("profesorCreado") Profesor profesor,
			BindingResult result, Model model, HttpServletRequest request) {

		validarProfesor(profesor, result);
		
		if(result.hasErrors()) {
			
			List<Asignatura> asignaturas = this.asignaturaService.findAll();
			
			Map<String,String> opcionesAsignatura = new HashMap<String,String>();
			
			for (Asignatura a: asignaturas) {
				opcionesAsignatura.put(a.getId().toString(),
						a.getNombre() + "(" + a.getTitulacion().getNombre()+")");
				
			}


			model.addAttribute("profesorCreado", profesor);
			model.addAttribute("profesores", this.profesorService.profesorList());
			model.addAttribute("opcionesDepartamento", this.departamentoService.getDepartamentos());
			model.addAttribute("opcionesAsignatura", opcionesAsignatura);

			return "profesor/listProfesores";
			
		}else {
			
			this.profesorService.save(profesor);
			
			for(Asignatura a: profesor.getAsignaturas()) {
				
				a.getProfesores().add(profesor);
				this.asignaturaService.saveAsignatura(a);
				
			}
			
			return "redirect:/profesorList";
		}

	}
	

	@GetMapping(value = "/profesorDeleteSeguridad/{profesorId}")
	public String profesorDeleteModal(@PathVariable("profesorId") final long profesorId, Model model,
			HttpServletRequest request) {

		model.addAttribute("idProfesorSeleccionado", profesorId);
		
		List<Profesor> profesores = this.profesorService.profesorList();
		model.addAttribute("profesores", profesores);
		
		return "profesor/listProfesores";

	}
	
	
	
public void validarProfesor(Profesor profesor, BindingResult result) {
		
		String regex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
		
		String regexTelefono = "(\\+34|0034|34)?[ -]*(6|7|8)[ -]*([0-9][ -]*){8}";
		
		List<Long> idsDepartamentos = this.departamentoService.getIdsDepartamentos();
		List<Long> idsAsignaturas = this.asignaturaService.getIdsAsignaturas();

		if(profesor.getNombre().trim().equals("")) {
			result.rejectValue("nombre", "nombre", "El nombre no puede ser nulo");
			}
		
		if(!profesor.getTelefono().trim().equals("") && !Pattern.matches(regexTelefono, profesor.getTelefono())) {
			
			result.rejectValue("telefono", "telefono", "El teléfono debe ser válido");
		}
		
		if(profesor.getEmail().trim().equals("") || !Pattern.matches(regex, profesor.getEmail())) {
			result.rejectValue("email", "email", "El emáil debe ser válido");
			}
		
		if(profesor.getDepartamento() == null ||
				!idsDepartamentos.contains(profesor.getDepartamento().getId())) {
			result.rejectValue("departamento.id", "departamento.id", "El valor indicado no es corrector");
		}
		
		for(Asignatura a: profesor.getAsignaturas()) {
			
			if(a== null ||
					!idsAsignaturas.contains(a.getId())) {
				result.rejectValue("asignaturas", "asignaturas", "Se ha introducido alguna asignatura errónea");
			}
		}
		
	}
	
}
