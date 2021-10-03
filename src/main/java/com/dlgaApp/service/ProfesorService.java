package com.dlgaApp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Departamento;
import com.dlgaApp.entity.Profesor;
import com.dlgaApp.repository.DepartamentoRepository;
import com.dlgaApp.repository.ProfesorRepository;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

@Service
public class ProfesorService {

	@Autowired
	private ProfesorRepository profesorRepository;

	@Autowired
	private DepartamentoRepository departamentoRepository;

	private final String web = "https://www.us.es/trabaja-en-la-us/directorio/personal-docente-e-investigador?title=&title_2=&title_1=informatica&page=";

	public List<String> enlacesProfesores() {

		List<String> l = new ArrayList<String>();

		WebClient webClient = new WebClient();
		webClient.getOptions().setSSLClientProtocols(new String[] { "TLSv1.3", "TLSv1.2", "TLSv1.1" });
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);

		try {

			Integer i = 0;
			Integer check = 1;

			while (check == 1) {
				Document doc;

				doc = Jsoup.parse(webClient.getPage(web + i).getWebResponse().getContentAsString());
				
				if (doc.getElementsByClass("pager__item pager__item--next").isEmpty()){
					check = 0;
				}

				
				Elements elementos = doc.getElementsByClass("url-enlace");
				System.out.println(elementos.size());
				for (Element e : elementos) {
					l.add(e.text());
				}

				System.out.println("pagina " + i + " ok");
				i++;

			}
			webClient.close();
			System.out.println(l.size());
			System.out.println(l);
		} catch (FailingHttpStatusCodeException | IOException e) {
			l = enlacesProfesores();
		}
		return l;
	}

	public Map<String, String> datosProfesor(String enlace) {

		WebClient webClient = new WebClient();
		webClient.getOptions().setSSLClientProtocols(new String[] { "TLSv1.3", "TLSv1.2", "TLSv1.1" });
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);

		Map<String, String> map = new HashMap<String, String>();

		Document doc;
		try {
			doc = Jsoup.parse(webClient.getPage(enlace).getWebResponse().getContentAsString());

			Element elemento = doc.getElementsByClass("col-sm-12 bs-region bs-region--main").first();

			if (elemento != null) {

				String nombre = "";
				if (elemento.getElementsByClass("field--name-node-title").first() != null) {
					nombre = elemento.getElementsByClass("field--name-node-title").first().text();
				} else {
					nombre = "El nombre d no está disponible";
				}

				String telefono = "";
				if (elemento.getElementsByClass("field--type-telephone").first() != null) {
					telefono = elemento.getElementsByClass("field--type-telephone").first()
							.getElementsByClass("field__item").text();
				} else {
					telefono = "El número de telefono no está disponible";
				}

				String email = "";

				if (elemento.getElementsByClass("field--type-email").first() != null) {
					email = elemento.getElementsByClass("field--type-email").first().getElementsByClass("field__item")
							.text();
				} else {
					email = "El email no está disponible";
				}

				String departamento = "";
				
				if (elemento.getElementsByClass("field--name-field-departamento").first() != null) {
					departamento = elemento.getElementsByClass("field--name-field-departamento").first()
							.getElementsByTag("a").text();
				} else {
					departamento = "El departamento no está disponible";
				}


				if (nombre == "" ||nombre == null || telefono == "" || telefono == null || email == ""|| email == null|| departamento == ""||departamento == null) {
					webClient.close();
					System.out.println("mal");
					throw new IOException();
					

				} else {

					map.put("nombre", nombre);
					map.put("telefono", telefono);
					map.put("email", email);
					map.put("departamento", departamento);

					if (map == null||map.isEmpty()||map.get("departamento") == null||map.get("nombre") == null||map.get("email") == null||map.get("telefono") == null) {
						webClient.close();
						System.out.println("mal");
						throw new IOException();
					} else {
						webClient.close();
						System.out.println("encontrado");
					}
				}
			}
		} catch (FailingHttpStatusCodeException | IOException e1) {

			System.out.println("Retry");
			map = datosProfesor(enlace);
		}
		return map;

	}

	public void añadirProfesores() {
		
		this.profesorRepository.deleteAll();
		
		List<String> listaEnlaces = enlacesProfesores();

		

		Integer i = 0;
		for (String enlaceProf : listaEnlaces) {

			Profesor profesor = new Profesor();
			Map<String, String> map = new HashMap<String, String>();

			map = datosProfesor(enlaceProf);
			

			profesor.setNombre(map.get("nombre"));
			profesor.setTelefono(map.get("telefono"));
			profesor.setEmail(map.get("email"));
			Departamento departamento = this.departamentoRepository.findByNombre(map.get("departamento"));

			profesor.setDepartamento(departamento);

			this.profesorRepository.save(profesor);
			i++;
			System.out.println(i);
		}
		// 245
		System.out.println("Se ha añadido" + i);
		this.profesorRepository.limpiarTabla();
	}
	
	public List<Profesor> profesorList(){
		return (List<Profesor>) this.profesorRepository.findAll();
	}
	
	public Profesor findById(Long id) {
		
		Profesor p = this.profesorRepository.findById(id).get();
		
		return p;
	}
	
	public void save(Profesor p) {
		this.profesorRepository.save(p);
	}
}
