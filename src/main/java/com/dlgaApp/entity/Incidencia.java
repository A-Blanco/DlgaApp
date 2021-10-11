package com.dlgaApp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "incidencias")
public class Incidencia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "incidencia_id")
	private Long id;
	
	@Column(name = "fechaCreacion", updatable = false, nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
	
	@NotEmpty
	private String descripcion;
	
	private String informacionContrastada;
	
	private String acuerdo;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "alumnoImplicado")
	private Alumno alumno;
	
	
	@ManyToOne
	@JoinColumn(name = "profesorImplicado")
	private Profesor profesor;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "miembroImplicado")
	private Usuario miembro;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "asignaturaImplicada")
	private Asignatura asignatura;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private EstadosIncidencia estado;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getInformacionContrastada() {
		return informacionContrastada;
	}

	public void setInformacionContrastada(String informacionContrastada) {
		this.informacionContrastada = informacionContrastada;
	}

	public String getAcuerdo() {
		return acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public Usuario getMiembro() {
		return miembro;
	}

	public void setMiembro(Usuario miembro) {
		this.miembro = miembro;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	public EstadosIncidencia getEstado() {
		return estado;
	}

	public void setEstado(EstadosIncidencia estado) {
		this.estado = estado;
	}
	

}
