package com.dlgaApp.service;

import java.io.IOException;
import java.net.MalformedURLException;
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

import com.dlgaApp.entity.Asignatura;
import com.dlgaApp.repository.AsignaturaRepository;
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

	public List<String> elancesAsignatura() {

		WebClient webClient = new WebClient();
		webClient.getOptions().setSSLClientProtocols(new String[] { "TLSv1.3", "TLSv1.2", "TLSv1.1" });
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);

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

			Document docGrados;
			try {
				docGrados = Jsoup.parse(webClient.getPage(enlace).getWebResponse().getContentAsString());
				Elements elementos = docGrados.getElementsByClass(
						"field field--name-field-tabla-de-asignaturas field--type-viewsreference field--label-hidden field__item")
						.first().getElementsByTag("a");

				elementos.stream().forEach(x -> listaEnlaces.add(x.attr("href")));
				webClient.close();
				// 282
				System.out.println(listaEnlaces.size());
			} catch (FailingHttpStatusCodeException e) {

			} catch (MalformedURLException e) {

			} catch (IOException e) {
				System.out.println(e.getMessage());

			}

		}

		
		return listaEnlaces;
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
				webClient.close();
				System.out.println("encontrado");
			}
		} catch (FailingHttpStatusCodeException | IOException e1) {

			System.out.println(e1.getCause());
		}

		

		return map;

	}

	public void añadirAsignaturas() {

		List<String> l = new ArrayList<String>();
		l = elancesAsignatura();

		for (String enlace : l) {

			Asignatura asignatura = new Asignatura();
			Map<String, String> map = new HashMap<String, String>();

			map = datosAsignatura(enlace);

			if (!map.isEmpty()) {
				asignatura.setNombre(map.get("Asignatura"));
				asignatura.setCaracter(map.get("Carácter"));
				asignatura.setDuracion(map.get("Duración"));
				asignatura.setCreditos(map.get("Créditos Totales"));

				this.asignaturaRepository.save(asignatura);
				System.out.println("ok");
			}

		}
		System.out.println("Finalizado");

	}

}
