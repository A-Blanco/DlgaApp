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

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Entity
@Table(name = "grupos")
public class Grupo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "grupo_id")
	private Long id;
	
	@NotNull
	private Integer numerogrupo;
	
	@NotNull
	private Integer curso;
	
	@NotNull
	private Boolean esingles;
	
	@OneToMany(mappedBy = "grupoDelegado")
	private List<Alumno> delegados ;
	
	@ManyToOne
    @JoinColumn(name = "titulacion_id")
	@NotNull
    private Titulacion titulacion;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumerogrupo() {
		return numerogrupo;
	}

	public void setNumerogrupo(Integer numerogrupo) {
		this.numerogrupo = numerogrupo;
	}

	public Integer getCurso() {
		return curso;
	}

	public void setCurso(Integer curso) {
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
