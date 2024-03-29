package com.dlgaApp.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.entity.Departamento;
import com.dlgaApp.entity.EstadosIncidencia;
import com.dlgaApp.entity.Incidencia;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.entity.Roles;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.AlumnoServiceImpl;
import com.dlgaApp.service.AsignaturaServiceImpl;
import com.dlgaApp.service.IncidenciaServiceImpl;
import com.dlgaApp.service.MailService;
import com.dlgaApp.service.ProfesorService;
import com.dlgaApp.service.UserDetailsServiceImpl;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

@Controller
public class IncidenciaController {
	
	@Autowired
	private IncidenciaServiceImpl incidenciaService;
	
	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private ProfesorService profesorService;
	
	@Autowired
	private UserDetailsServiceImpl usuarioService;
	
	@Autowired 
	private AsignaturaServiceImpl asignaturaService;
	
	@Autowired
	private MailService mailService;
	
	
	@GetMapping(value = "/incidenciaCreate")
	public String createIncidenciaGet(Model model, HttpServletRequest request) {
		
		Incidencia incidencia = new Incidencia();
		incidencia.setEstado(EstadosIncidencia.BusquedaInformacion);
		incidencia.setMiembro(this.usuarioActual());
		
		List<Asignatura> asignaturas = this.asignaturaService.findAll();
		List<Alumno> alumnos = this.alumnoService.findAll();
		List<Profesor> profesores = this.profesorService.profesorList();
		
		Map<String, String> opcionesAsignatura = new HashMap<String, String>();

		for (Asignatura a : asignaturas) {
			opcionesAsignatura.put(a.getId().toString(), a.getNombre() + "(" + a.getTitulacion().getNombre() + ")");
		}
		
		Map<String, String> opcionesAlumnos = new HashMap<String, String>();

		for (Alumno alum : alumnos) {
			opcionesAlumnos.put(alum.getId().toString(), alum.getNombre() + " " + alum.getApellidos());
		}
		
		Map<String, String> opcionesProfesores = new HashMap<String, String>();

		for (Profesor prof : profesores) {
			opcionesProfesores.put(prof.getId().toString(), prof.getNombre() );
		}
		
		model.addAttribute("incidenciaCreate", incidencia);
		model.addAttribute("opcionesAlumnos", opcionesAlumnos);
		model.addAttribute("opcionesProfesores", opcionesProfesores);
		model.addAttribute("opcionesAsignaturas", opcionesAsignatura);
		
		
		return "recursos/index";
		
	}
	
	@PostMapping(value = "/incidenciaCreate")
	public String createIncidenciaPost(@Valid @ModelAttribute("incidenciaCreate") Incidencia incidencia,
			BindingResult result,Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		validateIncidencia(incidencia, result,0);
		
		if(result.hasErrors()) {
		
		List<Asignatura> asignaturas = this.asignaturaService.findAll();
		List<Alumno> alumnos = this.alumnoService.findAll();
		List<Profesor> profesores = this.profesorService.profesorList();
		
		Map<String, String> opcionesAsignatura = new HashMap<String, String>();

		for (Asignatura a : asignaturas) {
			opcionesAsignatura.put(a.getId().toString(), a.getNombre() + "(" + a.getTitulacion().getNombre() + ")");
		}
		
		Map<String, String> opcionesAlumnos = new HashMap<String, String>();

		for (Alumno alum : alumnos) {
			opcionesAlumnos.put(alum.getId().toString(), alum.getNombre() + " " + alum.getApellidos());
		}
		
		Map<String, String> opcionesProfesores = new HashMap<String, String>();

		for (Profesor prof : profesores) {
			opcionesProfesores.put(prof.getId().toString(), prof.getNombre() );
		}
		
		model.addAttribute("incidenciaCreate", incidencia);
		model.addAttribute("opcionesAlumnos", opcionesAlumnos);
		model.addAttribute("opcionesProfesores", opcionesProfesores);
		model.addAttribute("opcionesAsignaturas", opcionesAsignatura);
		
		
		return "recursos/index";
		
		}else {
			
			incidencia.setFechaCreacion(new Date());
			
			this.incidenciaService.save(incidencia);
			redirectAttributes.addFlashAttribute("alert", 1);
			
			return "redirect:";
			
		}
		
	}
	
