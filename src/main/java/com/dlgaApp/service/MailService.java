package com.dlgaApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.dlgaApp.controller.GeneradorPdfController;
import com.dlgaApp.entity.Incidencia;
import com.dlgaApp.entity.Usuario;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Service
public class MailService {
	
	@Autowired
	private GeneradorPdfController pdfController;
	
	private final String publicKey = "891c437bd8d020e0e17a75468ab45bab";
	
	private final String privateKey = "c96a13fffde20bfc8e5fde9bec3fe7bf";

	public void envíoEmail() throws MailjetException, MailjetSocketTimeoutException {
		MailjetClient client;
		MailjetRequest request;
		MailjetResponse response;
		client = new MailjetClient(publicKey, privateKey, new ClientOptions("v3.1"));
        
		request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES, new JSONArray().put(new JSONObject()
				.put(Emailv31.Message.FROM,
						new JSONObject().put("Email", "alexblanper99@gmail.com").put("Name", "Alejandro"))
				.put(Emailv31.Message.TO,
						new JSONArray()
								.put(new JSONObject().put("Email", "agfwgfuywhibvcwhybp99@gmail.com").put("Name", "Alejandro")))
				.put(Emailv31.Message.SUBJECT, "EEEEIIII from Mailjet.")
				.put(Emailv31.Message.TEXTPART, "My first Mailjet email")
				.put(Emailv31.Message.HTMLPART,
						"<h3>Dear passenger 1, welcome to <a href='https://www.mailjet.com/'>Mailjet</a>!</h3><br />May the delivery force be with you!")
				.put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
		response = client.post(request);
		System.out.println(response.getStatus());
		System.out.println(response.getData());
		
	}
	
	public void emailAceptadoMiembro(Usuario usuario) throws MailjetException, MailjetSocketTimeoutException {
		MailjetClient client;
		MailjetRequest request;
		MailjetResponse response;
		client = new MailjetClient(publicKey, privateKey, new ClientOptions("v3.1"));
        
		request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES, new JSONArray().put(new JSONObject()
				.put(Emailv31.Message.FROM,
						new JSONObject().put("Email", "alexblanper99@gmail.com").put("Name", "Alejandro Blanco Pérez"))
				.put(Emailv31.Message.TO,
						new JSONArray()
								.put(new JSONObject().put("Email", usuario.getAlumno().getEmail()).put("Name", usuario.getAlumno().getNombre()
										+" "+ usuario.getAlumno().getApellidos())))
				.put(Emailv31.Message.SUBJECT, "Ya puedes acceder a la aplicación de DlgaApp!!")
				.put(Emailv31.Message.TEXTPART, "")
				.put(Emailv31.Message.HTMLPART,
						"<h2> Hola " + usuario.getAlumno().getNombre() + "</h2><br>" + "<h3> "
								+"Ya tienes acceso a la aplicación de la Delegación de Alumnos de la ETSII."
										+ " </h3><br> " +"<p>A partir de este momento ya eres un miembro oficial de la Delegación de Alumno de la ETSII"
												+ " y por lo tanto, tienes acceso a todas las funciones de nuestra aplicación.</p>"
												+ "<br><p>Recuerda que si tienes alguna pregunta en estos primeros días, no dudes en hacerla a cualquier compañero.")
				));
		response = client.post(request);
		System.out.println(response.getStatus());
		System.out.println(response.getData());
		
	}
	
