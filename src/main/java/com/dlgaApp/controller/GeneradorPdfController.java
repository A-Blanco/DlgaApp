package com.dlgaApp.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlgaApp.entity.EstadosIncidencia;
import com.dlgaApp.entity.Incidencia;
import com.dlgaApp.service.AsignaturaServiceImpl;
import com.dlgaApp.service.DepartamentoServiceImpl;
import com.dlgaApp.service.IncidenciaServiceImpl;
import com.dlgaApp.service.ProfesorService;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.ListRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;

import net.bytebuddy.asm.Advice.This;

@Controller
public class GeneradorPdfController {

	@Autowired
	private DepartamentoServiceImpl departamentoService;

	@Autowired
	private AsignaturaServiceImpl asignaturaService;

	@Autowired
	private FiltroMantenimiento filtroMantenimiento;

	@Autowired
	private ProfesorService profesorService;

	@Autowired
	private IncidenciaServiceImpl incidenciaService;

	@GetMapping(value = "/pdf/{idIncidencia}")
	public void generaIncidenciaPdf(@PathVariable("idIncidencia") final long incidenciaId,HttpServletResponse response, String file,
			String name, String title, String place, String date) throws IOException {

		
		Incidencia incidencia = this.incidenciaService.findIncidenciaById(incidenciaId);

		try {

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			PdfWriter pdfWriter = new PdfWriter(out);
			PdfDocument pdfDoc = new PdfDocument(pdfWriter);
			Document doc = new Document(pdfDoc, PageSize.A4);
			

			doc.setMargins(100, 36, 75, 36);
				
			PdfFont font =  PdfFontFactory.createFont("static/fuentes/Nunito-Regular.ttf");

			Paragraph tituloIncidencia = new Paragraph(incidencia.getId()+" - "+incidencia.getTitulo()).setFont(font).setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER);
			

			doc.add(tituloIncidencia);
			
			ImageData imageData = ImageDataFactory.create("src/main/resources/static/img/LogoUS-ETSIIhoriz.png");
		    Image imagenETSII = new Image(imageData);
		    
		    ImageData imageDataLogo = ImageDataFactory.create("src/main/resources/static/img/Simbolo.png");
		    Image imagenLogo = new Image(imageDataLogo);
		    
		    ImageData imageDataCalendario = ImageDataFactory.create("src/main/resources/static/img/calendario.png");
		    Image imageCalendario = new Image(imageDataCalendario);
		    imageCalendario.setWidth(20);
		    
		    ImageData imageDataEstado = ImageDataFactory.create("src/main/resources/static/img/estado.png");
		    Image imageEstado = new Image(imageDataEstado);
		    imageEstado.setWidth(20);
		   
		    ImageData imageDataAlum = ImageDataFactory.create("src/main/resources/static/img/iconEstudiante.png");
		    Image imageAlum = new Image(imageDataAlum);
		    imageAlum.setWidth(20);
		    
		    ImageData imageDataProf = ImageDataFactory.create("src/main/resources/static/img/iconProfesor.png");
		    Image imageProf = new Image(imageDataProf);
		    imageProf.setWidth(20);
		    
		    ImageData imageDataAsig = ImageDataFactory.create("src/main/resources/static/img/asig.png");
		    Image imageAsig = new Image(imageDataAsig);
		    imageAsig.setWidth(20);
		    

		    
		    Rectangle rFechaIcono = new Rectangle(40, 640, 20, 20);
		    Rectangle rFecha = new Rectangle(65, 635, 210, 30);
		    
		    Rectangle rEstadoIcono = new Rectangle(345, 640, 20, 20);
		    Rectangle rEstado = new Rectangle(370, 635, 210, 30);
		    
		    Rectangle rAlumnoIcono = new Rectangle(40, 600, 20, 20);
		    Rectangle rTituloAlum = new Rectangle(65, 595, 210, 30);
		    Rectangle rDatosAlum = new Rectangle(40, 530, 230, 60);
		    
		    Rectangle rProfesorIcono = new Rectangle(345, 600, 20, 20);
		    Rectangle rTituloProf = new Rectangle(370, 595, 210, 30);
		    Rectangle rDatosProf = new Rectangle(345, 395, 230, 200);
		    
		    Rectangle rAsignaturaIcono = new Rectangle(40, 500, 20, 20);
		    Rectangle rTituloAsig = new Rectangle(65, 495, 210, 30);
		    Rectangle rDatosAsig = new Rectangle(40, 300, 230, 200);
		    
		    AreaBreak nextArea = new AreaBreak(AreaBreakType.NEXT_AREA);
		    AreaBreak nextPag = new AreaBreak(AreaBreakType.NEXT_PAGE);
		    
		    
		    
		    Rectangle[] columns = {rFechaIcono,rFecha, rEstadoIcono, rEstado, rAlumnoIcono,rTituloAlum,
		    		rProfesorIcono,rTituloProf,rDatosAlum, rDatosProf, rAsignaturaIcono,rTituloAsig,rDatosAsig};
		    
		    ColumnDocumentRenderer render = new ColumnDocumentRenderer(doc, columns);
		    
		    doc.setRenderer(render);
 
		    
		    doc.add(imageCalendario);
		    
		    doc.add(nextArea);
		    
		    Text tituloFecha = new Text("Fecha: ").setBold().setFontSize(13);
		    Text fecha =  new Text(String.valueOf(incidencia.getFechaCreacion())).setFont(font).setFontSize(13);
		    Paragraph comb=new Paragraph();  
		    comb.add(tituloFecha);
		    comb.add(fecha);
		    doc.add(comb);
		    
		   
		    doc.add(nextArea);
		    doc.add(imageEstado);
		    
		    doc.add(nextArea);
		    
		    Text tituloEstado = new Text("Estado: ").setBold().setFontSize(13);
		    Text estado =  new Text(String.valueOf(incidencia.getEstado().toString())).setFont(font).setFontSize(13);
		    Paragraph comb2 =new Paragraph();  
		    comb2.add(tituloEstado);
		    comb2.add(estado);
		    doc.add(comb2);
		    
		    doc.add(nextArea);
		    doc.add(imageAlum);
		    
		    doc.add(nextArea);
		    doc.add(new Paragraph("Datos Alumno").setBold().setFont(font).setFontSize(13));
		    
		    doc.add(nextArea);
		    doc.add(imageProf);
		    
		    doc.add(nextArea);
		    Paragraph paragraph = new Paragraph("Datos Profesor").setBold().setFontSize(13);
		    doc.add(paragraph);
		  
		    
		    doc.add(nextArea);
		    
		    Text tituloNombreAlum = new Text("\u2022  Nombre: ").setBold().setFontSize(13);
		    Text nomAlum =  new Text(incidencia.getAlumno().getNombre() + incidencia.getAlumno().getApellidos()).setFont(font);
		    Paragraph comb3 =new Paragraph();  
		    nomAlum.setFontSize(getMaxFontSize(font, String.valueOf(nomAlum), 220, 13));
		    comb3.add(tituloNombreAlum);
		    comb3.add(nomAlum);
		    doc.add(comb3);
		    
		    Text tituloEmailAlum = new Text("\u2022  Email: ").setBold().setFontSize(13);
		    Text emailAlum =  new Text(incidencia.getAlumno().getEmail()).setFont(font).setFontSize(getMaxFontSize(font, String.valueOf(tituloNombreAlum) + String.valueOf(nomAlum), 220, 12));
		    Paragraph comb4 =new Paragraph();  
		    emailAlum.setFontSize(getMaxFontSize(font, String.valueOf(emailAlum), 220, 13));
		    comb4.add(tituloEmailAlum);
		    comb4.add(emailAlum);
		    
		    doc.add(comb4);
		    
		    doc.add(nextArea);
		    
		    Text tituloNombreProf = new Text("\u2022  Nombre: ").setBold().setFontSize(13);
		    Text nomProf =  new Text(incidencia.getProfesor().getNombre()).setFont(font);
		    Paragraph comb5 =new Paragraph();  
		    nomProf.setFontSize(getMaxFontSize(font, String.valueOf(nomProf), 220, 13));
		    comb5.add(tituloNombreProf);
		    comb5.add(nomProf);
		    doc.add(comb5);
		    
		    Text tituloEmailProf = new Text("\u2022  Email: ").setBold().setFontSize(13);
		    Text emailProf =  new Text(incidencia.getProfesor().getEmail()).setFont(font);
		    Paragraph comb6 =new Paragraph();  
		    emailProf.setFontSize(getMaxFontSize(font, String.valueOf(emailProf), 220, 13));
		    comb6.add(tituloEmailProf);
		    comb6.add(emailProf);
		    doc.add(comb6);
		    
		    Text tituloTelfProf = new Text("\u2022  Teléfono: ").setBold().setFontSize(13);
		    Text telProf =  new Text(incidencia.getProfesor().getNombre()).setFont(font);
		    Paragraph comb7 =new Paragraph();  
		    telProf.setFontSize(getMaxFontSize(font, String.valueOf(telProf), 220, 13));
		    comb7.add(tituloTelfProf);
		    comb7.add(telProf);
		    doc.add(comb7);
		    
		    Text tituloDepProf = new Text("\u2022  Departamento: ").setBold().setFontSize(13);
		    Text depProf =  new Text(incidencia.getProfesor().getDepartamento().getNombre()).setFont(font);
		    Paragraph comb8 =new Paragraph();  
		    depProf.setFontSize(getMaxFontSize(font, String.valueOf(depProf), 220, 13));
		    comb8.add(tituloDepProf);
		    comb8.add(depProf);
		    doc.add(comb8);
		    

		    doc.add(nextArea);
		    doc.add(imageAsig);
		    
		    doc.add(nextArea);
		    doc.add(new Paragraph("Datos Asignatura").setBold().setFontSize(13));
		    
		    doc.add(nextArea);
		    
		    
		    Text tituloNombreAsig = new Text("\u2022  Nombre: ").setBold().setFontSize(13);
		    Text nomAsig =  new Text(incidencia.getAsignatura().getNombre()).setFont(font);
		    Paragraph comb9 =new Paragraph();  
		    nomAsig.setFontSize(getMaxFontSize(font, String.valueOf(nomAsig), 220, 13));
		    comb9.add(tituloNombreAsig);
		    comb9.add(nomAsig);
		    doc.add(comb9);
		    
		    Text tituloTitulacionAsig = new Text("\u2022  Titulacion: ").setBold().setFontSize(13);
		    Text titulacionAsig =  new Text(incidencia.getAsignatura().getTitulacion().getNombre()).setFont(font);
		    Paragraph comb10 =new Paragraph();  
		    titulacionAsig.setFontSize(getMaxFontSize(font, String.valueOf(titulacionAsig), 220, 13));
		    comb10.add(tituloTitulacionAsig);
		    comb10.add(titulacionAsig);
		    doc.add(comb10);
		    
		    Text tituloDepAsig = new Text("\u2022  Departamento: ").setBold().setFontSize(13);
		    Text departamentoAsig =  new Text(incidencia.getAsignatura().getDepartamento().getNombre()).setFont(font);
		    Paragraph comb11 =new Paragraph();  
		    departamentoAsig.setFontSize(getMaxFontSize(font, String.valueOf(departamentoAsig), 220, 13));
		    comb11.add(tituloDepAsig);
		    comb11.add(departamentoAsig);
		    doc.add(comb11);
		   

		    DocumentRenderer drender = new DocumentRenderer(doc,false);
		    doc.setRenderer(drender);
		    doc.add(nextPag);
		   
		    
//		    
//		    ImageData imageDescripcion = ImageDataFactory.create("src/main/resources/static/img/descripcion.png");
//		    Image imageDesc= new Image(imageDescripcion);
//		    imageDesc.setHorizontalAlignment(HorizontalAlignment.CENTER);
//		    imageDesc.setMarginBottom(20).setMarginTop(20);
//		    
//			
//
//			doc.add(imageDesc);
		    
		    Paragraph tituloDescripcion = new Paragraph("Descripción de la Incidencia")
		    		.setFont(font).setFontSize(16).setTextAlignment(TextAlignment.CENTER).setBold().setUnderline()
		    		.setMarginBottom(20);
		    
		    doc.add(tituloDescripcion);
			
			doc.add(new Paragraph(incidencia.getDescripcion()
					).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED));
			
			
//			ImageData imageInformacion = ImageDataFactory.create("src/main/resources/static/img/TituloInformacion.png");
//			    Image imageInfo= new Image(imageInformacion);
//			    imageInfo.setHorizontalAlignment(HorizontalAlignment.CENTER);
//			    imageInfo.setMarginBottom(20).setMarginTop(20);
//				
//
//				doc.add(imageInfo);
			
