package com.dlgaApp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.entity.Departamento;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.entity.Titulacion;
import com.dlgaApp.repository.AsignaturaRepository;
import com.dlgaApp.repository.DepartamentoRepository;
import com.dlgaApp.repository.ProfesorRepository;
import com.dlgaApp.repository.TitulacionRepository;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

@Service
public class AsignaturaServiceImpl {

	private final String enlaceDGInforMates = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/doble-grado-en-ingenieria-informatica-tecnologias";
	private final String enlaceGIngeSalud = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/grado-en-ingenieria-de-la-salud-por-la-universidad-de-malaga";
	private final String enlaceGIngeComp = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/grado-en-ingenieria-informatica-ingenieria-de-computadores";
	private final String enlaceGIngeSoftw = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/grado-en-ingenieria-informatica-ingenieria-del-software";
	private final String enlaceGIngeTecnol = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/grado-en-ingenieria-informatica-tecnologias-informaticas";

	private final String enlaceMIngeBioySalud = "https://www.us.es/estudiar/que-estudiar/oferta-de-masteres/master-universitario-en-ingenieria-biomedica-y-salud";
	private final String enlaceMInfor = "https://www.us.es/estudiar/que-estudiar/oferta-de-masteres/master-universitario-en-ingenieria-informatica";
	private final String enlaceMInteligencia = "https://www.us.es/estudiar/que-estudiar/oferta-de-masteres/master-universitario-en-logica-computacion-e-inteligencia";

	@Autowired
	private AsignaturaRepository asignaturaRepository;

	@Autowired
	private DepartamentoRepository departamentoRepository;

	@Autowired
	private TitulacionRepository titulacionRepository;

	@Autowired
	private ProfesorRepository profesorRepository;

	public List<String> elancesAsignatura(String s) {

		WebClient webClient = new WebClient();
		webClient.getOptions().setSSLClientProtocols(new String[] { "TLSv1.3", "TLSv1.2", "TLSv1.1" });
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);

		List<String> l = new ArrayList<String>();

