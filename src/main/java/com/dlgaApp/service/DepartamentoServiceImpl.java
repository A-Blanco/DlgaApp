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
import com.dlgaApp.repository.DepartamentoRepository;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

@Service
public class DepartamentoServiceImpl {

	@Autowired
	private DepartamentoRepository departamentoRepository;

	private final String webDepartamentos = "https://www.us.es/centros/escuela-tecnica-superior-de-ingenieria-informatica";

	public List<String> listaDepartamentos() {

		List<String> l = new ArrayList<String>();

		WebClient webClient = new WebClient();

		webClient.getOptions().setSSLClientProtocols(new String[] { "TLSv1.3", "TLSv1.2", "TLSv1.1" });
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);

		Document doc;
		try {
			doc = Jsoup.parse(webClient.getPage(webDepartamentos).getWebResponse().getContentAsString());
			if (doc.getElementsByClass(
					"field field--name-field-departamentos-que-imparten field--type-entity-reference field--label-hidden field--entity-reference-target-type-node clearfix")
					.first() != null) {
				Elements elementos = doc.getElementsByClass(
						"field field--name-field-departamentos-que-imparten field--type-entity-reference field--label-hidden field--entity-reference-target-type-node clearfix")
						.first().getElementsByTag("li");

				for (Element e : elementos) {
					if (l.contains(e.getElementsByTag("a").get(0).attr("href"))) {
						webClient.close();
						throw new IOException("Datos no encontrado");
					} else {
						l.add(e.getElementsByTag("a").get(0).attr("href"));
					}
				}
			} else {
				webClient.close();
				throw new IOException("Datos no encontrado");
			}

			webClient.close();
			System.out.println("encontrados los enlaces");
			System.out.println(l.size());
		} catch (FailingHttpStatusCodeException | IOException e) {

			if (e.getMessage().equals("Datos no encontrado")) {
				System.out.println("Reintentando lista");
				l = listaDepartamentos();
			}
		}

		return l;
	}

	public Map<String, String> datosDepartamento(String s) {

		Map<String, String> map = new HashMap<String, String>();

		try {
			String url = "https://www.us.es" + s;

			WebClient webClient = new WebClient();
			webClient.getOptions().setSSLClientProtocols(new String[] { "TLSv1.3", "TLSv1.2", "TLSv1.1" });
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			Document doc;
			String r;

			r = webClient.getPage(url).getWebResponse().getContentAsString();

			doc = Jsoup.parse(r);

			String nombre = "";
			Element e = doc.getElementsByClass("text-center noticia").first();
			if (e != null) {
				nombre = doc.getElementsByClass("text-center noticia").first().text();

				System.out.println("nombre =" + nombre);
				map.put("nombre", nombre);
			} else {
				webClient.close();
				throw new IOException("Datos no encontrado");
			}

			String sede;
			if (doc.getElementsByClass(
					"field field--name-field-centro field--type-entity-reference field--label-hidden field--entity-reference-target-type-node clearfix")
					.first() != null) {
				sede = doc.getElementsByClass(
						"field field--name-field-centro field--type-entity-reference field--label-hidden field--entity-reference-target-type-node clearfix")
						.first().text();
				map.put("sede", sede);
			} else {
				sede = "Sede no disponible";
				map.put("sede", sede);
			}

			String email;

			if (doc.getElementsByClass(
					"field field--name-field-correo-electronico field--type-email field--label-above")
					.first() != null) {
				email = doc
						.getElementsByClass(
								"field field--name-field-correo-electronico field--type-email field--label-above")
						.first().getElementsByClass("field__item").get(0).text();
				map.put("email", email);
			} else {
				email = "Email no disponible";
				map.put("email", email);
			}
			String telefono;

			if (doc.getElementsByClass("field field--name-field-telefono field--type-telephone field--label-inline")
					.first() != null) {
				telefono = doc
						.getElementsByClass(
								"field field--name-field-telefono field--type-telephone field--label-inline")
						.first().getElementsByClass("field__item").get(0).text();
				map.put("telefono", telefono);
			} else {
				telefono = "Telefono no disponible";
				map.put("telefono", telefono);
			}

			String web;
			if (doc.getElementsByClass("field field--name-field-pagina-web- field--type-link field--label-above")
					.first() != null) {
				web = doc.getElementsByClass("field field--name-field-pagina-web- field--type-link field--label-above")
						.first().getElementsByClass("field__item").get(0).text();
				map.put("web", web);
			} else {
				web = "Web no disponible";
				map.put("web", web);
			}

			webClient.close();
		} catch (IOException | RuntimeException e) {

			if (e.getMessage().equals("java.net.SocketException: Connection reset")) {
				System.out.println("Reconectando");
				map = datosDepartamento(s);
			}
			if (e.getMessage().equals("Datos no encontrado")) {
				System.out.println("Buscando datos");
				map = datosDepartamento(s);
			}
		}

		return map;

	}

	
	
	public void añadirDepartamentos() {
		
		this.departamentoRepository.deleteAll();

		List<String> departamentos = listaDepartamentos();
		Integer i = 0;
		for (String s : departamentos) {

			Departamento departamento = new Departamento();
			Map<String, String> datos = datosDepartamento(s);

			departamento.setNombre(datos.get("nombre"));
			departamento.setSede(datos.get("sede"));
			departamento.setEmail(datos.get("email"));
			departamento.setTelefono(datos.get("telefono"));
			departamento.setWeb(datos.get("web"));

			departamentoRepository.save(departamento);
			System.out.println("ok");

			i++;

		}
		System.out.println("se han añadido" + i);
		this.departamentoRepository.limpiarTabla();
	}

	public List<Departamento> listaDepartamento() {

		return (List<Departamento>) this.departamentoRepository.findAll();
	}

	public Departamento getDepartamentoById(Long id) {
		return this.departamentoRepository.findById(id).orElse(null);
	}

	public void deleteAllDepartamento() {
		this.departamentoRepository.deleteAll();
	}
	

}
