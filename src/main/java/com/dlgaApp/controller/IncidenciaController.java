package com.dlgaApp.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.entity.Departamento;
import com.dlgaApp.entity.EstadosIncidencia;
import com.dlgaApp.entity.Incidencia;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.AlumnoServiceImpl;
import com.dlgaApp.service.AsignaturaServiceImpl;
import com.dlgaApp.service.IncidenciaServiceImpl;
import com.dlgaApp.service.ProfesorService;
import com.dlgaApp.service.UserDetailsServiceImpl;

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
		
		model.addAttribute("incidencia", incidencia);
		model.addAttribute("opcionesAlumnos", opcionesAlumnos);
		model.addAttribute("opcionesProfesores", opcionesProfesores);
		model.addAttribute("opcionesAsignaturas", opcionesAsignatura);
		
		
		return "incidencia/formIncidencia";
		
	}
	
	@PostMapping(value = "/incidenciaCreate")
	public String createIncidenciaPost(@Valid @ModelAttribute("incidencia") Incidencia incidencia,
			BindingResult result,Model model, HttpServletRequest request) {
		
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
		
		model.addAttribute("incidencia", incidencia);
		model.addAttribute("opcionesAlumnos", opcionesAlumnos);
		model.addAttribute("opcionesProfesores", opcionesProfesores);
		model.addAttribute("opcionesAsignaturas", opcionesAsignatura);
		
		
		return "incidencia/formIncidencia";
		
		}else {
			
			incidencia.setFechaCreacion(new Date());
			
			this.incidenciaService.save(incidencia);
			
			return "redirect:";
			
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