			if(incidencia.getEstado() == EstadosIncidencia.BusquedaAcuerdo || incidencia.getEstado() == EstadosIncidencia.Finalizada) {
				
			 Paragraph tituloInformacion = new Paragraph("Información Contrastada")
			    		.setFont(font).setFontSize(16).setTextAlignment(TextAlignment.CENTER).setBold().setUnderline()
			    		.setMarginBottom(20).setMarginTop(20);
			    
			    doc.add(tituloInformacion);
				
				doc.add(new Paragraph(incidencia.getInformacionContrastada()
						).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED));}
				
				
//				ImageData imageAcuerdo = ImageDataFactory.create("src/main/resources/static/img/tituloAcuerdo.png");
//			    Image imageAc= new Image(imageAcuerdo);
//			    imageAc.setHorizontalAlignment(HorizontalAlignment.CENTER);
//			    imageAc.setMarginBottom(20).setMarginTop(20);
//				
//
//				doc.add(imageAc);
			
			if( incidencia.getEstado() == EstadosIncidencia.Finalizada) {
				
				Paragraph tituloAcuerdo = new Paragraph("Acuerdo Alcanzado")
			    		.setFont(font).setFontSize(16).setTextAlignment(TextAlignment.CENTER).setBold().setUnderline()
			    		.setMarginBottom(20).setMarginTop(20);
			    
			    doc.add(tituloAcuerdo);
				
				doc.add(new Paragraph(incidencia.getAcuerdo()
						).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED));
				
			}
		    

			// Realizamos un bucle con el número de páginas
			for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
				// Introducimos la cabecera de cada página del archivo con los dos logos
				imagenETSII.setFixedPosition(i, 50, 760, 200);
				imagenLogo.setFixedPosition(i, 480, 760, 50);
				doc.add(imagenLogo);
				doc.add(imagenETSII);
				
				 doc.showTextAligned(new Paragraph(String.format("Página %s", i)),
		                    559, 30, i, TextAlignment.RIGHT, VerticalAlignment.TOP, 0);
			}

			doc.close();

			byte[] pdfReport = out.toByteArray();

			String mimeType = "application/pdf";
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "Incidencia-"+incidencia.getId()+".pdf"));

			response.setContentLength(pdfReport.length);

			ByteArrayInputStream inStream = new ByteArrayInputStream(pdfReport);

			FileCopyUtils.copy(inStream, response.getOutputStream());

		} catch (IOException  e) {
			System.out.println(e.getMessage());
		}

	}

	private static float getMaxFontSize(PdfFont bf, String text, int width, float size) {

		while (bf.getWidth(text, size) > width) {
			size = size - 1;
		}
		return size;
	}

}