	public void emailRechazoMiembro(Usuario usuario) throws MailjetException, MailjetSocketTimeoutException {
		MailjetClient client;
		MailjetRequest request;
		MailjetResponse response;
		client = new MailjetClient(publicKey, privateKey, new ClientOptions("v3.1"));
		
		String motivoRechazo = "";
		if(usuario.getMotivoRechazo()!=null && usuario.getMotivoRechazo()!="") {
			motivoRechazo = "<h3>Motivo del rechazo: </h3> <p>"+ usuario.getMotivoRechazo()+"</p><br>";
		}
        
		request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES, new JSONArray().put(new JSONObject()
				.put(Emailv31.Message.FROM,
						new JSONObject().put("Email", "alexblanper99@gmail.com").put("Name", "Alejandro Blanco Pérez"))
				.put(Emailv31.Message.TO,
						new JSONArray()
								.put(new JSONObject().put("Email", usuario.getAlumno().getEmail()).put("Name", usuario.getAlumno().getNombre()
										+" "+ usuario.getAlumno().getApellidos())))
				.put(Emailv31.Message.SUBJECT, "Rechazo de acceso a DlgaApp")
				.put(Emailv31.Message.TEXTPART, "")
				.put(Emailv31.Message.HTMLPART,
						"<h2> Hola " + usuario.getAlumno().getNombre() + "</h2><br>" + "<h3> "
								+"Se ha rechazado el acceso a la aplicación de la Delegación de Alumnos de la ETSII."
										+ " </h3><br> " +"<p>Debido a motivos internos de la organización, se le ha rechazado el acceso a la aplicación de la Delegación de alumnos de la ETSII"
												+ " y por lo tanto, no tienes acceso a las funciones de nuestra aplicación.</p><br>"
												+ motivoRechazo							
												+ "<p>Si tienes alguna duda, o quieres obtener más información sobre el rechazo, póngase en contacto con la Delegación de Alumnos")
				));
		response = client.post(request);
		System.out.println(response.getStatus());
		System.out.println(response.getData());
		
	}
	
	public void emailInfoIncidencia(Incidencia incidencia) throws MailjetException, MailjetSocketTimeoutException, JSONException, IOException {
		MailjetClient client;
		MailjetRequest request;
		MailjetResponse response;
		client = new MailjetClient(publicKey, privateKey, new ClientOptions("v3.1"));

		request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES, new JSONArray().put(new JSONObject()
				.put(Emailv31.Message.FROM,
						new JSONObject().put("Email", "alexblanper99@gmail.com").put("Name", "Alejandro Blanco Pérez"))
				.put(Emailv31.Message.TO,
						new JSONArray()
								.put(new JSONObject().put("Email", incidencia.getAlumno().getEmail()).put("Name", incidencia.getAlumno().getNombre()
										+" "+ incidencia.getAlumno().getApellidos())))
				.put(Emailv31.Message.SUBJECT, "Actualización de su Incidencia - " +incidencia.getId().toString())
				.put(Emailv31.Message.TEXTPART, "")
				.put(Emailv31.Message.HTMLPART,
						"<h2> Hola " + incidencia.getAlumno().getNombre() + "</h2><br>" + "<h3> "
								+"Se ha actualizado la información de su incidencia con título: " + incidencia.getTitulo()
										+ " </h3><br> " +"<p>A continuación se adjunta el archivo pdf de su incidencia con los últimos cambios.</p><br>"
																	
												+ "<p>Si tienes alguna duda, o no estás de acuerdo con la información contrastada, póngase en contacto con la Delegación de Alumnos.")
				.put(Emailv31.Message.INLINEDATTACHMENTS, new JSONArray()
				.put(new JSONObject()
                        .put("ContentType", "application/pdf")
                        .put("Filename", "Incidencia"+incidencia.getId()+".pdf")
                        .put("ContentID", "id1")
                        .put("Base64Content",this.pdfController.pdfBase64(incidencia.getId())))
				)));
		response = client.post(request);
		System.out.println(response.getStatus());
		System.out.println(response.getData());
		
	}
	
	public void emailFinalizacionIncidencia(Incidencia incidencia) throws MailjetException, MailjetSocketTimeoutException, JSONException, IOException {
		MailjetClient client;
		MailjetRequest request;
		MailjetResponse response;
		client = new MailjetClient(publicKey, privateKey, new ClientOptions("v3.1"));

		request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES, new JSONArray().put(new JSONObject()
				.put(Emailv31.Message.FROM,
						new JSONObject().put("Email", "alexblanper99@gmail.com").put("Name", "Alejandro Blanco Pérez"))
				.put(Emailv31.Message.TO,
						new JSONArray()
								.put(new JSONObject().put("Email", incidencia.getAlumno().getEmail()).put("Name", incidencia.getAlumno().getNombre()
										+" "+ incidencia.getAlumno().getApellidos())))
				.put(Emailv31.Message.SUBJECT, "Acuerdo alcanzado en su Incidencia - " +incidencia.getId().toString())
				.put(Emailv31.Message.TEXTPART, "")
				.put(Emailv31.Message.HTMLPART,
						"<h2> Hola " + incidencia.getAlumno().getNombre() + "</h2><br>" + "<h3> "
								+"Se ha finalizado su incidencia con título: " + incidencia.getTitulo()
										+ " </h3><br> " +"<p>A continuación se adjunta el archivo pdf de su incidencia con el acuerdo alcanzado.</p><br>"
																	
												+ "<p>Si tienes alguna duda, o no estás de acuerdo con la información contrastada, póngase en contacto con la Delegación de Alumnos.")
				.put(Emailv31.Message.INLINEDATTACHMENTS, new JSONArray()
				.put(new JSONObject()
                        .put("ContentType", "application/pdf")
                        .put("Filename", "Incidencia"+incidencia.getId()+".pdf")
                        .put("ContentID", "id1")
                        .put("Base64Content",this.pdfController.pdfBase64(incidencia.getId())))
				)));
		response = client.post(request);
		System.out.println(response.getStatus());
		System.out.println(response.getData());
		
	}
}
