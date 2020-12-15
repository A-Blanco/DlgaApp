package com.dlgaApp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.buf.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.internal.Normalizer;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Departamento;
import com.dlgaApp.repository.DepartamentoRepository;



@Service
public class DepartamentoServiceImpl {
	
	@Autowired
	private DepartamentoRepository departamentoRepository;

	private final String webDepartamentos = "https://www.us.es/centros/escuela-tecnica-superior-de-ingenieria-informatica";

	public List<String> listaDepartamentos() throws IOException {

		List<String> l = new ArrayList<String>();

		Document docDepartamentos = Jsoup.connect(webDepartamentos).userAgent("Mozilla/5.0").timeout(100000).get();

		docDepartamentos.getElementsByClass(
				"field field--name-field-departamentos-que-imparten field--type-entity-reference field--label-hidden field--entity-reference-target-type-node clearfix")
				.get(0).getElementsByTag("li").stream().forEach(x -> l.add(x.text()));

		return l;
	}

	public Map<String, String> datosDepartamento(String s) throws IOException {

		Map<String, String> map = new HashMap<String, String>();

		String url = "https://www.us.es/centros/departamentos/" + s;

		Document docDepartamento = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();

		String sede = docDepartamento.getElementsByClass(
				"field field--name-field-centro field--type-entity-reference field--label-hidden field--entity-reference-target-type-node clearfix")
				.get(0).text();

		String email = docDepartamento
				.getElementsByClass("field field--name-field-correo-electronico field--type-email field--label-above")
				.get(0).getElementsByClass("field__item").get(0).text();

		String telefono = docDepartamento
				.getElementsByClass("field field--name-field-telefono field--type-telephone field--label-inline").get(0)
				.getElementsByClass("field__item").get(0).text();

		String web = docDepartamento
				.getElementsByClass("field field--name-field-pagina-web- field--type-link field--label-above").get(0)
				.getElementsByClass("field__item").get(0).text();

		map.put("nombre", s);
		map.put("sede", sede);
		map.put("email", email);
		map.put("telefono", telefono);
		map.put("web", web);

		return map;

	}
	
	
	public void añadirDepartamentos() throws IOException {
		
		List<String> departamentos = listaDepartamentos();
		departamentos.stream().forEach(x-> Normalizer.normalize(x));
		departamentos.stream().forEach(x-> x.replace(" ", "-"));
		
		for (String s : departamentos){
			
			Departamento departamento = new Departamento();
			Map<String, String> datos = datosDepartamento(s);
			
			departamento.setNombre(datos.get("nombre"));
			departamento.setSede(datos.get("sede"));
			departamento.setEmail(datos.get("email"));
			departamento.setTelefono(datos.get("telefono"));
			departamento.setWeb(datos.get("web"));
			
			departamentoRepository.save(departamento);
			
			
		}
		
	}
	
	

}
