package com.dlgaApp.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dlgaApp.repository.UserRepository;
 
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
	@Autowired
	private BCryptPasswordEncoder passw;
	
    @Autowired
    private UserRepository userRepository;
     
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        com.dlgaApp.entity.User user = userRepository.getUserByUsername(username);
         
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
         
        return new MyUserDetails(user);
    }
    
    
    @Transactional
    public void guardarusuario(com.dlgaApp.entity.User u) {
    	
    	u.setPassword(passw.encode(u.getPassword()));
    	
    	
    	
    	userRepository.save(u);
    	
    	userRepository.añadirRolUsuario(u.getId());
    }
   
}
