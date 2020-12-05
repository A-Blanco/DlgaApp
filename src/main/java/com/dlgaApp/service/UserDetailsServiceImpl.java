package com.dlgaApp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Alumno;
import com.dlgaApp.entity.Roles;
import com.dlgaApp.entity.Usuario;
import com.dlgaApp.repository.AlumnoRepository;
import com.dlgaApp.repository.UsuarioRepository;
 
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
	@Autowired
	private BCryptPasswordEncoder passw;
	
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AlumnoRepository aRepository;
     
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username);
         
        if (usuario == null) {
            throw new UsernameNotFoundException("No se puede encontrar el usuario");
        }
         
        return usuario;
    }
    
    
    @Transactional
    public void guardarusuario(Usuario u) {
    	
    	u.setPassword(passw.encode(u.getPassword()));
    	
    	u.setRol(Roles.ROLE_MIEMBRO);
    	
    	
    	usuarioRepository.save(u);
    	
    	
    }
    
    public List<Usuario> findAllUsers() {
		
    	
    	return (List<Usuario>) usuarioRepository.findAll() ;
    	
    }
    
    @Transactional
    public void guardarAl(Alumno a) {
    	aRepository.save(a);
    }
    
    public Alumno getAlumnoById(Long i) {
    	return aRepository.findById(i).get();
    }
   
}
