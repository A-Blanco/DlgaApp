package com.dlgaApp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.AlumnoServiceImpl;

@Controller
public class AlumnoController {

	@Autowired
	private AlumnoServiceImpl alumnoService;

	@GetMapping(value = "/crearAlumno")
	public String crearAlumnoForm(Model model) {

		Alumno alumno = new Alumno();
		model.addAttribute("alumno", alumno);
		return "formAlumno";

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
		}

		return "redirect:";

	}

	public void validarAlumno(Alumno alumno, BindingResult result) {

		// validar que el username es único
		if (alumnoService.numeroAlumnosByEmail(alumno.getEmail()) != 0) {
			result.rejectValue("email", "email", "El email introducido ya está registrado");
		}

	}
}
