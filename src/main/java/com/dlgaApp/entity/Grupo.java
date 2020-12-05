package com.dlgaApp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "grupos")
public class Grupo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "grupo_id")
	private Integer id;
	
	@NotNull
	private Integer numerogrupo;
	
	@NotEmpty
	private String curso;
	
	@NotNull
	private Boolean esingles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "grupoDelegado")
	private List<Alumno> delegados ;
	
	@ManyToOne
    @JoinColumn(name = "titulacion_id")
	@NotNull
    private Titulacion titulacion;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumerogrupo() {
		return numerogrupo;
	}

	public void setNumerogrupo(Integer numerogrupo) {
		this.numerogrupo = numerogrupo;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Boolean getEsingles() {
		return esingles;
	}

	public void setEsingles(Boolean esingles) {
		this.esingles = esingles;
	}

	public List<Alumno> getDelegados() {
		return delegados;
	}

	public void setDelegados(List<Alumno> delegados) {
		this.delegados = delegados;
	}

	public Titulacion getTitulacion() {
		return titulacion;
	}

	public void setTitulacion(Titulacion titulacion) {
		this.titulacion = titulacion;
	}
	
	
	
}
