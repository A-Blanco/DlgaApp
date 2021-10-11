package com.dlgaApp.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

@Entity
@Table(name = "asignaturas")
public class Asignatura {

	@Id
	@Column(name = "asignatura_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nombre;

	private String caracter;

	private String duracion;

	private String creditos;
	
	private String ano;

	@ManyToOne
	@JoinColumn(name = "titulacion_id")
	private Titulacion titulacion;

	@ManyToOne()
	@JoinColumn(name = "departamento_id")
	private Departamento departamento;

	@JoinTable(name = "rel_asig_prof", joinColumns = @JoinColumn(name = "FK_ASIGANTURA"), inverseJoinColumns = @JoinColumn(name = "FK_PROFESOR"))
	@ManyToMany()
	private List<Profesor> profesores;
	
	@OneToMany(mappedBy = "asignatura")
	private List<Incidencia> incidencias ;
	
	@PreRemove
    private void removeProfesoresFromAsignatura() {
        for (Profesor p : profesores) {
            p.getAsignaturas().remove(this);
        }
    }
	

	public void addProfesor(Profesor profesor) {
		if (this.profesores == null) {
			this.profesores = new ArrayList<>();
		}

		this.profesores.add(profesor);
	}
	

	public List<Profesor> getProfesores() {
		return profesores;
	}


	public void setProfesores(List<Profesor> profesores) {
		this.profesores = profesores;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCaracter() {
		return caracter;
	}

	public void setCaracter(String caracter) {
		this.caracter = caracter;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public String getCreditos() {
		return creditos;
	}

	public void setCreditos(String creditos) {
		this.creditos = creditos;
	}

	public Titulacion getTitulacion() {
		return titulacion;
	}

	public void setTitulacion(Titulacion titulacion) {
		this.titulacion = titulacion;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}


	public String getAno() {
		return ano;
	}


	public void setAno(String ano) {
		this.ano = ano;
	}


	public List<Incidencia> getIncidencias() {
		return incidencias;
	}


	public void setIncidencias(List<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}
	
	

}
