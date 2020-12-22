package com.dlgaApp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class asignaturaServiceImpl {

	private final String enlaceDGInforMates = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/doble-grado-en-ingenieria-informatica-tecnologias";
	private final String enlaceGIngeSalud = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/grado-en-ingenieria-de-la-salud-por-la-universidad-de-malaga";
	private final String enlaceGIngeComp = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/grado-en-ingenieria-informatica-ingenieria-de-computadores";
	private final String enlaceGIngeSoftw = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/grado-en-ingenieria-informatica-ingenieria-del-software";
	private final String enlaceGIngeTecnol = "https://www.us.es/estudiar/que-estudiar/oferta-de-grados/grado-en-ingenieria-informatica-tecnologias-informaticas";

	private final String enlaceMIngeBioySalud = "https://www.us.es/estudiar/que-estudiar/oferta-de-masteres/master-universitario-en-ingenieria-biomedica-y-salud";
	private final String enlaceMInfor = "https://www.us.es/estudiar/que-estudiar/oferta-de-masteres/master-universitario-en-ingenieria-informatica";
	private final String enlaceMInteligencia = "https://www.us.es/estudiar/que-estudiar/oferta-de-masteres/master-universitario-en-logica-computacion-e-inteligencia";

	public List<String> elancesAsignatura() throws IOException {

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
		try {

			for (String enlace : l) {

				Document docGrados = Jsoup.connect(enlace).userAgent(
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
						.timeout(0).ignoreHttpErrors(true).get();

				Elements elementos = docGrados.getElementsByClass(
						"field field--name-field-tabla-de-asignaturas field--type-viewsreference field--label-hidden field__item")
						.get(0).getElementsByTag("a");

				elementos.stream().forEach(x -> listaEnlaces.add(x.attr("href")));
				System.out.println(elementos.stream().count());

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		System.out.println(listaEnlaces.size());
		return listaEnlaces;
	}

}
