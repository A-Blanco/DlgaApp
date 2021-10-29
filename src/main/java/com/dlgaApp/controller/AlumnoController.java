package com.dlgaApp.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Grupo;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.AlumnoServiceImpl;
import com.dlgaApp.service.GrupoServiceImpl;
import com.dlgaApp.service.UserDetailsServiceImpl;

@Controller
public class AlumnoController {

	@Autowired
	private AlumnoServiceImpl alumnoService;

	@Autowired
	private GrupoServiceImpl grupoService;

	@Autowired
	private UserDetailsServiceImpl usuarioService;

	@GetMapping(value = "/crearAlumno")
	public String crearAlumnoForm(Model model, HttpServletRequest request) {

		if (request.getSession().getAttribute("noAlumno") != null) {
			model.addAttribute("noAlumno", 1);
		}

		if (request.getSession().getAttribute("siUsuario") != null) {
			model.addAttribute("siUsuario", 1);
		}

		Alumno alumno = new Alumno();

		model.addAttribute("alumno", alumno);
		request.getSession().removeAttribute("noAlumno");
		request.getSession().removeAttribute("siUsuario");
		return "alumno/formAlumno";

	}

	@GetMapping(value = "/listAlumnos")
	public String listAlumnos(Model model) {

		List<Alumno> alumnos = this.alumnoService.findAll();
		Usuario usuarioActual = usuarioActual();
		Alumno alumnoActual = usuarioActual.getAlumno();
		alumnos.removeIf(x->x.getId()==alumnoActual.getId());
		
		model.addAttribute("alumnos", alumnos);

		model.addAttribute("prueba", "prueba");

		return "alumno/alumnoList";

	}

	@PostMapping(value = "/crearAlumno")
	public String crearAlumno(@Valid @ModelAttribute("alumno") Alumno alumno, BindingResult result, Model model,
			HttpServletRequest request) {

		validarAlumno(alumno, result, false);

		if (result.hasErrors()) {

			model.addAttribute("alumno", alumno);
			return "alumno/formAlumno";
		} else {

			alumnoService.saveAlumno(alumno);

			if (request.getSession().getAttribute("tipo") == "usuario") {

				Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
				usuario.setAlumno(alumno);

				model.addAttribute("usuario", usuario);

				request.getSession().removeAttribute("tipo");
				request.getSession().removeAttribute("usuario");
				return "usuario/formUser";
			}

			if (request.getSession().getAttribute("op") == "addDelegado") {

				long idGrupo = (long) request.getSession().getAttribute("grupoId");
				alumno.setGrupoDelegado(this.grupoService.findById(idGrupo));
				alumnoService.saveAlumno(alumno);

				Grupo grupo = this.grupoService.findById((long) request.getSession().getAttribute("grupoId"));

				if (grupo.getDelegados().size() < 4) {

					model.addAttribute("addDelegadoAgain", 1);
				} else {
					model.addAttribute("addDelegadoAgain", 0);
					model.addAttribute("grupo", grupo);
				}
				return "grupo/inicioGrupoAdd";

			}
		}

		return "redirect:/listAlumnos";

	}

	@RequestMapping(value = "/agregarDelegado/{alumnoId}")
	public String agregarDelegado(@PathVariable("alumnoId") final long alumnoId, Model model,
			HttpServletRequest request) {

		Alumno alumno = this.alumnoService.findById(alumnoId);
		Grupo grupo = this.grupoService.findById((long) request.getSession().getAttribute("grupoId"));

		alumno.setGrupoDelegado(grupo);
		this.alumnoService.saveAlumno(alumno);

		if (grupo.getDelegados().size() < 4) {

			model.addAttribute("addDelegadoAgain", 1);
		} else {
			model.addAttribute("addDelegadoAgain", 0);
			model.addAttribute("grupo", grupo);
		}

		return "grupo/inicioGrupoAdd";

	}

	@GetMapping(value = "/detallesAlumno/{alumnoId}")
	public String alumnoDetails(Model model, @PathVariable("alumnoId") final long alumnoId,
			HttpServletRequest request) {

		if (request.getSession().getAttribute("siAlumno") != null) {

			model.addAttribute("siAlumno", 1);
		}

		Alumno alumno = this.alumnoService.findById(alumnoId);

		model.addAttribute("alumno", alumno);
		request.getSession().removeAttribute("siAlumno");

		return "alumno/alumnoDetails";

	}

