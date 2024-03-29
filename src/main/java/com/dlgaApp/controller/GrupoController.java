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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Grupo;
import com.dlgaApp.entity.Titulacion;
import com.dlgaApp.repository.GrupoRepository;
import com.dlgaApp.service.AlumnoServiceImpl;
import com.dlgaApp.service.GrupoServiceImpl;
import com.dlgaApp.service.TitulacionService;

@Controller
public class GrupoController {

	@Autowired
	private GrupoServiceImpl grupoService;

	@Autowired
	private TitulacionService titulacionService;

	@Autowired
	private AlumnoServiceImpl alumnoService;

	@GetMapping(value = "/crearGrupo")
	public String crearGrupoForm(Model model) {

		Grupo grupo = new Grupo();
		List<Titulacion> t = titulacionService.findAll();
		List<Grupo> l = this.grupoService.findAll();
		model.addAttribute("grupos", l);

		model.addAttribute("grupo", grupo);
		model.addAttribute("titulaciones", t);
		return "grupo/grupoList";

	}

	@PostMapping(value = "/crearGrupo")
	public String crearGrupo(@Valid @ModelAttribute("grupo") Grupo grupo, BindingResult result, Model model,
			HttpServletRequest request,RedirectAttributes redirectAttributes) {

		validarGrupo(grupo, result);

		if (result.hasErrors()) {
			List<Titulacion> t = titulacionService.findAll();
			
			List<Grupo> l = this.grupoService.findAll();
			model.addAttribute("grupos", l);

			model.addAttribute("grupo", grupo);
			model.addAttribute("titulaciones", t);
			return "grupo/grupoList";
		} else {

			grupoService.save(grupo);

		}
		redirectAttributes.addFlashAttribute("alert", 1);
		return "redirect:/grupoList";

	}

	@GetMapping(value = "/grupoList")
	public String grupoList(Model model, @ModelAttribute("alert") final Object alert) {

		List<Grupo> l = this.grupoService.findAll();
		model.addAttribute("grupos", l);
		model.addAttribute("alert", alert);

		return "grupo/grupoList";

	}

	@GetMapping(value = "añadirDelegado/{grupoId}")
	public String inicioAddDelegado(Model model, HttpServletRequest request,
			@PathVariable("grupoId") final long grupoId) {

		Integer check = (int) this.alumnoService.numeroAlumnos();

		model.addAttribute("check", check);
		Grupo grupo = this.grupoService.findById(grupoId);
		model.addAttribute("grupo", grupo);
		model.addAttribute("update", 1);

		request.getSession().setAttribute("grupoId", grupoId);
		request.getSession().setAttribute("op", "addDelegado");

		return "delegado/updateDelegados";

	}

	@GetMapping(value = "addDelegadoAgain")
	public String addDelegadoAgain(Model model, HttpServletRequest request) {

		Grupo grupo = this.grupoService.findById((long) request.getSession().getAttribute("grupoId"));

		if (grupo.getDelegados().size() < 4 && request.getSession().getAttribute("op") == "addDelegado") {

			Integer check = (int) this.alumnoService.numeroAlumnos();

			model.addAttribute("check", check);
			model.addAttribute("grupo", grupo);
			return "delegado/updateDelegados";
		} else {
			return "recursos/index";
		}
	}

	@GetMapping(value = "delegados/{grupoId}")
	public String listDelegados(Model model, @PathVariable("grupoId") final long grupoId,HttpServletRequest request) {

		Grupo grupo = this.grupoService.findById(grupoId);
		model.addAttribute("grupo", grupo);
		model.addAttribute("update", 0);
		request.getSession().removeAttribute("grupoId");
		request.getSession().removeAttribute("op");
		return "delegado/updateDelegados";
	}

	@GetMapping(value = "modificarDelegados/{grupoId}")
	public String updateDelegados(Model model, @PathVariable("grupoId") final long grupoId) {

		Grupo grupo = this.grupoService.findById(grupoId);
		model.addAttribute("grupo", grupo);
		model.addAttribute("update", 1);

		return "delegado/updateDelegados";
	}

	@GetMapping(value = "eliminarDelegado/{grupoId}/{alumnoId}")
	public String deleteDelegado(Model model, @PathVariable("grupoId") final long grupoId,
			@PathVariable("alumnoId") final long alumnoId) {

		Alumno alumno = this.alumnoService.findById(alumnoId);
		alumno.setGrupoDelegado(null);
		this.alumnoService.saveAlumno(alumno);

		return "redirect:/modificarDelegados/" + String.valueOf(grupoId);
	}

	@RequestMapping(value = "eliminarGrupo/{grupoId}")
	public String deleteGrupo(Model model, @PathVariable("grupoId") final long grupoId,RedirectAttributes redirectAttributes) {

		Grupo grupo = this.grupoService.findById(grupoId);

		List<Alumno> delegados = grupo.getDelegados();

		for (Alumno delegado : delegados) {
			delegado.setGrupoDelegado(null);
			this.alumnoService.saveAlumno(delegado);
		}

		this.grupoService.deleteById(grupoId);
		redirectAttributes.addFlashAttribute("alert", 3);

		return "redirect:/grupoList";

	}
	
	@GetMapping(value = "/eliminarGrupoSeguridad/{grupoId}")
	public String departamentoDeleteModal(@PathVariable("grupoId") final long grupoId, Model model,
			HttpServletRequest request) {

		model.addAttribute("idGrupoSeleccionado", grupoId);
		List<Grupo> l = this.grupoService.findAll();
		model.addAttribute("grupos", l);

		return "grupo/grupoList";

	}


	public void validarGrupo(Grupo grupo, BindingResult result) {

		List<Long> idsTitulaciones = this.titulacionService.getIdsTitulaciones();
		
		// validar que el grupo es único
		if (this.grupoService.numeroGruposRepetidos(grupo.getCurso(), grupo.getNumerogrupo(), grupo.getEsingles(),
				grupo.getTitulacion().getId()) != 0) {
			result.reject("unico", "El grupo introducido ya está registrado en la base de datos");
		}
		if( grupo.getTitulacion() == null|| grupo.getTitulacion().getId()  == null ||
				!idsTitulaciones.contains(grupo.getTitulacion().getId())) {
			result.rejectValue("titulacion.id", "titulacion.id", "El valor indicado no es correcto");
		}
		
	}

}