	@GetMapping(value = "/incidenciaList")
	public String incidenciaList(Model model,@ModelAttribute("alert") final Object alert) {
		
		List<Incidencia> incidencias = this.incidenciaService.finfAll();
		
		Usuario usuarioActual = usuarioActual();
		incidencias.removeIf(x->x.getMiembro().getId()==usuarioActual.getId());
		
		model.addAttribute("incidencias", incidencias);
		model.addAttribute("modo", "ajeno");
		model.addAttribute("alert", alert);
		return "incidencia/incidenciaList";
		
	}
	
	@GetMapping(value = "/incidenciaPersonalList")
	public String incidenciaPersonalList(Model model,@ModelAttribute("alert") final Object alert) {
		
		List<Incidencia> incidencias = this.incidenciaService.finfAll();
		
		Usuario usuarioActual = usuarioActual();
		incidencias.removeIf(x->x.getMiembro().getId()!=usuarioActual.getId());
		
		model.addAttribute("incidencias", incidencias);
		model.addAttribute("modo", "personal");
		model.addAttribute("alert", alert);

		return "incidencia/incidenciaList";
		
	}
	
	
	@GetMapping(value = "/gestionIncidencias")
	public String gestionIncidencias(Model model,@ModelAttribute("alert") final Object alert) {
		
		List<Incidencia> incidencias = this.incidenciaService.finfAll();
		
		model.addAttribute("incidencias", incidencias);
		model.addAttribute("modo", "gestion");
		model.addAttribute("alert", alert);

		return "incidencia/incidenciaList";
		
	}
	
	@GetMapping(value = "/detallesIncidencia/{incidenciaId}")
	public String detallesIncidencia(@PathVariable("incidenciaId") final long incidenciaId, Model model,
			@ModelAttribute("alert") final Object alert) {
		
		Incidencia incidencia = this.incidenciaService.findIncidenciaById(incidenciaId);
		
		model.addAttribute("incidenciaDetalles", incidencia);
		model.addAttribute("alert", alert);
		return "incidencia/incidenciaList";
	}
	
	@GetMapping(value = "/incidenciaUpdateInfo/{incidenciaId}")
	public String incidenciaUpdateInfoGet(@PathVariable("incidenciaId") final long incidenciaId, Model model) {
		
		Incidencia incidencia = this.incidenciaService.findIncidenciaById(incidenciaId);
		
		model.addAttribute("incidenciaUpdateInfo", incidencia);
		return "incidencia/incidenciaList";
	}
	
	@PostMapping(value = "/incidenciaUpdateInfo")
	public String incidenciaUpdateInfo(@Valid @ModelAttribute("incidenciaUpdateInfo") Incidencia incidencia, BindingResult result,
			Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) throws JSONException, MailjetException, MailjetSocketTimeoutException, IOException {

		this.validateIncidencia(incidencia, result,1);

		if (result.hasErrors()) {
			model.addAttribute("incidenciaUpdateInfo", incidencia);
			if(result.hasFieldErrors("informacionContrastada")) {
				model.addAttribute("errorInfo", "errorInfo");
			}
			
			if(result.hasFieldErrors("acuerdo")) {
				model.addAttribute("errorAcuerdo", "errorAcuerdo");
			}
			

			return "incidencia/incidenciaList";
		} else {
			
			if(incidencia.getEstado() == EstadosIncidencia.BusquedaInformacion) {
				incidencia.setEstado(EstadosIncidencia.BusquedaAcuerdo);
			}
			
			this.incidenciaService.save(incidencia);
			
			this.mailService.emailInfoIncidencia(incidencia);
			redirectAttributes.addFlashAttribute("alert", 2);

			return "redirect:/detallesIncidencia/" + incidencia.getId();
		}

	}
	
	@GetMapping(value = "/incidenciaUpdateAcuerdo/{incidenciaId}")
	public String incidenciaUpdateAcuerdoGet(@PathVariable("incidenciaId") final long incidenciaId, Model model) {
		
		Incidencia incidencia = this.incidenciaService.findIncidenciaById(incidenciaId);
		
		model.addAttribute("incidenciaUpdateAcuerdo", incidencia);
		return "incidencia/incidenciaList";
	}
	