	@GetMapping(value = "/alumnoDelete/{alumnoId}")
	public String alumnoDelete(Model model, @PathVariable("alumnoId") final long alumnoId, HttpServletRequest request) {

		this.alumnoService.eliminaAlumnoById(alumnoId);

		return "redirect:/listAlumnos";

	}

	@GetMapping(value = "/alumnoUpdate")
	public String alumnoUpdateModal(@RequestParam(name = "alumnoId") final long alumnoId, Model model,
			HttpServletRequest request) {

		Alumno alumno = this.alumnoService.findById(alumnoId);

		model.addAttribute("alumnoSeleccionado", alumno);
		List<Alumno> alumnos = this.alumnoService.findAll();
		Usuario usuarioActual = usuarioActual();
		Alumno alumnoActual = usuarioActual.getAlumno();
		alumnos.removeIf(x->x.getId()==alumnoActual.getId());
		
		model.addAttribute("alumnos", alumnos);

		return "alumno/alumnoList";

	}

	@PostMapping(value = "/alumnoUpdate")
	public String alumnoUpdate(@Valid @ModelAttribute("alumnoSeleccionado") Alumno alumno, BindingResult result,
			Model model, HttpServletRequest request) {

		this.validarAlumno(alumno, result, true);

		if (result.hasErrors()) {
			model.addAttribute("alumnoSeleccionado", alumno);
			List<Alumno> alumnos = this.alumnoService.findAll();
			Usuario usuarioActual = usuarioActual();
			Alumno alumnoActual = usuarioActual.getAlumno();
			alumnos.removeIf(x->x.getId()==alumnoActual.getId());
			
			model.addAttribute("alumnos", alumnos);

			return "alumno/alumnoList";
		} else {

			this.alumnoService.saveAlumno(alumno);

			return "redirect:/listAlumnos";
		}

	}
	
	@GetMapping(value = "/alumnoDeleteSeguridad/{alumnoId}")
	public String alumnoDeleteModal(@PathVariable("alumnoId") final long alumnoId, Model model,
			HttpServletRequest request) {


		model.addAttribute("idAlumnoSeleccionado", alumnoId);
		List<Alumno> alumnos = this.alumnoService.findAll();
		Usuario usuarioActual = usuarioActual();
		Alumno alumnoActual = usuarioActual.getAlumno();
		alumnos.removeIf(x->x.getId()==alumnoActual.getId());
		
		model.addAttribute("alumnos", alumnos);

		return "alumno/alumnoList";

	}
	
	

	public void validarAlumno(Alumno alumno, BindingResult result, boolean checkUpdate) {

		// validar que el email es único
		if (checkUpdate) {

			Alumno alumnoBd = this.alumnoService.findById(alumno.getId());

			String email = alumno.getEmail();

			String emailBD = alumnoBd.getEmail();

			if (!email.equals(emailBD)) {

				if (this.alumnoService.numeroAlumnosByEmail(alumno.getEmail()) != 0) {
					result.rejectValue("email", "email", "El email introducido ya está registrado");
				}

			}
		} else {
			if (this.alumnoService.numeroAlumnosByEmail(alumno.getEmail()) != 0) {
				result.rejectValue("email", "email", "El email introducido ya está registrado");
			}
		}
		
		if(alumno.getFechaNacimiento()!=null) {
		LocalDate hoy = LocalDate.now();
		LocalDate fechaNacimiento = alumno.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		if(Period.between(fechaNacimiento, hoy).getYears()<17 || Period.between(fechaNacimiento, hoy).getYears()>85) {
			result.rejectValue("fechaNacimiento", "fechaNacimiento", "El alumno debe tener una edad entre los 17 y los 85 años");
		}
		}
		
		if(!alumno.getNombre().equals("") && alumno.getNombre().trim().equals("")) {
			result.rejectValue("nombre", "nombre", "Debes introducir un nombre válido");
		}
		
		if(!alumno.getApellidos().equals("") && alumno.getApellidos().trim().equals("")) {
			result.rejectValue("apellidos", "apellidos", "Debes introducir unos apellidos válido");
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
