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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Grupo;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.AlumnoServiceImpl;
import com.dlgaApp.service.GrupoServiceImpl;

@Controller
public class AlumnoController {

	@Autowired
	private AlumnoServiceImpl alumnoService;

	@Autowired
	private GrupoServiceImpl grupoService;

	@GetMapping(value = "/crearAlumno")
	public String crearAlumnoForm(Model model) {

		Alumno alumno = new Alumno();
		model.addAttribute("alumno", alumno);
		return "formAlumno";

	}

	@GetMapping(value = "/listAlumnos")
	public String listAlumnos(Model model) {

		List<Alumno> alumnos = this.alumnoService.findAll();
		model.addAttribute("alumnos", alumnos);

		return "alumnoList";

	}

	@PostMapping(value = "/crearAlumno")
	public String crearAlumno(@Valid @ModelAttribute("alumno") Alumno alumno, BindingResult result, Model model,
			HttpServletRequest request) {

		validarAlumno(alumno, result);

		if (result.hasErrors()) {
			model.addAttribute("alumno", alumno);
			return "formAlumno";
		} else {

			alumnoService.saveAlumno(alumno);

			if (request.getSession().getAttribute("tipo") == "usuario") {

				Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
				usuario.setAlumno(alumno);

				model.addAttribute("usuario", usuario);

				request.getSession().removeAttribute("tipo");
				request.getSession().removeAttribute("usuario");
				return "formUser";
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
				return "inicioGrupoAdd";

			}
		}

		return "redirect:";

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

		return "inicioGrupoAdd";

	}

	public void validarAlumno(Alumno alumno, BindingResult result) {

		// validar que el username es único
		if (alumnoService.numeroAlumnosByEmail(alumno.getEmail()) != 0) {
			result.rejectValue("email", "email", "El email introducido ya está registrado");
		}

	}
}
