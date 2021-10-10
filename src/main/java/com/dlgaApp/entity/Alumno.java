package com.dlgaApp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name = "alumnos")
public class Alumno {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty(message = "Debes introducir el nombre del alumno")
	@Length(max = 35,message = "La longitud no es válida")
	private String nombre;
	
	@NotEmpty(message = "Debes introducir los apellidos del alumno")
	@Length(max = 70,message = "La longitud de los apellidos no es válida")
	private String apellidos;
	
	@NotNull(message = "Se debe introducir la edad del alumno")
	@Min(value = 17,message = "Debe ser de al menos 17 años")
	@Max(value = 80,message = "Debe ser menor de 80 años")
	private Integer edad;

	@NotEmpty(message = "Debes introducir el email del alumno")
	@Email(message = "El email debe ser vádilo")
	@Column(unique = true)
	private String email;
	
	@OneToOne(mappedBy = "alumno",cascade = CascadeType.ALL)
	private Usuario usuario;
	
	@ManyToOne
    @JoinColumn(name = "grupoEsDelegado")
    private Grupo grupoDelegado;



	public Grupo getGrupoDelegado() {
		return grupoDelegado;
	}


	public void setGrupoDelegado(Grupo grupoDelegado) {
		this.grupoDelegado = grupoDelegado;
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
	
	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public Integer getEdad() {
		return edad;
	}


	public void setEdad(Integer edad) {
		this.edad = edad;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
}