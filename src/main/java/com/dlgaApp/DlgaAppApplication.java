package com.dlgaApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DlgaAppApplication {
	
	

	public static void main(String[] args) {
		SpringApplication.run(DlgaAppApplication.class, args);
		
		PasswordEncoder p = new BCryptPasswordEncoder();
		String s = p.encode("1234");
		System.out.println(s);
		
	}

}
