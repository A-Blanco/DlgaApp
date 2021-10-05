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
import com.dlgaApp.service.DepartamentoServiceImpl;
import com.dlgaApp.service.TitulacionService;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

@Controller
public class AsignaturaController {
	
	
	@Autowired
	private AsignaturaServiceImpl asignaturaService;
	
	@Autowired
	private DepartamentoServiceImpl departamentoService;
	
	@Autowired
	private TitulacionService titulacionService;
	
	
	@GetMapping(value = "/asignaturas")
	public String cargarAsignaturas(Model model) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		this.asignaturaService.añadirAsignaturas();
		
		return "recursos/index";
	}
	


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
	public String asignaturaDelete(Model model, @PathVariable("asignaturaId") final long asignaturaId, HttpServletRequest request) {

		this.asignaturaService.deleteAsignaturaByID(asignaturaId);

		return "redirect:/asignaturaList";

	}
	
	@GetMapping(value = "/asignaturaUpdate")
	public String asignaturaUpdateModal(@RequestParam(name = "asignaturaId") final long asignaturaId, Model model,
			HttpServletRequest request) {

		Asignatura asignatura = this.asignaturaService.findById(asignaturaId);


		model.addAttribute("asignaturaSeleccionado", asignatura);
		model.addAttribute("asignaturas", this.asignaturaService.findAll());
		model.addAttribute("opcionesCaracter", this.asignaturaService.obtenerCaracteres());
		model.addAttribute("opcionesDuracion", this.asignaturaService.obtenerDuraciones());
		model.addAttribute("opcionesCredito", this.asignaturaService.obtenerCreditos());
		model.addAttribute("opcionesAno", this.asignaturaService.obtenerAños());
		model.addAttribute("opcionesDepartamento", this.departamentoService.getDepartamentos());
		model.addAttribute("opcionesTitulacion", this.titulacionService.findAll());

		return "asignatura/asignaturaList";

	}
	
	@PostMapping(value = "/asignaturaUpdate")
	public String asignaturaUpdate(@Valid @ModelAttribute("asignaturaSeleccionado") Asignatura asignatura, BindingResult result,
			Model model, HttpServletRequest request) {

		this.validarAsignatura(asignatura, result, true);

		if (result.hasErrors()) {
			
			model.addAttribute("asignaturaSeleccionado", asignatura);
			model.addAttribute("asignaturas", this.asignaturaService.findAll());
			model.addAttribute("opcionesCaracter", this.asignaturaService.obtenerCaracteres());
			model.addAttribute("opcionesDuracion", this.asignaturaService.obtenerDuraciones());
			model.addAttribute("opcionesCredito", this.asignaturaService.obtenerCreditos());
			model.addAttribute("opcionesAno", this.asignaturaService.obtenerAños());
			model.addAttribute("opcionesDepartamento", this.departamentoService.getDepartamentos());
			model.addAttribute("opcionesTitulacion", this.titulacionService.findAll());
			

			return "asignatura/asignaturaList";
		} else {

			this.asignaturaService.saveAsignatura(asignatura);

			return "redirect:/asignaturaList";
		}

	}
	
	@GetMapping(value = "/asignaturaCreate")
	public String asignaturaCreateGet(
			Model model, HttpServletRequest request) {

		Asignatura asignatura = new Asignatura();
		
		
		model.addAttribute("asignaturaCreada", asignatura);
		
		model.addAttribute("asignaturas", this.asignaturaService.findAll());
		model.addAttribute("opcionesCaracter", this.asignaturaService.obtenerCaracteres());
		model.addAttribute("opcionesDuracion", this.asignaturaService.obtenerDuraciones());
		model.addAttribute("opcionesCredito", this.asignaturaService.obtenerCreditos());
		model.addAttribute("opcionesAno", this.asignaturaService.obtenerAños());
		model.addAttribute("opcionesDepartamento", this.departamentoService.getDepartamentos());
		model.addAttribute("opcionesTitulacion", this.titulacionService.findAll());
		
		return "asignatura/asignaturaList";

	}
	
	@PostMapping(value = "/asignaturaCreate")
	public String asignaturaCreatePost(@Valid @ModelAttribute("asignaturaCreada") Asignatura asignatura, BindingResult result,
			Model model, HttpServletRequest request) {

		this.validarAsignatura(asignatura, result, false);

		if (result.hasErrors()) {
			
			model.addAttribute("asignaturaCreada", asignatura);
			model.addAttribute("asignaturas", this.asignaturaService.findAll());
			model.addAttribute("opcionesCaracter", this.asignaturaService.obtenerCaracteres());
			model.addAttribute("opcionesDuracion", this.asignaturaService.obtenerDuraciones());
			model.addAttribute("opcionesCredito", this.asignaturaService.obtenerCreditos());
			model.addAttribute("opcionesAno", this.asignaturaService.obtenerAños());
			model.addAttribute("opcionesDepartamento", this.departamentoService.getDepartamentos());
			model.addAttribute("opcionesTitulacion", this.titulacionService.findAll());
			

			return "asignatura/asignaturaList";
		} else {

			this.asignaturaService.saveAsignatura(asignatura);

			return "redirect:/asignaturaList";
		}

	}
	
	public void validarAsignatura(Asignatura asignatura, BindingResult result, boolean checkUpdate) {

		List<Long> idsDepartamentos = this.departamentoService.getIdsDepartamentos();
		List<Long> idsTitulaciones = this.titulacionService.getIdsTitulaciones();
		List<String> nombresCaracters = this.asignaturaService.obtenerCaracteres();
		List<String> nombresDuracions = this.asignaturaService.obtenerDuraciones();	
		List<String> nombresCreditos = this.asignaturaService.obtenerCreditos();
		List<String> nombresAños = this.asignaturaService.obtenerAños();
		
		
		if(asignatura.getNombre().equals("")) {
			result.rejectValue("nombre", "nombre", "El nombre no puede ser nulo");
}
		
		if(asignatura.getCaracter().equals("") || !nombresCaracters.contains(asignatura.getCaracter())) {
			result.rejectValue("caracter", "caracter", "El valor indicado no es corrector");
		}
		
		if(asignatura.getDuracion().equals("") || !nombresDuracions.contains(asignatura.getDuracion())) {
			result.rejectValue("duracion", "duracion", "El valor indicado no es corrector");
		}
		
		if(asignatura.getCreditos().equals("") || !nombresCreditos.contains(asignatura.getCreditos())) {
			result.rejectValue("creditos", "creditos", "El valor indicado no es corrector");
		}
		
		if(asignatura.getAno().equals("") || !nombresAños.contains(asignatura.getAno())) {
			result.rejectValue("ano", "ano", "El valor indicado no es corrector");
		}
		
		if(asignatura.getDepartamento() == null ||
				!idsDepartamentos.contains(asignatura.getDepartamento().getId())) {
			result.rejectValue("departamento.id", "departamento.id", "El valor indicado no es corrector");
		}
		
		if( asignatura.getTitulacion() == null|| asignatura.getTitulacion().getId()  == null ||
				!idsTitulaciones.contains(asignatura.getTitulacion().getId())) {
			result.rejectValue("titulacion.id", "titulacion.id", "El valor indicado no es corrector");
		}
		
		if(!result.hasErrors()) {
		
		// validar que el email es único
		if (checkUpdate) {

			Asignatura asignaturaBd = this.asignaturaService.findById(asignatura.getId());

			
			
			String nombre = asignatura.getNombre();
			Long titulacionId = asignatura.getTitulacion().getId();
			Titulacion titulacion = this.titulacionService.findById(titulacionId);
			
			String nombreBd = asignaturaBd.getNombre();
			Long titulacionIdBd = asignaturaBd.getTitulacion().getId();
			

			if (!nombre.equals(nombreBd) || !titulacionId.equals(titulacionIdBd) ) {

				if (titulacion.getAsignaturas().stream()
				.map(x->x.getNombre()).anyMatch(a->a.equals(nombre))) {
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

}
