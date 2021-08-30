package com.dlgaApp.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
public class FiltroMantenimiento implements Filter {
   
	private boolean mantenimiento = false;
	
	
	public void activarMantenimiento(){
		
		System.out.println("Mantinimineto activado");
		this.mantenimiento = true;
	}
	
	public void desactivarMantenimiento(){
		System.out.println("Mantinimineto desactivado");
		this.mantenimiento = false;
	}
	
	private static final String MAINTENANCE_PAGE = "/denegado";
	
	
	@Override
   public void destroy() {}

   @Override
   public void doFilter
      (ServletRequest req, ServletResponse res, FilterChain filterchain) 
      throws IOException, ServletException {
	   
	    HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;

	    String servletPath = request.getServletPath();
	    

	    
	    if(mantenimiento &&
	    !servletPath.startsWith(MAINTENANCE_PAGE) && !request.getRequestURI().contains("js") && !request.getRequestURI().contains("css")
	    && !request.getRequestURI().contains("img"))
	    {
	        response.sendRedirect(request.getContextPath() + MAINTENANCE_PAGE);
	        return;
	    }

	    filterchain.doFilter(req, res);
       
  
       
	  
   }

   @Override
   public void init(FilterConfig filterconfig) throws ServletException {}
}