		try {

			Document docGrados;
			docGrados = Jsoup.parse(webClient.getPage(s).getWebResponse().getContentAsString());

			Element lista = docGrados.getElementsByClass(
					"field field--name-field-tabla-de-asignaturas field--type-viewsreference field--label-hidden field__item")
					.first();
			if (lista != null) {
				Elements enlacesPagina = lista.getElementsByTag("a");

				for (Element e : enlacesPagina) {
					String enlaceAsig = e.attr("href");
					if (enlaceAsig.isEmpty()) {
						webClient.close();
						throw new IOException();
					} else {
						l.add(enlaceAsig);
					}
				}
			} else {
				webClient.close();
				throw new IOException();
			}

			webClient.close();
			System.out.println(l.size());

		} catch (IOException e) {
			System.out.println("reintentando");
			l = elancesAsignatura(s);

		}
		return l;
	}

	public Map<String, String> datosAsignatura(String enlace) {

		WebClient webClient = new WebClient();
		webClient.getOptions().setSSLClientProtocols(new String[] { "TLSv1.3", "TLSv1.2", "TLSv1.1" });
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);

		Map<String, String> map = new HashMap<String, String>();

		String url = "https://www.us.es/" + enlace;

		Document doc;
		try {
			doc = Jsoup.parse(webClient.getPage(url).getWebResponse().getContentAsString());

			Element elemento = doc.getElementsByClass(
					"clearfix text-formatted field field--name-field-cuerpo2 field--type-text-long field--label-hidden field__item")
					.first();

			if (elemento != null) {

				Elements filas = elemento.getElementsByTag("tr");

				for (Element e : filas) {

					map.put(e.getElementsByTag("th").text(), e.getElementsByTag("td").text());
				}
				if (map.isEmpty() || map.containsValue(null) || map.containsKey(null) || map.get("Asignatura") == null
						|| map.get("Asignatura") == "" || map.get("Departamento Responsable") == null
						|| map.get("Departamento Responsable") == "") {
					webClient.close();
					throw new IOException();
				} else {
					webClient.close();
					System.out.println("encontrado");
				}
			}
		} catch (FailingHttpStatusCodeException | IOException e1) {

			map = datosAsignatura(enlace);
		}

		return map;

	}

	public List<String> obtenerProfesores(String enlace) {

		WebClient webClient = new WebClient();
		webClient.getOptions().setSSLClientProtocols(new String[] { "TLSv1.3", "TLSv1.2", "TLSv1.1" });
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);

		String url = "https://www.us.es/" + enlace;

		List<String> res = new ArrayList<String>();

		Document doc;
		try {
			doc = Jsoup.parse(webClient.getPage(url).getWebResponse().getContentAsString());

			Element elemento = doc.getElementsByClass(
					"field field--name-field-profesores field--type-viewsreference field--label-hidden field__item")
					.first();

			if (elemento != null) {

				Elements filas = elemento.getElementsByClass("views-row");

				for (Element e : filas) {

					res.add(e.text());
				}

				System.out.println("Profesores obtenidos");
				webClient.close();
			}

		} catch (FailingHttpStatusCodeException | IOException e1) {

			res = obtenerProfesores(enlace);
		}

		return res;

	}

	public void añadirAsignaturas() {

		this.asignaturaRepository.deleteAll();
		this.titulacionRepository.deleteAll();

		List<String> l = new ArrayList<String>();
		l.add(enlaceDGInforMates);
		l.add(enlaceGIngeComp);
		l.add(enlaceGIngeSalud);
		l.add(enlaceGIngeSoftw);
		l.add(enlaceGIngeTecnol);
		l.add(enlaceMIngeBioySalud);
		l.add(enlaceMInfor);
		l.add(enlaceMInteligencia);

		List<String> listaEnlaces = new ArrayList<String>();

		for (String enlace : l) {

			listaEnlaces.addAll(elancesAsignatura(enlace));

		}

		Integer i = 0;
		for (String enlaceAsig : listaEnlaces) {

			Asignatura asignatura = new Asignatura();
			Map<String, String> map = new HashMap<String, String>();
			List<String> profesores = obtenerProfesores(enlaceAsig);

			map = datosAsignatura(enlaceAsig);

			asignatura.setNombre(map.get("Asignatura"));
			asignatura.setCaracter(map.get("Carácter"));
			asignatura.setDuracion(map.get("Duración"));
			asignatura.setCreditos(map.get("Créditos Totales"));
			asignatura.setAno(map.get("Curso"));
			Departamento departamento = this.departamentoRepository.findByNombre(map.get("Departamento Responsable"));

			asignatura.setDepartamento(departamento);

			if (map.get("Titulacion") != null || map.get("Titulacion") != "") {
				if (!this.titulacionRepository.existsByNombre(map.get("Titulacion"))) {
					Titulacion titulacion = new Titulacion();
					titulacion.setNombre(map.get("Titulacion"));
					this.titulacionRepository.save(titulacion);
					asignatura.setTitulacion(titulacion);
				} else {
					asignatura.setTitulacion(this.titulacionRepository.findByNombre(map.get("Titulacion")));
				}
			}

			this.asignaturaRepository.save(asignatura);
			for (String profesor : profesores) {

				Profesor p = this.profesorRepository.findByNombre(profesor);
				asignatura.addProfesor(p);

			}

			this.asignaturaRepository.save(asignatura);
			i++;
			System.out.println(i);
		}
		// 282
		System.out.println("Se ha añadido" + i);
		this.asignaturaRepository.limpiarTabla();
	}

	public void delete() {

		this.asignaturaRepository.deleteById((long) 2223);
	}
	
	public List<Asignatura> asignaturaList(){
		return (List<Asignatura>) this.asignaturaRepository.findAll();
	}
	
	public Asignatura findById(Long id) {
		
		Asignatura a = this.asignaturaRepository.findById(id).get();
		
		return a;
	}
	
	public void deleteAsignaturaByID(Long id) {
		
		this.asignaturaRepository.deleteById(id);
		
	}

	public List<Asignatura> findAll(){
		return (List<Asignatura>) this.asignaturaRepository.findAll();
	}
	
	@Transactional
	public void saveAsignatura(Asignatura asignatura) {
		
		asignaturaRepository.save(asignatura);
	}
	
	public Long numeroAsignaturasByNombre(String nombre) {
		return this.asignaturaRepository.countByNombre(nombre);
	}
	
	public Long numeroAsignaturasByTitulacion(Titulacion titulacion) {
		return this.asignaturaRepository.countByTitulacion(titulacion);
	}
	
	public List<String> obtenerCaracteres(){
		return this.asignaturaRepository.obtenerCaracters();
	}
	
	public List<String> obtenerAños(){
		return this.asignaturaRepository.obtenerAños();
	}
	
	public List<String> obtenerCreditos(){
		return this.asignaturaRepository.obtenerCreditos();
	}
	
	public List<String> obtenerDuraciones(){
		return this.asignaturaRepository.obtenerDuraciones();
	}
	
	public List<Long> getIdsAsignaturas(){
		
		return this.asignaturaRepository.obtenerIds();
	}
	
}