	@PostMapping(value = "/incidenciaUpdateAcuerdo")
	public String incidenciaUpdateAcuerdo(@Valid @ModelAttribute("incidenciaUpdateAcuerdo") Incidencia incidencia, BindingResult result,
			Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) throws JSONException, MailjetException, MailjetSocketTimeoutException, IOException {

		this.validateIncidencia(incidencia, result,2);

		if (result.hasErrors()) {
			model.addAttribute("incidenciaUpdateAcuerdo", incidencia);
			if(result.hasFieldErrors("informacionContrastada")) {
				model.addAttribute("errorInfo", "errorInfo");
			}
			
			if(result.hasFieldErrors("acuerdo")) {
				model.addAttribute("errorAcuerdo", "errorAcuerdo");
			}
			

			return "incidencia/incidenciaList";
		} else {
			
			 if(incidencia.getEstado() == EstadosIncidencia.BusquedaAcuerdo) {
				incidencia.setEstado(EstadosIncidencia.Finalizada);
			}
			
			this.incidenciaService.save(incidencia);
			
			this.mailService.emailFinalizacionIncidencia(incidencia);
			redirectAttributes.addFlashAttribute("alert", 21);
			
			return "redirect:/detallesIncidencia/" + incidencia.getId();
		}

	}
	
	@GetMapping(value = "/incidenciaDeleteSeguridad/{incidenciaId}")
	public String incidenciaDeleteModal(@PathVariable("incidenciaId") final long incidenciaId, @RequestParam(name = "modo") String modo, Model model,
			HttpServletRequest request) {
		
		
		model.addAttribute("incidenciaIdDelete", incidenciaId);
		List<Incidencia> incidencias = this.incidenciaService.finfAll();
		Usuario usuarioActual = usuarioActual();
		model.addAttribute("modo", modo);
		
		if(modo.equals("ajeno")) {
			incidencias.removeIf(x->x.getMiembro().getId()==usuarioActual.getId());
		}if(modo.equals("personal")) {
			incidencias.removeIf(x->x.getMiembro().getId()!=usuarioActual.getId());
		}if(modo.equals("gestion")) {
			
		}
		
		model.addAttribute("incidencias", incidencias);
		return "incidencia/incidenciaList";
	}
	
	@GetMapping(value = "/incidenciaDelete/{incidenciaId}")
	public String incidenciaDelete(Model model, @PathVariable("incidenciaId") final long incidenciaId,@RequestParam(required = false, name = "modo") String modo,
			HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		Incidencia incidencia = this.incidenciaService.findIncidenciaById(incidenciaId);
		
		Usuario usuarioActual = usuarioActual();
		if((usuarioActual.getRol().equals(Roles.ROLE_ADMIN) || incidencia.getMiembro().getId() == usuarioActual.getId()) && modo != null){

		this.incidenciaService.deleteById(incidenciaId);
		
		
		if(modo.equals("ajeno")) {
			redirectAttributes.addFlashAttribute("alert", 3);
			return "redirect:/incidenciaList";
		}if(modo.equals("personal")) {
			redirectAttributes.addFlashAttribute("alert", 3);
			return "redirect:/incidenciaPersonalList";
		}if(modo.equals("gestion")) {
			redirectAttributes.addFlashAttribute("alert", 3);
			return "redirect:/gestionIncidencias";
		}else {
			
			return "redirect:";
		}
		}else {
			return "redirect:/denegado";
		}
	}
	
	
	private void validateIncidencia(Incidencia incidencia, BindingResult result, Integer update) {
		
		if(!incidencia.getDescripcion().equals("") && incidencia.getDescripcion().trim().equals("")) {
			result.rejectValue("descripcion", "descripcion", "La descripción debe ser válida");
		}
		
		
		
		if(update == 1 && (incidencia.getEstado() == EstadosIncidencia.BusquedaInformacion || incidencia.getEstado() == EstadosIncidencia.BusquedaAcuerdo)
				&& (incidencia.getInformacionContrastada() ==null || incidencia.getInformacionContrastada().trim().equals(""))){
			
			result.rejectValue("informacionContrastada", "informacionContrastadaUpdate", 
					"Debes completar la información Contrastada");
			
		}
				
		
		if(update == 2 && incidencia.getEstado() == EstadosIncidencia.BusquedaAcuerdo
				&& (incidencia.getAcuerdo() == null || incidencia.getAcuerdo().trim().equals(""))){
			
					result.rejectValue("acuerdo", "acuerdoUpdate", 
							"Debes completar la información sobre el Acuerdo");
			
		}
		
		
	}

	
	private Usuario usuarioActual() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		Usuario us = null;
		if (principal instanceof UserDetails) {
			userDetails = (UserDetails) principal;
			String userName = userDetails.getUsername();
			us = this.usuarioService.findByUsername(userName);
		} else {
			us = null;
		}

		return us;

	}
	
}
