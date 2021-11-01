package com.dlgaApp.controller;

import java.io.IOException;
import java.lang.annotation.Target;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.entity.Departamento;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.service.AsignaturaServiceImpl;
import com.dlgaApp.service.DepartamentoServiceImpl;
import com.dlgaApp.service.ProfesorService;

@Controller
public class DepartamentoController {

	@Autowired
	private DepartamentoServiceImpl departamentoService;

	@Autowired
	private AsignaturaServiceImpl asignaturaService;

	@Autowired
	private ProfesorService profesorService;

	@GetMapping(value = "/departamentos")
	public String departamentos(Model model) throws IOException, KeyManagementException, NoSuchAlgorithmException {

		departamentoService.deleteAllDepartamento();
		departamentoService.añadirDepartamentos();

		return "recursos/index";

	}

	@GetMapping(value = "/listDepartamentos")
	public String listDepartamentos(Model model) {

		model.addAttribute("departamentos", departamentoService.listaDepartamento());

		return "departamento/listDepartamentos";
	}

	@GetMapping(value = "/detallesDepartamento/{departamentoId}")
	public String detallesDepartamento(@PathVariable("departamentoId") final long departamentoId, Model model) {

		Departamento departamento = this.departamentoService.getDepartamentoById(departamentoId);
		
		model.addAttribute("departamentos", departamentoService.listaDepartamento());
		model.addAttribute("departamentoDetalles", departamento);
		String detallesAsignatura = "";
		for(Asignatura a: departamento.getAsignaturas()) {
			detallesAsignatura = detallesAsignatura + "- " + a.getNombre() + " (" 
		+ a.getTitulacion().getNombre() + ")\n";
		}
		model.addAttribute("detallesAsignatura", detallesAsignatura);
		
		return "departamento/listDepartamentos";
	}

	@GetMapping(value = "/departamentoDelete/{departamentoId}")
	public String departamentoDelete(Model model, @PathVariable("departamentoId") final long departamentoId,
			HttpServletRequest request) {

		this.departamentoService.removeDepartamentoById(departamentoId);

		return "redirect:/listDepartamentos";

	}

	@GetMapping(value = "/departamentoUpdate")
	public String asignaturaUpdateModal(@RequestParam(name = "departamentoId") final long departamentoId, Model model,
			HttpServletRequest request) {

		Departamento departamento = this.departamentoService.getDepartamentoById(departamentoId);
		List<Asignatura> asignaturas = this.asignaturaService.findAll();
		List<Asignatura> listaAsignatura = new ArrayList<Asignatura>();

		for (Asignatura asig : asignaturas) {
			if (asig.getDepartamento() == null || asig.getDepartamento().getId() == departamento.getId()) {
				listaAsignatura.add(asig);

			}
		}
		Map<String, String> opcionesAsignatura = new HashMap<String, String>();

		for (Asignatura a : listaAsignatura) {
			opcionesAsignatura.put(a.getId().toString(), a.getNombre() + "(" + a.getTitulacion().getNombre() + ")");

		}

		List<Profesor> profesores = this.profesorService.profesorList();
		List<Profesor> listaProfesor = new ArrayList<Profesor>();

		for (Profesor prof : profesores) {
			if (prof.getDepartamento() == null || prof.getDepartamento().getId() == departamento.getId()) {
				listaProfesor.add(prof);

			}
		}

		Map<String, String> opcionesProfesor = new HashMap<String, String>();

		for (Profesor p : listaProfesor) {
			opcionesProfesor.put(p.getId().toString(), p.getNombre());

		}

		model.addAttribute("departamentoSeleccionado", departamento);
		model.addAttribute("departamentos", departamentoService.listaDepartamento());
		model.addAttribute("opcionesSede", this.departamentoService.obtenerSedes());
		model.addAttribute("opcionesAsignatura", opcionesAsignatura);
		model.addAttribute("opcionesProfesor", opcionesProfesor);

		return "departamento/listDepartamentos";

	}

