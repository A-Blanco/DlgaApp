package com.dlgaApp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Roles;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.AlumnoServiceImpl;
import com.dlgaApp.service.MailService;
import com.dlgaApp.service.UserDetailsServiceImpl;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

@Controller
public class UserController {

	@Autowired
	private UserDetailsServiceImpl usuarioService;

	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private MailService mailService;

	@GetMapping(value = "/crearUsuario")
	public String crearUsuarioForm(Model model, HttpServletRequest request) {

		Usuario usuario = new Usuario();

		Usuario us = this.usuarioActual();

		if (us != null && (us.getRol().equals(Roles.ROLE_MIEMBRO) || us.getRol().equals(Roles.ROLE_ADMIN))) {
			usuario.setRol(Roles.ROLE_MIEMBRO);
		} else {
			usuario.setRol(Roles.ROLE_REGISTRADO);
		}
		String tipo = "usuario";

		request.getSession().setAttribute("usuario", usuario);
		request.getSession().setAttribute("tipo", tipo);

		return "redirect:/crearAlumno";
	}

	@PostMapping(value = "/crearUsuario")
	public String crearusuario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model
			,RedirectAttributes redirectAttributes) {

		validarUsuario(usuario, result);

		if (result.hasErrors()) {
			model.addAttribute("usuario", usuario);
			return "usuario/formUser";
		} else {

			usuarioService.creaUsuario(usuario);
			;
		}
		redirectAttributes.addFlashAttribute("alert", 11);
		return "redirect:";

	}

	@GetMapping(value = "/crearUsuarioRegistrado")
	public String crearUsuarioRegistrado(@RequestParam(required = false, name = "email") String email, Model model,
			HttpServletRequest request) {

		Alumno alumno = this.alumnoService.findByEmail(email);

		if (alumno != null && alumno.getUsuario() != null) {
			request.getSession().setAttribute("siUsuario", 1);
			return "redirect:/crearUsuario";
		} else if (alumno != null && alumno.getUsuario() == null) {
			request.getSession().setAttribute("siAlumno", 1);
			return "redirect:/detallesAlumno/" + alumno.getId();
		} else {
			request.getSession().setAttribute("noAlumno", 1);
			return "redirect:/crearUsuario";
		}

	}

	@PostMapping(value = "/crearUsuarioRegistrado")
	public String crearUsuarioRegistradoPost(@RequestParam(required = true, name = "alumnoId") Long alumnoId,
			Model model, HttpServletRequest request) {

		Alumno alumno = this.alumnoService.findById(alumnoId);
		request.getSession().setAttribute("Alumno", alumno);

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
		usuario.setAlumno(alumno);

		model.addAttribute("usuario", usuario);

		request.getSession().removeAttribute("tipo");
		request.getSession().removeAttribute("usuario");
		request.getSession().removeAttribute("Alumno");
		return "usuario/formUser";

	}

	public void validarUsuario(Usuario usuario, BindingResult result) {

		// validar que el username es único
		if (usuarioService.numeroUsuariosByUsername(usuario.getUsername()) != 0) {
			result.rejectValue("username", "username", "El usuario introducido ya existe");
		}

		// validar que el numero de telefono es único
		if (usuarioService.numeroUsuariosByTelefono(usuario.getTelefono()) != 0) {
			result.rejectValue("telefono", "telefono", "El telefono introducido ya está registrado");
		}

		// validación de las 2 contraseñas
		if (!usuario.getPassword().equals(usuario.getPassword2())) {
			result.rejectValue("password", "password", "Las contraseñas no coinciden");
		}

	}

	@GetMapping(value = "/listaUsuario")
	public String listarUsuarios(Model model,@ModelAttribute("alert") final Object alert) {

		List<Usuario> l = new ArrayList<Usuario>();
		l = usuarioService.findAllUsers();
		l.removeIf(x->x.getId()==this.usuarioActual().getId());

		model.addAttribute("usuarios", l);
		model.addAttribute("alert", alert);

		return "usuario/listUsuario";
	}

	@PostMapping(value = "/eliminarUsuario")
	public String eliminaUsuario(@RequestParam(defaultValue = "false",name = "checkbox") boolean checkbox,
			@RequestParam(name = "usuarioId") long usuarioId, Model model,RedirectAttributes redirectAttributes) {

		Usuario usuario = this.usuarioService.findById(usuarioId);
		Alumno alumno = usuario.getAlumno();
		
		if(checkbox) {
			
			alumno.setUsuario(null);
			this.alumnoService.saveAlumno(alumno);
			this.usuarioService.eliminarUsuario(usuario);
			redirectAttributes.addFlashAttribute("alert", 3);
			return "redirect:/listaUsuario";
			
		}else {
			
			
			this.alumnoService.eliminaAlumnoById(alumno.getId());
			redirectAttributes.addFlashAttribute("alert", 3);
			return "redirect:/listaUsuario";
		}
		
	}

	@GetMapping(value = "/usuario/{usuarioId}")
	public String detallesUsuario(@PathVariable("usuarioId") final long usuarioId, Model model) {

		Usuario usuario = this.usuarioService.findById(usuarioId);

		model.addAttribute("usuario", usuario);

		return "usuario/detallesUsuario";
	}

	@GetMapping(value = "/usuariosNoAceptados")
	public String usuariosNoAceptados(Model model,@ModelAttribute("alert") final Object alert) {

		List<Usuario> usuarios = this.usuarioService.findAllUsuarios().stream()
				.filter(x -> x.getRol().equals(Roles.ROLE_REGISTRADO)).collect(Collectors.toList());

		model.addAttribute("usuarios", usuarios);
		model.addAttribute("alert", alert);

		return "usuario/usuarioListNoAceptados";
	}

	@GetMapping(value = "/aceptarUsuario/{usuarioId}")
	public String aceptarUsuario(@PathVariable("usuarioId") final long usuarioId, Model model
			,RedirectAttributes redirectAttributes) throws MailjetException, MailjetSocketTimeoutException {

		Usuario usuario = this.usuarioService.findById(usuarioId);

		usuario.setRol(Roles.ROLE_MIEMBRO);
		this.usuarioService.save(usuario);
		this.mailService.emailAceptadoMiembro(usuario);
		redirectAttributes.addFlashAttribute("alert", 1);
		

		return "redirect:/usuariosNoAceptados";
	}

	@PostMapping(value = "/denegarUsuario")
	public String denegarUsuario(@RequestParam("usuarioId") Long usuarioId,
			@RequestParam(required = false, name = "motivo") String motivo, Model model
			,RedirectAttributes redirectAttributes) throws MailjetException, MailjetSocketTimeoutException {

		Usuario usuario = this.usuarioService.findById(usuarioId);
		usuario.setRol(Roles.ROLE_DENEGADO);

		if (motivo != "" && motivo != null) {
			usuario.setMotivoRechazo(motivo);
		}

		this.usuarioService.save(usuario);
		
		this.mailService.emailRechazoMiembro(usuario);
		redirectAttributes.addFlashAttribute("alert", 2);

		return "redirect:/usuariosNoAceptados";
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
