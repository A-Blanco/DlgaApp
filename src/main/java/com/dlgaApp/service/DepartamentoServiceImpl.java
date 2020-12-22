package com.dlgaApp.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import org.jboss.jandex.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Departamento;
import com.dlgaApp.repository.DepartamentoRepository;
import com.jaunt.ResponseException;

@Service
public class DepartamentoServiceImpl {

	@Autowired
	private DepartamentoRepository departamentoRepository;

	private final String webDepartamentos = "https://www.us.es/centros/escuela-tecnica-superior-de-ingenieria-informatica";

	public List<String> listaDepartamentos()
			throws IOException, ResponseException, NoSuchAlgorithmException, KeyManagementException {

		List<String> l = new ArrayList<String>();

		Document doc = Jsoup.connect(webDepartamentos).userAgent(
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
				.timeout(0).ignoreHttpErrors(true).get();

		doc.getElementsByClass(
				"field field--name-field-departamentos-que-imparten field--type-entity-reference field--label-hidden field--entity-reference-target-type-node clearfix")
				.get(0).getElementsByTag("li").stream()
				.forEach(x -> l.add(x.getElementsByTag("a").get(0).attr("href")));

		System.out.println(l);

		return l;
	}

	public Map<String, String> datosDepartamento(String s)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {

		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(null, null, null);

		Map<String, String> map = new HashMap<String, String>();

		String url = "https://www.us.es" + s;

		String sa = Jsoup.connect(url).userAgent(
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
				.timeout(0).ignoreHttpErrors(true).get().toString();

		Document doc = Jsoup.parse(sa);

		String nombre = null;
		try {
			nombre = doc.getElementsByClass("text-center noticia").text();
		} catch (Exception e) {

		}

		String sede = null;
		try {
			sede = doc.getElementsByClass(
					"field field--name-field-centro field--type-entity-reference field--label-hidden field--entity-reference-target-type-node clearfix")
					.get(0).text();
		} catch (Exception e) {

		}
		String email = null;
		try {
			email = doc
					.getElementsByClass(
							"field field--name-field-correo-electronico field--type-email field--label-above")
					.get(0).getElementsByClass("field__item").get(0).text();
		} catch (Exception e) {

		}
		String telefono = null;
		try {
			telefono = doc
					.getElementsByClass("field field--name-field-telefono field--type-telephone field--label-inline")
					.get(0).getElementsByClass("field__item").get(0).text();
		} catch (Exception e) {

		}

		String web = null;
		try {
			web = doc.getElementsByClass("field field--name-field-pagina-web- field--type-link field--label-above")
					.first().getElementsByClass("field__item").get(0).text();
		} catch (Exception e) {
		}

		if (nombre == null) {
			nombre = "Nombre no disponible";
		}

		if (sede == null) {
			sede = "Sede no disponible";
		}

		if (email == null) {
			email = "Email no disponible";
		}

		if (telefono == null) {
			telefono = "Telefono no disponible";
		}

		if (web == null) {
			web = "Web no disponible";
		}

		map.put("nombre", nombre);
		map.put("sede", sede);
		map.put("email", email);
		map.put("telefono", telefono);
		map.put("web", web);

		return map;

	}

	public void añadirDepartamentos()
			throws IOException, ResponseException, KeyManagementException, NoSuchAlgorithmException {

		List<String> departamentos = listaDepartamentos();

		for (String s : departamentos) {

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