	@PostMapping(value = "/departamentoUpdate")
	public String departamentoUpdate(@Valid @ModelAttribute("departamentoSeleccionado") Departamento departamento,
			BindingResult result, Model model, HttpServletRequest request) {

		this.validarDepartamento(departamento, result);

		if (result.hasErrors()) {

			List<Asignatura> asignaturas = this.asignaturaService.findAll();
			List<Asignatura> listaAsignatura = new ArrayList<Asignatura>();

			for (Asignatura asig : asignaturas) {
				if (asig.getDepartamento() == null || asig.getDepartamento().getId() == departamento.getId()) {
					listaAsignatura.add(asig);

				}
			}
			Map<String, String> opcionesAsignatura = new HashMap<String, String>();

			for (Asignatura a : listaAsignatura) {
				opcionesAsignatura.put(a.getId().toString(), a.getNombre() + "(" + a.getTitulacion().getNombre() + ")");

			}
			List<Profesor> profesores = this.profesorService.profesorList();
			List<Profesor> listaProfesor = new ArrayList<Profesor>();

			for (Profesor prof : profesores) {
				if (prof.getDepartamento() == null || prof.getDepartamento().getId() == departamento.getId()) {
					listaProfesor.add(prof);

				}
			}

			Map<String, String> opcionesProfesor = new HashMap<String, String>();

			for (Profesor p : listaProfesor) {
				opcionesProfesor.put(p.getId().toString(), p.getNombre());

			}

			model.addAttribute("departamentoSeleccionado", departamento);
			model.addAttribute("departamentos", departamentoService.listaDepartamento());
			model.addAttribute("opcionesSede", this.departamentoService.obtenerSedes());
			model.addAttribute("opcionesAsignatura", opcionesAsignatura);
			model.addAttribute("opcionesProfesor", opcionesProfesor);

			return "departamento/listDepartamentos";
		} else {

			List<Asignatura> asignaturasAntes = this.departamentoService.getDepartamentoById(departamento.getId())
					.getAsignaturas();
			List<Asignatura> asignaturaDespues = departamento.getAsignaturas();
			List<Asignatura> asignaturasTotales = new ArrayList<Asignatura>(asignaturaDespues);
			asignaturasTotales.addAll(asignaturasAntes);

			for (Asignatura a : asignaturasTotales) {
				if ((asignaturasAntes.contains(a) && asignaturaDespues.contains(a))
						|| (!asignaturasAntes.contains(a) && asignaturaDespues.contains(a))) {
					a.setDepartamento(departamento);
				}
				if (asignaturasAntes.contains(a) && !asignaturaDespues.contains(a)) {
					a.setDepartamento(null);
				}
				this.asignaturaService.saveAsignatura(a);
			}

			List<Profesor> profesorAntes = this.departamentoService.getDepartamentoById(departamento.getId())
					.getProfesores();
			List<Profesor> profesorDespues = departamento.getProfesores();
			List<Profesor> profesorTotales = new ArrayList<Profesor>(profesorDespues);
			profesorTotales.addAll(profesorAntes);

			for (Profesor p : profesorTotales) {
				if ((profesorAntes.contains(p) && profesorDespues.contains(p))
						|| (!profesorAntes.contains(p) && profesorDespues.contains(p))) {
					p.setDepartamento(departamento);
				}
				if (profesorAntes.contains(p) && !profesorDespues.contains(p)) {
					p.setDepartamento(null);
				}
				this.profesorService.save(p);
			}

			this.departamentoService.save(departamento);

			return "redirect:/listDepartamentos";
		}

	}

	@GetMapping(value = "/departamentoCreate")
	public String departametoCreateGet(Model model, HttpServletRequest request) {

		Departamento departamento = new Departamento();

		List<Asignatura> asignaturas = this.asignaturaService.findAll();
		List<Asignatura> listaAsignatura = new ArrayList<Asignatura>();

		for (Asignatura asig : asignaturas) {
			if (asig.getDepartamento() == null || asig.getDepartamento().getId() == departamento.getId()) {
				listaAsignatura.add(asig);

			}
		}
		Map<String, String> opcionesAsignatura = new HashMap<String, String>();

		for (Asignatura a : listaAsignatura) {
			opcionesAsignatura.put(a.getId().toString(), a.getNombre() + "(" + a.getTitulacion().getNombre() + ")");

		}
		List<Profesor> profesores = this.profesorService.profesorList();
		List<Profesor> listaProfesor = new ArrayList<Profesor>();

		for (Profesor prof : profesores) {
			if (prof.getDepartamento() == null || prof.getDepartamento().getId() == departamento.getId()) {
				listaProfesor.add(prof);

			}
		}

		Map<String, String> opcionesProfesor = new HashMap<String, String>();

		for (Profesor p : listaProfesor) {
			opcionesProfesor.put(p.getId().toString(), p.getNombre());

		}

		model.addAttribute("departamentoCreada", departamento);
		model.addAttribute("departamentos", departamentoService.listaDepartamento());
		model.addAttribute("opcionesSede", this.departamentoService.obtenerSedes());
		model.addAttribute("opcionesAsignatura", opcionesAsignatura);
		model.addAttribute("opcionesProfesor", opcionesProfesor);

		return "departamento/listDepartamentos";

	}

