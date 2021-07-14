package com.dlgaApp.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dlgaApp.entity.Usuario;
import com.dlgaApp.repository.UsuarioRepository;
 
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
	@Autowired
	private BCryptPasswordEncoder passw;
	
    @Autowired
    private UsuarioRepository usuarioRepository;
     
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
    public void creaUsuario(Usuario u) {
    	
    	u.setPassword(passw.encode(u.getPassword()));
    	
    	
    	usuarioRepository.save(u);
    	
    	
    }
    
    public List<Usuario> findAllUsers() {
		
    	return (List<Usuario>) usuarioRepository.findAll() ;
    	
    }
    
    public Long numeroUsuariosByUsername(String username) {
    	
    	long i = usuarioRepository.numeroUsuariosByUsername(username);
    	
    	
    	return i;
    }
    
    public Long numeroUsuariosByTelefono(String telefono) {
		return usuarioRepository.countByTelefono(telefono);
    	
    }
    
    public List<Usuario> findAllUsuarios(){
		
    	List<Usuario> l = new ArrayList<Usuario>();
    	l = (List<Usuario>) usuarioRepository.findAll();
    	
    	return l;
		
	}
    
    public Usuario findById(Long id) {
    	return usuarioRepository.findById(id).orElse(null);
    }
    
    public void save(Usuario u) {
    	this.usuarioRepository.save(u);
    }
    
    
   
}
