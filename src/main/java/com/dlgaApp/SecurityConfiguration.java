package com.dlgaApp;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.dlgaApp.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//administradorController
		http.authorizeRequests().antMatchers("/administrador").hasRole("ADMIN")
		.antMatchers("/populateBd").hasRole("ADMIN")
		.antMatchers("/mantenimiento").permitAll()
		.antMatchers("/gestionBd").hasRole("ADMIN");
		
		//alumnoController
		http.authorizeRequests().antMatchers("/crearAlumno").permitAll()
		.antMatchers("/listAlumnos").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/agregarDelegado/{alumnoId}").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/detallesAlumno/{alumnoId}").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/alumnoDelete/{alumnoId}").hasRole("ADMIN")
		.antMatchers("/alumnoDeleteSeguridad/{alumnoId}").hasRole("ADMIN")
		.antMatchers("/alumnoUpdate").hasRole("ADMIN");
		
		//asignaturaController
		 http.authorizeRequests()
		.antMatchers("/asignaturas").hasRole("ADMIN")
		.antMatchers("/asignaturaList").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/detallesAsignatura/{asignaturaId}").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/asignaturaDelete/{asignaturaId}").hasRole("ADMIN")
		.antMatchers("/asignaturaDeleteSeguridad/{asignaturaId}").hasRole("ADMIN")
		.antMatchers("/asignaturaUpdate").hasRole("ADMIN")
		.antMatchers("/asignaturaCreate").hasRole("ADMIN");
		 
		 //departamentoController
		 http.authorizeRequests()
		.antMatchers("/departamentos").hasRole("ADMIN")
		.antMatchers("/listDepartamentos").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/detallesDepartamento/{departamentoId}").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/departamentoDelete/{departamentoId}").hasRole("ADMIN")
		.antMatchers("/departamentoDeleteSeguridad/{departamentoId}").hasRole("ADMIN")
		.antMatchers("/departamentoUpdate").hasRole("ADMIN")
		.antMatchers("/departamentoCreate").hasRole("ADMIN");
		 
		 //grupoController
		 http.authorizeRequests()
		 .antMatchers("/crearGrupo").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("/grupoList").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("añadirDelegado/{grupoId}").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("addDelegadoAgain").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("delegados/{grupoId}").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("modificarDelegados/{grupoId}").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("eliminarDelegado/{grupoId}/{alumnoId}").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("eliminarGrupo/{grupoId}").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("/eliminarGrupoSeguridad/{grupoId}").hasAnyRole("ADMIN","MIEMBRO");
		 
		 //profesorController
		 http.authorizeRequests()
		 .antMatchers("/profesor").hasRole("ADMIN")
		 .antMatchers("/profesorList").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("/detallesProfesor/{profesorId}").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("/profesorDelete/{profesorId}").hasRole("ADMIN")
		 .antMatchers("/profesorDeleteSeguridad/{profesorId}").hasRole("ADMIN")
		 .antMatchers("/profesorUpdate").hasRole("ADMIN")
		 .antMatchers("/profesorCreate").hasRole("ADMIN");
		 
		 //titulacionController
		 http.authorizeRequests()
		 .antMatchers("/titulacionList").hasAnyRole("ADMIN","MIEMBRO")
		 .antMatchers("/titulacionUpdate").hasRole("ADMIN")
		 .antMatchers("/titulacionCreate").hasRole("ADMIN")
		 .antMatchers("/titulacionDelete/{titulacionId}").hasRole("ADMIN")
		 .antMatchers("/titulacionDeleteSeguridad/{titulacionId}").hasRole("ADMIN");
		 
		 
		 //usuarioController
		 http.authorizeRequests()
		.antMatchers("/crearUsuario").permitAll()
		.antMatchers("/crearUsuarioRegistrado").permitAll()
		.antMatchers("/listaUsuario").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/eliminarUsuario").hasRole("ADMIN")
		.antMatchers("/usuario/{usuarioId}").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/usuariosNoAceptados").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/aceptarUsuario/{usuarioId}").hasAnyRole("ADMIN","MIEMBRO")
		.antMatchers("/denegarUsuario").hasAnyRole("ADMIN","MIEMBRO");
		
		 //incidenciaController
		 http.authorizeRequests()
			.antMatchers("/incidenciaCreate").hasAnyRole("ADMIN","MIEMBRO")
			.antMatchers("/incidenciaList").hasAnyRole("ADMIN","MIEMBRO")
			.antMatchers("/incidenciaPersonalList").hasAnyRole("ADMIN","MIEMBRO")
			.antMatchers("/gestionIncidencias").hasAnyRole("ADMIN")
			.antMatchers("/detallesIncidencia/{incidenciaId}").hasAnyRole("ADMIN","MIEMBRO")
			.antMatchers("/incidenciaUpdateAcuerdo").hasAnyRole("ADMIN","MIEMBRO")
			.antMatchers("/incidenciaDeleteSeguridad/{incidenciaId}").hasAnyRole("ADMIN","MIEMBRO")
			.antMatchers("/incidenciaDelete/{incidenciaId}").hasAnyRole("ADMIN","MIEMBRO");
		 
		 
		 //pdfController
		 http.authorizeRequests()
			.antMatchers("/pdf/{idIncidencia}").hasAnyRole("ADMIN","MIEMBRO");
			

		 //recursos
		 http.authorizeRequests()
		 .antMatchers("/**").hasAnyRole("MIEMBRO, ADMIN")
		 .and().formLogin().loginPage("/login")
		 .permitAll().defaultSuccessUrl("/",true).failureUrl("/login?error=true")
		 .usernameParameter("username").passwordParameter("password").and().logout().logoutSuccessUrl("/");
		 
		 http.exceptionHandling().accessDeniedPage("/denegado");
		
		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();
		
		
		
	}
	@Override
	public void configure(WebSecurity web) {
	    web.ignoring()
	        .antMatchers("/**/*.{js,html,css}");
	}
}