	@PostMapping(value = "/departamentoCreate")
	public String departamentoCretePost(@Valid @ModelAttribute("departamentoCreada") Departamento departamento,
			BindingResult result, Model model, HttpServletRequest request) {

		this.validarDepartamento(departamento, result);

		if (result.hasErrors()) {

			List<Asignatura> asignaturas = this.asignaturaService.findAll();
			List<Asignatura> listaAsignatura = new ArrayList<Asignatura>();

			for (Asignatura asig : asignaturas) {
				if (asig.getDepartamento() == null || asig.getDepartamento().getId() == departamento.getId()) {
					listaAsignatura.add(asig);

				}
			}
			Map<String, String> opcionesAsignatura = new HashMap<String, String>();

			for (Asignatura a : listaAsignatura) {
				opcionesAsignatura.put(a.getId().toString(), a.getNombre() + "(" + a.getTitulacion().getNombre() + ")");

			}
			List<Profesor> profesores = this.profesorService.profesorList();
			List<Profesor> listaProfesor = new ArrayList<Profesor>();

			for (Profesor prof : profesores) {
				if (prof.getDepartamento() == null || prof.getDepartamento().getId() == departamento.getId()) {
					listaProfesor.add(prof);

				}
			}

			Map<String, String> opcionesProfesor = new HashMap<String, String>();

			for (Profesor p : listaProfesor) {
				opcionesProfesor.put(p.getId().toString(), p.getNombre());

			}

			model.addAttribute("departamentoCreada", departamento);
			model.addAttribute("departamentos", departamentoService.listaDepartamento());
			model.addAttribute("opcionesSede", this.departamentoService.obtenerSedes());
			model.addAttribute("opcionesAsignatura", opcionesAsignatura);
			model.addAttribute("opcionesProfesor", opcionesProfesor);

			return "departamento/listDepartamentos";
		} else {

			this.departamentoService.save(departamento);

			List<Asignatura> asignaturaDespues = departamento.getAsignaturas();

			for (Asignatura a : asignaturaDespues) {
				a.setDepartamento(departamento);
				this.asignaturaService.saveAsignatura(a);
			}

			List<Profesor> profesorDespues = departamento.getProfesores();

			for (Profesor p : profesorDespues) {
				p.setDepartamento(departamento);
				this.profesorService.save(p);
			}

			return "redirect:/listDepartamentos";
		}

	}

	@GetMapping(value = "/departamentoDeleteSeguridad/{departamentoId}")
	public String departamentoDeleteModal(@PathVariable("departamentoId") final long departamentoId, Model model,
			HttpServletRequest request) {

		model.addAttribute("idDepartamentoSeleccionado", departamentoId);
		model.addAttribute("departamentos", departamentoService.listaDepartamento());

		return "departamento/listDepartamentos";

	}

	public void validarDepartamento(Departamento departamento, BindingResult result) {

		String regex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

		String regexTelefono = "(\\+34|0034|34)?[ -]*(6|7|8|9)[ -]*([0-9][ -]*){8}";

		String regexWeb = "/^http[s]?:\\/\\/[\\w]+([\\.]+[\\w]+)+$/";

		if (departamento.getNombre().trim().equals("")) {
			result.rejectValue("nombre", "nombre", "El nombre no puede ser nulo");
		}

		if (departamento.getSede().trim().equals("")) {
			result.rejectValue("sede", "sede", "La Sede no puede ser nula");
		}

		if (departamento.getEmail().trim().equals("") || !Pattern.matches(regex, departamento.getEmail())) {
			result.rejectValue("email", "email", "El emáil debe ser válido");
		}

		if (!departamento.getTelefono().equals("") && !Pattern.matches(regexTelefono, departamento.getTelefono())) {

			result.rejectValue("telefono", "telefono", "El teléfono debe ser válido");
		}

		
	}
}