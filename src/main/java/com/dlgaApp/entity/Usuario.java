package com.dlgaApp.entity;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	@Column(unique = true)
	@NotEmpty(message = "Se debe introducir su uvus")
	private String username;
	
	@NotEmpty(message = "Debes escribir una contraseña")
	@Pattern(regexp = "^(?=.{8,}$)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).*$",
	message = "La contraseña debe tener al menos 8 caracteres,un dígito,una minúscula y una mayúscula.")
	private String password;
	
	
	private String password2;
	
	@NotNull(message = "Debes introducir su número de teléfono")
	@Pattern(regexp = "(\\+34|0034|34)?[ -]*(6|7|8)[ -]*([0-9][ -]*){8}",
	message = "Debe introducir un número de teléfono válido")
	@Column(unique = true)
	private String telefono;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private Roles rol;

	@NotNull
	@OneToOne
	@JoinColumn(name = "alumno_id")
	private Alumno alumno;
	
	private String motivoRechazo;

	 @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        List<GrantedAuthority> roles = new ArrayList<>();
	        roles.add(new SimpleGrantedAuthority(rol.toString()));
	        return roles;
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}


		public Alumno getAlumno() {
			return alumno;
		}

		public void setAlumno(Alumno alumno) {
			this.alumno = alumno;
		}

		public Roles getRol() {
			return rol;
		}

		public void setRol(Roles rol) {
			this.rol = rol;
		}

		
		public  String getTelefono() {
			return telefono;
		}

		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}

		public String getPassword2() {
			return password2;
		}

		public void setPassword2(String password2) {
			this.password2 = password2;
		}

		public String getMotivoRechazo() {
			return motivoRechazo;
		}

		public void setMotivoRechazo(String motivoRechazo) {
			this.motivoRechazo = motivoRechazo;
		}
	    
		
}
