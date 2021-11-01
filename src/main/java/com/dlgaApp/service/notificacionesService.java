package com.dlgaApp.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.EstadosIncidencia;
import com.dlgaApp.entity.Incidencia;
import com.dlgaApp.entity.Roles;
import com.dlgaApp.entity.Usuario;

@Service
public class notificacionesService {

	@Autowired
	private UserDetailsServiceImpl userSevice;
	
	@Autowired
	private IncidenciaServiceImpl incidenciaSevice;
	
	 public Integer notificacionNuevosUsuarios() {
			
		    Integer numeroUsuariosRegistrados = (int) userSevice.findAllUsuarios().stream()
		    		.filter(x->x.getRol() == Roles.ROLE_REGISTRADO).count();
		    	
		     return numeroUsuariosRegistrados;
		    }
	 
	 public Integer notificacionIncidencias() {
		 
		 	Integer numeroIncidenciasTarde = 0;
		 	
		 	List<Incidencia> incidenciasPersonales =this.incidenciaSevice.finfAll().stream()
		 			.filter(x->x.getMiembro().getId() == usuarioActual().getId())
		 			.collect(Collectors.toList());
		 
		    for (Incidencia i : incidenciasPersonales) {
		    	
		    	LocalDate hoy = LocalDate.now();
		    	Date fecha = i.getFechaCreacion();
		    	java.util.Date safeDate = new Date(fecha.getTime());
		    	
		    	Instant instant = safeDate.toInstant();
		    	
				LocalDate fechaCreacion = instant
						.atZone(ZoneId.systemDefault()).toLocalDate();
				Integer dias= (int) Duration.between(fechaCreacion.atStartOfDay(), hoy.atStartOfDay()).toDays();
				
				if(dias>7 && !i.getEstado().equals(EstadosIncidencia.Finalizada)){
					numeroIncidenciasTarde = numeroIncidenciasTarde + 1;
				}
		    }
		    	
		     return numeroIncidenciasTarde;
		    }
	 
	 private Usuario usuarioActual() {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDetails userDetails = null;
			Usuario us = null;
			if (principal instanceof UserDetails) {
				userDetails = (UserDetails) principal;
				String userName = userDetails.getUsername();
				us = this.userSevice.findByUsername(userName);
			} else {
				us = null;
			}

			return us;

		}
	
}
