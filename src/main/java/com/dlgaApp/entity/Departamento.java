package com.dlgaApp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "departamentos")
public class Departamento {

	@Id
	@Column(name = "departamento_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(unique = true)
	private String nombre;
	
	@NotEmpty
	private String sede;
	
	@NotEmpty
	@Email
	@Column(unique = true)
	private String email;
	
	@NotNull
	@Column(unique = true)
	@Pattern(regexp = "^[0-9]{6}$")
	private String telefono;
	
	@NotEmpty
	@URL
	private String web;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "departamento")
	@NotNull
	private List<Asignatura> asignaturas;

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "departamento")
	@NotNull
	private List<Profesor> profesores;
	
	
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

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public List<Asignatura> getAsignaturas() {
		return asignaturas;
	}

	public void setAsignaturas(List<Asignatura> asignaturas) {
		this.asignaturas = asignaturas;
	}
	
	
	
	
}
